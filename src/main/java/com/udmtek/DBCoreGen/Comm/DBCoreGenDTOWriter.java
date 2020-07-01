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


public class DBCoreGenDTOWriter extends DBCoreGenFileWriter {
	private static final String DTOHeader = 
			"import java.util.Date;\r\n" + 
			"import org.springframework.context.annotation.Scope;\r\n" + 
			"import org.springframework.stereotype.Component;\r\n" + 
			"import com.udmtek.DBCore.DAOModel.DBCoreDTO;\r\n" + 
			"import lombok.Getter;\r\n" + 
			"import lombok.Setter;";
	private static final String DTOClassDefine = 
			"@Getter\r\n" + 
			"@Setter\r\n" + 
			"@Component\r\n" + 
			"@Scope(value = \"prototype\" )" +
			"\r\n" + 
			"public class <CLASSNAME> implements DBCoreDTO {";
	private static final String DTOFooterDefine = 
			"	@Override\r\n" + 
			"	protected Object clone() throws CloneNotSupportedException{\r\n" + 
			"		return super.clone();\r\n" + 
			"	}\r\n" ;
	public static void generateFile(String path, String packageName, TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName)+"DTO";
		className=className.toUpperCase().charAt(0) + className.substring(1);
		
		List<ColumnInfo> columnInfos= tableInfo.getAllColumns();
		File writeFile = makeFile(path + "//"+ className + ".java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			//Header 
			writeStream(bufferWriter, "package " + packageName+";");
			writeStream(bufferWriter, DTOHeader );
			//comment
			writeStream(bufferWriter, commentString );
			//class define
			String classDefine =  DTOClassDefine.replaceAll("<CLASSNAME>", className);
			writeStream(bufferWriter, classDefine );
			//column columns define
			List<String> columndefines=mekeColumns(columnInfos);
			writeStream(bufferWriter, columndefines);
			//Footer
			writeStream(bufferWriter, DTOFooterDefine );
			writeStream(bufferWriter, "}");

			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> mekeColumns (List<ColumnInfo> columnInfos) {
		List<String> constStrings = new ArrayList<String>();
		for ( ColumnInfo columnInfo:columnInfos)  {			
			String columnName ="";
			String columnType ="";
			try {
		
				columnName = columnInfo.getColumnName();
				columnType = columnInfo.getTypeName().toUpperCase();
				ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(columnType);
					constStrings.add("	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";");
			}	catch (Exception e ) {
				DBCoreGenLogger.printInfo("ColumnName:" + columnName + "columnType:"  + columnType);
				throw e;
			}
		}		
		return constStrings;	
	}
}
