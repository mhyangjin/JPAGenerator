package kr.co.codeJ.JPAGenerator.Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import kr.co.codeJ.JPAGenerator.Comm.TableInfo;

public class MapperWriter extends FileWriter {
	public static void generateFile(String path, String packageName, TableInfo tableInfo,Map <String,String> classNameTailMap) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		className=className.toUpperCase().charAt(0) + className.substring(1);	
		
		String MapperName=className + classNameTailMap.get("MAPPER");
		String DTOName=className + classNameTailMap.get("DTO");
		String EntityName=className + classNameTailMap.get("ENTITY");
		
		File writeFile = makeFile(path + "//"+ MapperName + ".java");
		BufferedWriter bufferWriter =null;
		ClassPack classPack=setClassDefine(packageName);
		try {
		bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));

		classPack.addClassDef("@Component");
		classPack.addClassDef("public class "+MapperName+" extends GenericDTOMapperImpl<"+EntityName+", "+DTOName+" >");
		classPack.makeImportString("Component");
		classPack.makeImportString("GenericDTOMapperImpl");
		
		//key columns define
		makeconstructor(className, classNameTailMap,classPack);
		
		writeStream(bufferWriter, classPack);
		
		bufferWriter.flush();
		bufferWriter.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	public static void makeconstructor(String className,Map <String,String> classNameTailMap, ClassPack classPack) {
		String MapperName=className + classNameTailMap.get("MAPPER");
		String DtoName=className + classNameTailMap.get("DTO");
		String EntityName=className + classNameTailMap.get("ENTITY");
		classPack.addMethodDef("	" + MapperName + "() {" );
		classPack.addMethodDef("		super(" + EntityName + ".class," + DtoName + ".class);" );
		classPack.addMethodDef("	}");
		
	}
}
