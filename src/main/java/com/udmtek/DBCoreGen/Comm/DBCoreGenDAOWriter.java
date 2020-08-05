package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenDAOWriter extends DBCoreGenFileWriter {
	
	public static void generateFile(String path, String packageName, TableInfo tableInfo,Map <String,String> classNameTailMap) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		className=className.toUpperCase().charAt(0) + className.substring(1);
		String DAOName=className + classNameTailMap.get("DAO");
		String EntityName=className + classNameTailMap.get("ENTITY");
		String DtoName=className + classNameTailMap.get("DTO");
		String MapperName=className + classNameTailMap.get("MAPPER");
		String InfName=className + classNameTailMap.get("INF");
		
		ClassPack classPack=setClassDefine(packageName);
		
		File writeFile = makeFile(path + "//"+DAOName + ".java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			//class define
			classPack.addClassDef("@Repository(\""+DAOName+"\")");
			String classDefine = "public class "+DAOName+" extends GenericDAOImpl< "+EntityName+", "+DtoName+","+MapperName +"> ";

			if (!classNameTailMap.get("INF").contains("NONE") )
				classDefine = classDefine + "implements "+ InfName;

			classPack.addClassDef(classDefine);
			classPack.addAttrDef("	@Autowired");
			classPack.addAttrDef("	" + MapperName+" mapper;");
			classPack.makeImportString("Repository");;
			classPack.makeImportString("GenericDAOImpl");
			classPack.makeImportString("Autowired");

			//key columns define
			makeconstructor(className,classNameTailMap,classPack);
			writeStream(bufferWriter, classPack);

			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void makeconstructor(String className,Map <String,String> classNameTailMap, ClassPack classPack) {
		String DaoName=className + classNameTailMap.get("DAO");
		String EntityName=className + classNameTailMap.get("ENTITY");
		String DtoName=className + classNameTailMap.get("DTO");
		classPack.addMethodDef("	public " + DaoName+ "() {" );
		classPack.addMethodDef("		super(" +EntityName + ".class," + DtoName + ".class);");
		classPack.addMethodDef("	}");
	
	}
}
