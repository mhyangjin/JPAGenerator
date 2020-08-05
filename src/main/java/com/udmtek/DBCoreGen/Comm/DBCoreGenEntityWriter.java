package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import com.udmtek.DBCoreGen.DBconn.ColumnInfo;
import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenEntityWriter extends DBCoreGenFileWriter {

	public static void generateFile(String path, String packageName, TableInfo tableInfo,Map <String,String> classNameTailMap) {
		DBCoreGenLogger.printInfo("generateFile:" + tableInfo.getTableName() + ":" + tableInfo.getAllColumns().size());
		
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
		
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		String entityTail=classNameTailMap.get("ENTITY");
		className=className.toUpperCase().charAt(0) + className.substring(1);
		List<ColumnInfo> keyColumnInfos= tableInfo.getKeyColumns();
		List<ColumnInfo> columnInfos= tableInfo.getColumns();
		
		classPack.addClassDef("@Table(name=\"" + tableName + "\")");
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
			if (extendClass) {
				classPack.addClassDef("public class " + className + entityTail + " extends DBCoreEntityImpl ");
				classPack.makeImportString("DBCoreEntityImpl");			
			}
			else {
				classPack.addClassDef("public class " + className + entityTail + " implements DBCoreEntity ");
				classPack.makeImportString("DBCoreEntity");	
			}
			//define key columns attribute
			makeKeyColumns(keyColumnInfos, classPack);
			//define columns attribute
			makeColumns(columnInfos, classPack);
			//define key Class attribute
			classPack.addAttrDef("\r\n	@Transient" );
			classPack.addAttrDef("	private Key key;");
			classPack.makeImportString("Transient");
			
			//define footer
			makeFooter(className + entityTail, keyColumnInfos,classPack);
//			DBCoreGenLogger.printInfo(classPack.makeTotals());
			writeStream(bufferWriter, classPack);
			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void makeKeyColumns (List<ColumnInfo> keyColumnInfos,ClassPack classPack) {
		for ( ColumnInfo keyColumnInfo:keyColumnInfos)  {
			String columnName = keyColumnInfo.getColumnName();
			String columnType = keyColumnInfo.getTypeName().toUpperCase();
			ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(columnType);
			
			classPack.addAttrDef("	@Id ");
			classPack.makeImportString("Id");
			classPack.makeImportString("Column");
			String attrAnno="";
			if (keyColumnInfo.getIsNullable().contentEquals("NO")) {
				attrAnno=", nullable=false ";
			}
			if (keyColumnInfo.getTypeName().contains("char") && keyColumnInfo.getMaxLen() > 0 ) {
				attrAnno= attrAnno + ", length=" + keyColumnInfo.getMaxLen();
			}
			attrAnno = attrAnno + ")";
						classPack.addAttrDef("	@Column(name=\"" + columnName +"\" " + attrAnno);
			classPack.addAttrDef("	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";");
			classPack.makeImportString(dbClumnType.getJavaType() );
		}		
	}
	
	public static void makeColumns (List<ColumnInfo> columnInfos,ClassPack classPack) {
		String columnName ="";
		String columnType ="";
		for ( ColumnInfo columnInfo:columnInfos)  {			
			try {
		
				columnName = columnInfo.getColumnName();
				columnType = columnInfo.getTypeName().toUpperCase();
	
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
				classPack.addAttrDef("	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";");
				classPack.makeImportString(dbClumnType.getJavaType() );
			}	catch (Exception e ) {
				DBCoreGenLogger.printInfo("ColumnName:" + columnName + "columnType:"  + columnType);
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
		classPack.addMethodDef("	public " + className + "(" + constArgs  + ") {");
		classPack.addMethodDef("		if ( key == null)");
		classPack.addMethodDef("			key= new Key (" + keyConstArgs + ");");
		classPack.addMethodDef("	}");
		classPack.addMethodDef("	public Key getKey() {");
		classPack.addMethodDef("		return key;");
		classPack.addMethodDef("	}" );
		classPack.addMethodDef("	public Key getKey(" + constArgs + ") {" );
		classPack.addMethodDef("		if ( key == null)");
		classPack.addMethodDef("			key= new Key (" + keyConstArgs + ");");
		classPack.addMethodDef("		return key;");
		classPack.addMethodDef("	}");
		
		classPack.addMethodDef("	public static class Key extends DBCoreEntityKeyImpl {") ;
		classPack.makeImportString("DBCoreEntityKeyImpl");
		
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
	}
}
