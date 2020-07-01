package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenMapperWriter extends DBCoreGenFileWriter {
	private static final String MapperlHeader = 
			"import org.springframework.stereotype.Component;\r\n" +
			"import com.udmtek.DBCore.DAOModel.DBCOreDTOMapperImpl;";

	private static final String MapperlClassDefine =
				"@Component\r\n" + 
				"public class <CLASSNAME>Mapper extends DBCOreDTOMapperImpl< <CLASSNAME>, <CLASSNAME>DTO > {\r\n";
	
	public static void generateFile(String path, String packageName, TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		className=className.toUpperCase().charAt(0) + className.substring(1);		
		
		File writeFile = makeFile(path + "//"+ className + "Mapper.java");
		BufferedWriter bufferWriter =null;
		
		try {
		bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
		//Header 
		writeStream(bufferWriter, "package " + packageName+";");
		writeStream(bufferWriter, MapperlHeader );
		//comment
		writeStream(bufferWriter, commentString );
		//class define
		String classDefine =  MapperlClassDefine.replaceAll("<CLASSNAME>", className);
		writeStream(bufferWriter, classDefine );
		//key columns define
		List<String>  constructString=makeconstructor(className, className);
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
		constructString.add("	" + className + "Mapper() {" );
		constructString.add("		super(" + className + ".class," + className + "DTO.class);" );
		constructString.add("	}");
		return constructString;
	}
}
