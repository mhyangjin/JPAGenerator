package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenDAOWriter extends DBCoreGenFileWriter {
	private static final String DAOHeader = 
							"import org.springframework.beans.factory.annotation.Autowired;\r\n" +
							"import org.springframework.stereotype.Repository;\r\n" + 
							"import com.udmtek.DBCore.DAOModel.GenericDAOImpl;\r\n";
	
	private static final String DAOClassDefine =
							"@Repository(\"<CLASSNAME>DAO\")\r\n" + 
							"public class <CLASSNAME>DAO extends GenericDAOImpl< <CLASSNAME>, <CLASSNAME>DTO, <CLASSNAME>Mapper > implements <CLASSNAME>Interface {\r\n" +
							"	@Autowired\r\n" + 
							"	<CLASSNAME>Mapper mapper;\r\n";
	
	public static void generateFile(String path, String packageName, TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		className=className.toUpperCase().charAt(0) + className.substring(1);
		
		File writeFile = makeFile(path + "//"+ className + "DAO.java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			//Header 
			writeStream(bufferWriter, "package " + packageName + ";");
			writeStream(bufferWriter, DAOHeader );
			//comment
			writeStream(bufferWriter, commentString );
			//class define
			String classDefine =  DAOClassDefine.replaceAll("<CLASSNAME>", className);
			writeStream(bufferWriter, classDefine );
			//key columns define
			 List<String>  constructString=makeconstructor(className);
			writeStream(bufferWriter, constructString);
			writeStream(bufferWriter, "}");

			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> makeconstructor(String className) {
		List<String> constructString = new ArrayList<String>();
		constructString.add("	public " + className + "DAO() {" );
		constructString.add("		super(" +className +".class," + className + "DTO.class);" );
		constructString.add("	}");
		return constructString;
	}
}
