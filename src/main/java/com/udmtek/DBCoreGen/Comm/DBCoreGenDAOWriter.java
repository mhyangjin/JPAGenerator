package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.udmtek.DBCoreGen.DBconn.ColumnInfo;
import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenDAOWriter extends DBCoreGenFileWriter {
	private static final String DAOHeader = 
			"import java.util.Date;\r\n" + 
			"import javax.persistence.Column;\r\n" + 
			"import javax.persistence.Entity;\r\n" + 
			"import javax.persistence.Id;\r\n" + 
			"import javax.persistence.IdClass;\r\n" + 
			"import javax.persistence.Table;\r\n" + 
			"import javax.persistence.Temporal;\r\n" + 
			"import javax.persistence.TemporalType;\r\n" + 
			"import javax.persistence.Transient;\r\n" + 
			"import com.udmtek.DBCore.DAOModel.EntityImpl;\r\n" + 
			"import com.udmtek.DBCore.DAOModel.EntityKeyImpl;\r\n" + 
			"import lombok.Getter;\r\n" + 
			"import lombok.Setter;\r\n" + 
			"import lombok.ToString;\r\n" + 
			"\r\n" + 
			"//update SQL 생성 시 빈칼럼은 제거한다.\r\n" + 
			"@org.hibernate.annotations.DynamicUpdate\r\n" + 
			"//insert SQL 생성 시 빈칼럼은 제거한다.\r\n" + 
			"@org.hibernate.annotations.DynamicInsert\r\n";
	
	private static final String DAOClassDefine=	
			"@Getter\r\n" + 
			"@Setter \r\n" + 
			"@ToString\r\n" + 
			"@Entity\r\n" +
			"@Table(name=\"<TABLENAME>\")\r\n" + 
			"@IdClass(<CLASSNAME>.Key.class)\r\n" + 
			"public class <CLASSNAME> extends EntityImpl {";
	
	private static  final String DAOColumnDefine="@Column(name=\"<COLUMNNAME>\")";
	private static  final String DAOkeyDefine=
											"	@Transient\r\n" + 
											"	private Key key;\r\n" + 
											"	\r\n" ;
	private static  final String keyClassDefine="	public static class Key extends EntityKeyImpl {\r\n" + 
			"		private static final long serialVersionUID = 1L;\r\n" ;
	
	public static void generateFile(String path, String packageName, TableInfo tableInfo) {
		DBCoreGenLogger.printInfo("generateFile:" + tableInfo.getTableName() + ":" + tableInfo.getAllColumns().size());
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName)+"DAO";
		className=className.toUpperCase().charAt(0) + className.substring(1);
		List<ColumnInfo> keyColumnInfos= tableInfo.getKeyColumns();
		List<ColumnInfo> columnInfos= tableInfo.getColumns();
		File writeFile = makeFile(path + "//"+ className + ".java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			//Header 
			writeStream(bufferWriter, "package " + packageName + ".DAO;");
			writeStream(bufferWriter, DAOHeader );
			//comment
			writeStream(bufferWriter, commentString );
			//class define
			String classDefine =  DAOClassDefine.replaceAll("<TABLENAME>", tableName)
					             .replaceAll("<CLASSNAME>", className);
			writeStream(bufferWriter, classDefine );
			//key columns define
			String[][] keydefines=makeKeyColumns(keyColumnInfos);
			writeStream(bufferWriter, keydefines);
			//columns define
			String[][] columndefines=mekeColumns(columnInfos);
			writeStream(bufferWriter, columndefines);
			//transient key define
			writeStream(bufferWriter, DAOkeyDefine);
			//footer define
			List<String> constStrings = makeFooter(className, keyColumnInfos);
			writeStream(bufferWriter, constStrings);
			writeStream(bufferWriter, "}");

			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[][] makeKeyColumns (List<ColumnInfo> keyColumnInfos) {
		String[][] keydefines= new String[keyColumnInfos.size()][2];
		int i=0;
		for ( ColumnInfo keyColumnInfo:keyColumnInfos)  {
			String columnName = keyColumnInfo.getColumnName();
			String columnType = keyColumnInfo.getTypeName().toUpperCase();
			ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(columnType);
			keydefines[i][0] = "	@Id " + DAOColumnDefine.replaceAll("<COLUMNNAME>", columnName);
			keydefines[i++][1] = "	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";";
		}		
		return keydefines;	
	}
	
	public static String[][] mekeColumns (List<ColumnInfo> columnInfos) {
		String[][] columndefines= new String[columnInfos.size()][2];
		int i=0;
		String columnName ="";
		String columnType ="";
		for ( ColumnInfo columnInfo:columnInfos)  {			
			try {
		
				columnName = columnInfo.getColumnName();
				columnType = columnInfo.getTypeName().toUpperCase();
	
				ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(columnType);
					
				columndefines[i][0] = "	" + DAOColumnDefine.replaceAll("<COLUMNNAME>", columnName);
				if ( dbClumnType.getJavaType().equals("Date")) {
					columndefines[i][0] =columndefines[i][0] +"\r\n	@Temporal(TemporalType.TIMESTAMP)";
				}	
				columndefines[i++][1] = "	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";";
			}	catch (Exception e ) {
				DBCoreGenLogger.printInfo("ColumnName:" + columnName + "columnType:"  + columnType);
				throw e;
			}
		}		
		return columndefines;	
	}
	
	public static List<String> makeFooter(String className,List<ColumnInfo> keyColumnInfos) {
		List<String> constStrings = new ArrayList<String>();
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
		constStrings.add("	public " + className + "() {}\r\n");
		constStrings.add("	public " + className + "(" + constArgs  + ") {");
		constStrings.add("		if ( key == null)");
		constStrings.add("			key= new Key (" + keyConstArgs + ");");
		constStrings.add("	}\r\n");
		constStrings.add("	public Key getKey() {\r\n" + 
						 "		return key;\r\n" + 
						 "	}\r\n" );
		constStrings.add("	public Key getKey(" + constArgs + ") {" );
		constStrings.add("		if ( key == null)");
		constStrings.add("			key= new Key (" + keyConstArgs + ");");
		constStrings.add("		return key;");
		constStrings.add("	}\r\n");
		constStrings.add(keyClassDefine );
		int i=0;
		for (String keyString :keyColumns) {
			String keyType =keytypes[i++];
			constStrings.add("		@Getter\r\n		@Setter");
			constStrings.add("		private " + keyType + " " +keyString + ";" );
		}
		constStrings.add("		public Key() {");
		constStrings.add("			super (Key.class);");
		constStrings.add("		}");
		constStrings.add("		public Key(" + constArgs + ") {");
		constStrings.add("			super (Key.class);");
		for (String keyString:keyColumns) {
			constStrings.add("		this."+ keyString + "=" + keyString +";");
		}
		constStrings.add("		}\r\n" + 
						"	}");		
		return constStrings;
	}
}
