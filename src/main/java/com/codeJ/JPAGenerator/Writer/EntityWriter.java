package com.codeJ.JPAGenerator.Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codeJ.JPAGenerator.Comm.ColumnInfo;
import com.codeJ.JPAGenerator.Comm.ColumnTypeEnum;
import com.codeJ.JPAGenerator.Comm.PropertyFileReader;
import com.codeJ.JPAGenerator.Comm.TableInfo;
import io.netty.util.internal.StringUtil;

public class EntityWriter extends FileWriter {
	private static Logger logger = LoggerFactory.getLogger(EntityWriter.class);
	
	public static void generateFile(	PropertyFileReader configReader,
										TableInfo tableInfo,
										Map <String,String> classNameTailMap) {
		String tableName= tableInfo.getTableName();
		String className= FileWriter.convertCamel(tableName);
		String path=configReader.getExtractPath() + "//" + className;
		String packageName = configReader.getPackageName() + "." + className;
		boolean tableJoin=configReader.isTableJoin();
		
		logger.debug("generateFile:{}:{}", tableInfo.getTableName(),tableInfo.getAllColumns().size());

		
		ClassPack classPack=setClassDefine(packageName);
		classPack.addClassDef("//update SQL 생성 시 빈칼럼은 제거한다.");
		classPack.addClassDef("@org.hibernate.annotations.DynamicUpdate");
		classPack.addClassDef("//insert SQL 생성 시 빈칼럼은 제거한다."); 
		classPack.addClassDef("@org.hibernate.annotations.DynamicInsert");

		classPack.addClassDef("@Getter");
		classPack.addClassDef("@ToString");
		classPack.addClassDef("@Entity");
		classPack.makeImportString("Getter");
		classPack.makeImportString("ToString");
		classPack.makeImportString("Entity");
		
		String entityTail=classNameTailMap.get("ENTITY");
		className=className.toUpperCase().charAt(0) + className.substring(1);
		List<ColumnInfo> keyColumnInfos= tableInfo.getKeyColumns();
		List<ColumnInfo> columnInfos= tableInfo.getColumns();
		
		classPack.addClassDef("@Table(name=\"" + tableName + "\")");
//		if ( keyColumnInfos.size() >= 2 )	//primary key가 2개 이상일 경우 복합키 생성
			classPack.addClassDef("@IdClass(" + className + entityTail + ".Key.class)");
		
		classPack.makeImportString("Table");
		classPack.makeImportString("IdClass");
		
		boolean extendClass = false;
		if ( columnInfos.size() !=  tableInfo.getExceptCreateUpdateInfo().size()) {
			extendClass = true;
			columnInfos=tableInfo.getExceptCreateUpdateInfo();
		}
		
		File writeFile = makeFile(path + "//"+ className + entityTail + ".java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			if (extendClass) {			// column에 history column 이 있으면 이미 구현된 class로부터 상속 
										// (creator_id, create_date_time, updator_id, update_data_time
				classPack.addClassDef("public class " + className + entityTail + " extends GenericEntityImpl ");
				classPack.makeImportString("GenericEntityImpl");			
			}
			else {
				classPack.addClassDef("public class " + className + entityTail + " implements GenericEntity ");
				classPack.makeImportString("GenericEntity");	
			}
			//define key columns attribute
			makeKeyColumns(tableInfo, classPack);
			//define columns attribute
			makeColumns(columnInfos, classPack,tableJoin,configReader.getPackageName());
//			if ( keyColumnInfos.size() >= 2 )	{ //primary key가 2개 이상일 경우 복합키 생성
				//define key Class attribute
				classPack.addAttrDef("\r\n	@Transient" );
				classPack.addAttrDef("	private Key key;");
				classPack.makeImportString("Transient");
//			}
			//define footer
			makeFooter(className + entityTail, keyColumnInfos, classPack);
//			logger.info(classPack.makeTotals());
			writeStream(bufferWriter, classPack);
			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void makeKeyColumns (TableInfo tableInfo,ClassPack classPack) {
		String tableName= tableInfo.getTableName();
		String className= FileWriter.convertCamel(tableName);
		List<ColumnInfo> keyColumnInfos=tableInfo.getKeyColumns();
		
		for ( ColumnInfo keyColumnInfo:keyColumnInfos)  {
			String columnName = keyColumnInfo.getColumnName();
			String columnType = keyColumnInfo.getTypeName().toUpperCase();
			ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(columnType);
			
			classPack.addAttrDef("	@Id ");
			classPack.makeImportString("Id");
			classPack.makeImportString("Column");
			String attrAnno="";
			logger.info("DATA Type {}",keyColumnInfo.getTypeName());
			if (keyColumnInfo.getTypeName().contains("bigint")) {
				String GeneratorName=className+"_SEQ_GENERATOR";
				String SequenceName=tableName + "_" + columnName + "_seq";
				String generateString = "	 @GeneratedValue(strategy=GenerationType.SEQUENCE,\r\n" +
										"			generator=\"" + GeneratorName + "\")";
				
				classPack.addAttrDef(generateString);
				String sequenceString = "	 @SequenceGenerator(name=\"" + GeneratorName +"\",\r\n" +
										"			sequenceName=\"" + SequenceName + "\",\r\n" +
										"			initialValue=1,allocationSize=1 )";
						
				classPack.addAttrDef(sequenceString);
				classPack.makeImportString("GeneratedValue");
				classPack.makeImportString("GenerationType");
				classPack.makeImportString("SequenceGenerator");
			}
			
			if (keyColumnInfo.getIsNullable().contentEquals("NO")) {
				attrAnno=", nullable=false ";
			}
			if (keyColumnInfo.getTypeName().contains("char") && keyColumnInfo.getMaxLen() > 0 ) {
				attrAnno= attrAnno + ", length=" + keyColumnInfo.getMaxLen();
			}
			attrAnno = attrAnno + ")";
						classPack.addAttrDef("	@Column(name=\"" + columnName +"\" " + attrAnno);
			classPack.addAttrDef("	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";\r\n");
			classPack.makeImportString(dbClumnType.getJavaType() );
		}		
	}
	
	public static void makeColumns (List<ColumnInfo> columnInfos,ClassPack classPack,boolean tableJoin,String packageName) {
		String columnName ="";
		String columnType ="";

		for ( ColumnInfo columnInfo:columnInfos)  {			
			try {
				
				columnName = columnInfo.getColumnName();
				columnType = columnInfo.getTypeName().toUpperCase();
//				logger.debug("BEfore ColumnName:{}:{}:{}",columnName,tableJoin,columnInfos.size());
				if ( tableJoin == false || StringUtil.isNullOrEmpty(columnInfo.getConstsraintTable())) {
//					logger.debug(" ColumnName:{}",columnName );
					ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(columnType);
					String attrAnno = "	" +"@Column(name=\"" + columnName +"\" ";
					if (columnInfo.getIsNullable().contentEquals("NO")) {
						attrAnno= attrAnno + ", nullable=false ";
					}
					if (columnInfo.getTypeName().contains("char") && columnInfo.getMaxLen() > 0 ) {
						attrAnno= attrAnno + ", length=" + columnInfo.getMaxLen();
					}
					attrAnno= attrAnno +")";
					if ( dbClumnType.getJavaType().equals("Date")) {
						attrAnno= attrAnno +"\r\n	@Temporal(TemporalType.TIMESTAMP)";
						classPack.makeImportString("Temporal");
						classPack.makeImportString("TemporalType");
					}	
					classPack.addAttrDef(attrAnno);
					classPack.addAttrDef("	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";\r\n");
					classPack.makeImportString(dbClumnType.getJavaType() );
				}
				else {		//fkey가 있으며 tableJoin이 true인 경우

					String constraintTableAttr = convertCamel(columnInfo.getConstsraintTable());
					String conastaintTableClass=constraintTableAttr.toUpperCase().charAt(0) + constraintTableAttr.substring(1);
					String attrAnno = "	@ManyToOne\r\n	@JoinColumn(name=\"" + columnName + "\")";
						classPack.makeImportString("ManyToOne");
						classPack.makeImportString("JoinColumn");
					
					classPack.addAttrDef(attrAnno);
					classPack.addAttrDef("	private "  + conastaintTableClass+ " " + constraintTableAttr + ";\r\n");
					String ImportString="import " + packageName +"." + constraintTableAttr +"." + conastaintTableClass + ";\r\n";
					classPack.addImportEntityClass( ImportString);
				}	
			}	catch (Exception e ) {
				e.printStackTrace();
				logger.error("ColumnName:{}:{}" ,columnName,columnType);
				throw e;
			}
		}		
	}
	
	public static void makeFooter(String className,List<ColumnInfo> keyColumnInfos,ClassPack classPack) {
		String constArgs=null;
		String keyConstArgs=null;
		String[] keytypes= new String[keyColumnInfos.size()];
		String[] keyColumns= new String[keyColumnInfos.size()];
		
		for ( int i=0; i <keyColumnInfos.size(); i++)  {
			ColumnInfo keyColumnInfo = keyColumnInfos.get(i);
			ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(keyColumnInfo.getTypeName().toUpperCase());
			keytypes[i] = dbClumnType.getJavaType();
			keyColumns[i] = convertCamel(keyColumnInfo.getColumnName());
			if (constArgs == null) 
				constArgs=   keytypes[i] + " "  + keyColumns[i];
			else
				constArgs = constArgs + "," +  keytypes[i] + " "  + keyColumns[i];
			if (keyConstArgs== null) 
				keyConstArgs = keyColumns[i];
			else
				keyConstArgs = keyConstArgs + "," +  keyColumns[i];
		}
		classPack.addMethodDef("	public String ToString() {");
		classPack.addMethodDef("		return this.toString() + super.toString();");
		classPack.addMethodDef("	}");
		classPack.addMethodDef("	public " + className + "() {}");
//		if ( keyColumnInfos.size() >= 2 )	{ //primary key가 2개 이상일 경우 복합키 생성
			classPack.addMethodDef("	public " + className + "(" + constArgs  + ") {");
			for (String keyArg:keyColumns)
				classPack.addMethodDef("		this." + keyArg + "= " + keyArg + ";");	
			classPack.addMethodDef("		if ( key == null)");
			classPack.addMethodDef("			key= new Key (" + keyConstArgs + ");");
			classPack.addMethodDef("	}");
			classPack.addMethodDef("	public GenericEntityKeyImpl getKey() {");
			classPack.addMethodDef("		if ( key == null)");
			classPack.addMethodDef("			key= new Key (" + keyConstArgs + ");");
			classPack.addMethodDef("		return key;");
			classPack.addMethodDef("	}" );
			classPack.addMethodDef("	public Key getKey(" + constArgs + ") {" );
			classPack.addMethodDef("		if ( key == null)");
			classPack.addMethodDef("			key= new Key (" + keyConstArgs + ");");
			classPack.addMethodDef("		return key;");
			classPack.addMethodDef("	}");
		
			classPack.addMethodDef("	public static class Key extends GenericEntityKeyImpl {") ;
			classPack.makeImportString("GenericEntityKeyImpl");
			classPack.makeImportString("GenericEntityKey");
			int i=0;
			for (String keyString :keyColumns) {
				String keyType =keytypes[i++];
				classPack.addMethodDef("		@Getter");
				classPack.addMethodDef("		private " + keyType + " " +keyString + ";" );
				classPack.makeImportString(keyType);
			}
			classPack.addMethodDef("		public Key() {");
			classPack.addMethodDef("			super (Key.class);");
			classPack.addMethodDef("		}");
			classPack.addMethodDef("		public Key(" + constArgs + ") {");
			classPack.addMethodDef("			super (Key.class);");
			
			for (String keyString:keyColumns) {
				classPack.addMethodDef("		this."+ keyString + "=" + keyString +";");
			}
			classPack.addMethodDef("		}");
			classPack.addMethodDef("	}");
//		}
	}
}
