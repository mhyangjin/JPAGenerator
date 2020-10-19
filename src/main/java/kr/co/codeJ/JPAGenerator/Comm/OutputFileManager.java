package kr.co.codeJ.JPAGenerator.Comm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.codeJ.JPAGenerator.Writer.DAOWriter;
import kr.co.codeJ.JPAGenerator.Writer.DTOWriter;
import kr.co.codeJ.JPAGenerator.Writer.EntityWriter;
import kr.co.codeJ.JPAGenerator.Writer.FileWriter;
import kr.co.codeJ.JPAGenerator.Writer.MapperWriter;

public class OutputFileManager {
	private static Logger logger = LoggerFactory.getLogger(OutputFileManager.class);
	String outputMode;
	
	Map <String,String> classNameTailMap;
	PropertyFileReader configReader;
	
	public OutputFileManager( PropertyFileReader configReader) {
		this.outputMode = configReader.getOutputMode();
		this.configReader=configReader;
		classNameTailMap=new HashMap<>();

		classNameTailMap.put("ENTITY","" );
		classNameTailMap.put("DAO","DAO" );
		classNameTailMap.put("DTO","DTO" );
		classNameTailMap.put("MAPPER","Mapper" );
		classNameTailMap.put("INF","NONE" );
	}
	
	public void makeFile(TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= FileWriter.convertCamel(tableName);
		String path=configReader.getExtractPath() + "//" + className;
		String packName = configReader.getPackageName() + "." + className;
		if (!makeDir (path) ) return;

//		DBCoreGenLogger.printInfo(tableInfo.getTableName() + ":"+tableInfo.containsCreateOrUpdateInfo());
//		if ( )
		EntityWriter.generateFile(configReader, tableInfo,classNameTailMap);
		DTOWriter.generateFile(path, packName, tableInfo,classNameTailMap);
		DAOWriter.generateFile(path, packName, tableInfo,classNameTailMap);
		MapperWriter.generateFile(path, packName, tableInfo,classNameTailMap);
	}
	
	public boolean makeDir(String Path) {
		File dir = new File (Path);
		boolean dirOK = dir.canExecute() ;
		if ( !dirOK) {
			dirOK=dir.mkdir();
		}	
		if (!dirOK ) 
			logger.info("makeDir failure :{}",Path);
		return dirOK;
	}
}
