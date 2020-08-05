package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenDAOInfWriter extends DBCoreGenFileWriter {
	public static void generateFile(String path, String packageName, TableInfo tableInfo,Map <String,String> classNameTailMap) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		className=className.toUpperCase().charAt(0) + className.substring(1);
		String InfClassName= className+ classNameTailMap.get("INF");
		
		File writeFile = makeFile(path + "//"+ InfClassName + ".java");
		
		ClassPack classPack=setClassDefine(packageName);
		
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			
			classPack.addClassDef("public interface "+ InfClassName );
			writeStream(bufferWriter, classPack);
			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}