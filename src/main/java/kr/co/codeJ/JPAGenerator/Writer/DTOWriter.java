package kr.co.codeJ.JPAGenerator.Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.codeJ.JPAGenerator.Comm.ColumnInfo;
import kr.co.codeJ.JPAGenerator.Comm.ColumnTypeEnum;
import kr.co.codeJ.JPAGenerator.Comm.TableInfo;

public class DTOWriter extends FileWriter {
	private static Logger logger = LoggerFactory.getLogger(DTOWriter.class);
	
	public static void generateFile(String path, String packageName, TableInfo tableInfo,Map <String,String> classNameTailMap) {
		String tableName= tableInfo.getTableName();
		String className= convertCamel(tableName);
		className=className.toUpperCase().charAt(0) + className.substring(1);
		String DtoName= className + classNameTailMap.get("DTO");
		List<ColumnInfo> keyColumnInfos= tableInfo.getKeyColumns();
		List<ColumnInfo> columnInfos= tableInfo.getColumns();
		boolean extendClass = false;
		if ( columnInfos.size() !=  tableInfo.getExceptCreateUpdateInfo().size()) {
			extendClass = true;
			columnInfos=tableInfo.getExceptCreateUpdateInfo();
		}
		
		ClassPack classPack=setClassDefine(packageName);
		
		File writeFile = makeFile(path + "//"+ DtoName + ".java");
		BufferedWriter bufferWriter =null;
		
		try {
			bufferWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(writeFile),"UTF8"));
			classPack.addClassDef("@Getter");
			classPack.addClassDef("@Setter");
			classPack.addClassDef("@Component");
			classPack.addClassDef("@Scope(value = \"prototype\" )");
			classPack.makeImportString("Getter");
			classPack.makeImportString("Setter");
			classPack.makeImportString("Component");
			classPack.makeImportString("Scope");
	
			if ( extendClass) {
				classPack.addClassDef("public class " + DtoName + " extends GenericDTOImpl ");
				classPack.makeImportString("GenericDTOImpl");
			}
			else {
				classPack.addClassDef("public class " + DtoName +" implements GenericDTO ");
				classPack.makeImportString("GenericDTO");
			}
			//column key columns define
			mekeColumns(keyColumnInfos,classPack);
			//column columns define
			mekeColumns(columnInfos,classPack);
			//Footer
			classPack.addMethodDef("	public String ToString() {");
			classPack.addMethodDef("		return this.toString() + super.toString();");
			classPack.addMethodDef("	}\r\n");
			classPack.addMethodDef("	@Override");
			classPack.addMethodDef("	public Object clone() throws CloneNotSupportedException {" );
			classPack.addMethodDef("		return super.clone();");
			classPack.addMethodDef("	}\r\n") ;
			classPack.addMethodDef("	@Override");
			classPack.addMethodDef("	public boolean isValid() {" );
			classPack.addMethodDef("		return false;");
			classPack.addMethodDef("	}\r\n") ;
			
			writeStream(bufferWriter, classPack);
			bufferWriter.flush();
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void mekeColumns (List<ColumnInfo> columnInfos, ClassPack classPack) {
		for ( ColumnInfo columnInfo:columnInfos)  {			
			String columnName ="";
			String columnType ="";
			try {
		
				columnName = columnInfo.getColumnName();
				columnType = columnInfo.getTypeName().toUpperCase();
				ColumnTypeEnum dbClumnType= ColumnTypeEnum.valueOf(columnType);
				classPack.addAttrDef("	private " + dbClumnType.getJavaType() + " " + convertCamel(columnName) + ";\r\n");
				classPack.makeImportString( dbClumnType.getJavaType());
			}	catch (Exception e ) {
				logger.info("ColumnName:{} columnType:{}", columnName, columnType);
				throw e;
			}
		}		

	}
}
