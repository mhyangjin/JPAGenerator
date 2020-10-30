package kr.co.codeJ.JPAGenerator.Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import kr.co.codeJ.JPAGenerator.Comm.ColumnInfo;
import kr.co.codeJ.JPAGenerator.Comm.ColumnTypeEnum;
import kr.co.codeJ.JPAGenerator.Comm.TableInfo;
import kr.co.codeJ.JPAGenerator.Writer.FileWriter.ClassPack;

public class DAOWriter extends FileWriter {
	
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
			makeget(className,classNameTailMap,tableInfo, classPack);;
			makedelete(className,classNameTailMap,tableInfo, classPack);;
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
	
	private static void makeget (String className,Map <String,String> classNameTailMap,TableInfo tableInfo,ClassPack classPack) {
		List<ColumnInfo> keyColumnInfos=tableInfo.getKeyColumns();
		String constArgs=null;
		String keyConstArgs=null;
		String[] keytypes= new String[keyColumnInfos.size()];
		String[] keyColumns= new String[keyColumnInfos.size()];
		String DtoName=className + classNameTailMap.get("DTO");
		String EntityName=className + classNameTailMap.get("ENTITY");
		
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
		
		classPack.addMethodDef("	public " + DtoName + " get(" + constArgs  + ") {");
		classPack.addMethodDef("		return this.get(new " + EntityName + ".Key(" + keyConstArgs + "));");
		classPack.addMethodDef("	}");
	}
	
	private static void makedelete (String className,Map <String,String> classNameTailMap, TableInfo tableInfo,ClassPack classPack) {
		List<ColumnInfo> keyColumnInfos=tableInfo.getKeyColumns();
		String constArgs=null;
		String keyConstArgs=null;
		String[] keytypes= new String[keyColumnInfos.size()];
		String[] keyColumns= new String[keyColumnInfos.size()];
		String DtoName=className + classNameTailMap.get("DTO");
		String EntityName=className + classNameTailMap.get("ENTITY");
		
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
		
		classPack.addMethodDef("	public void delete(" + constArgs  + ") {");
		classPack.addMethodDef("		this.delete(new " + EntityName + ".Key(" + keyConstArgs + "));");
		classPack.addMethodDef("	}");
	}
}
