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

public class DBCoreGenInfoImplWriter extends DBCoreGenFileWriter {
	private static final String InfoImplHeader = 
			"import org.springframework.stereotype.Repository;\r\n" + 
			"import com.udmtek.DBCore.DAOModel.GenericDAOImpl;";

	private static final String InfoImplClassDefine =
				"@Repository(\"<CLASSNAME>\")\r\n" + 
				"public class <CLASSNAME> extends GenericDAOImpl< <INFONAME> > {";
	
	public static void generateFile(String path, String packageName, TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName)+"InfoImpl";
		String InfoName= convertCamel(tableName)+"Info";
		className=className.toUpperCase().charAt(0) + className.substring(1);
		InfoName=InfoName.toUpperCase().charAt(0) + InfoName.substring(1);
		
		List<ColumnInfo> keyColumnInfos= tableInfo.getKeyColumns();
		File writeFile = makeFile(path + "//"+ className + ".java");
		BufferedWriter bufferWriter =null;
		
		try {
		bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
		//Header 
		writeStream(bufferWriter, "package " + packageName + ".InfoImpl;");
		writeStream(bufferWriter, InfoImplHeader );
		writeStream(bufferWriter, "import " +packageName + ".Info." +  InfoName + ";");
		//comment
		writeStream(bufferWriter, commentString );
		//class define
		String classDefine =  InfoImplClassDefine.replaceAll("<INFONAME>", InfoName)
			             .replaceAll("<CLASSNAME>", className);
		writeStream(bufferWriter, classDefine );
		//key columns define
		List<String>  constructString=makeconstructor(className, InfoName);
		writeStream(bufferWriter, constructString);
		writeStream(bufferWriter, "}");
		
		bufferWriter.flush();
		bufferWriter.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	public static List<String> makeconstructor(String className,String InfoName) {
		List<String> constructString = new ArrayList<String>();
		constructString.add("	public " + className + "() {" );
		constructString.add("		super(" + InfoName + ".class);" );
		constructString.add("	}");
		return constructString;
	}
}
