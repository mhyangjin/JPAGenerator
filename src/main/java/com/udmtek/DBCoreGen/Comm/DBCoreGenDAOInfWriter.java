package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenDAOInfWriter extends DBCoreGenFileWriter {
	private static final String DAOInfHeader =
			"public interface <CLASSNAME>Interface {";

	public static void generateFile(String path, String packageName, TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		className=className.toUpperCase().charAt(0) + className.substring(1);
		
		File writeFile = makeFile(path + "//"+ className + "Interface.java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			//Header 
			writeStream(bufferWriter, "package " + packageName + ";");
			writeStream(bufferWriter, commentString );
			//class define
			String classDefine =  DAOInfHeader.replaceAll("<CLASSNAME>", className);
			writeStream(bufferWriter, classDefine );
			writeStream(bufferWriter, "}");
			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}