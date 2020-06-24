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

public class DBCoreGenInfoWriter extends DBCoreGenFileWriter {
	private static final String InfoHeader = 
			"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
			"import org.springframework.context.ApplicationContext;\r\n" + 
			"import org.springframework.context.annotation.Scope;\r\n" + 
			"import java.util.Date;\r\n" +
			"import org.springframework.stereotype.Component;\r\n";
	private static final String InfoClassDefine = 
			"@Component\r\n" + 
			"@Scope(value = \"prototype\" )\r\n" +
			"public class <CLASSNAME> extends <DAONAME> {\r\n" + 
			"	@Autowired\r\n" + 
			"	static ApplicationContext context;\r\n" ;

	public static void generateFile(String path, String packageName, TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName)+"Info";
		String daoName= convertCamel(tableName)+"DAO";
		className=className.toUpperCase().charAt(0) + className.substring(1);
		daoName=daoName.toUpperCase().charAt(0) + daoName.substring(1);
		
		List<ColumnInfo> keyColumnInfos= tableInfo.getKeyColumns();
		File writeFile = makeFile(path + "//"+ className + ".java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			//Header 
			writeStream(bufferWriter, "package " + packageName + ".Info;");
			writeStream(bufferWriter, InfoHeader );
			writeStream(bufferWriter, "import " + packageName + ".DAO." + daoName + ";");
			//comment
			writeStream(bufferWriter, commentString );
			//class define
			String classDefine =  InfoClassDefine.replaceAll("<DAONAME>", daoName)
					             .replaceAll("<CLASSNAME>", className);
			writeStream(bufferWriter, classDefine );
			//key columns define
			 List<String>  constructString=makeconstructor(className, keyColumnInfos);
			writeStream(bufferWriter, constructString);
			writeStream(bufferWriter, "}");

			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> makeconstructor(String className,List<ColumnInfo> keyColumnInfos) {
		List<String> constructString = new ArrayList<String>();
		constructString.add("	public " + className + "() {}" );
		String consArgs=null;
		String consSets=null;
		for (ColumnInfo keyColumnInfo: keyColumnInfos) {
			ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(keyColumnInfo.getTypeName().toUpperCase());
			String keytype = dbClumnType.getJavaType();
			String keyColumn = convertCamel(keyColumnInfo.getColumnName());
			
			if ( consArgs == null) 
				consArgs = keytype + " " + keyColumn;
			else
				consArgs = consArgs + "," + keytype + " " + keyColumn;
			
			if ( consSets == null)
				consSets = keyColumn;
			else
				consSets = consSets + "," + keyColumn;
		}
		
		constructString.add("	public " + className + "("+consArgs + ") {");
		constructString.add("	super(" + consSets + ");");
		constructString.add("	}");
		return constructString;
	}
}
