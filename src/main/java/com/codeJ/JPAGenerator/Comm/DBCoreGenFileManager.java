package com.codeJ.JPAGenerator.Comm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.codeJ.JPAGenerator.Writer.DBCoreGenDAOInfWriter;
import com.codeJ.JPAGenerator.Writer.DBCoreGenDAOWriter;
import com.codeJ.JPAGenerator.Writer.DBCoreGenDTOWriter;
import com.codeJ.JPAGenerator.Writer.DBCoreGenEntityWriter;
import com.codeJ.JPAGenerator.Writer.DBCoreGenFileWriter;
import com.codeJ.JPAGenerator.Writer.DBCoreGenMapperWriter;

public class DBCoreGenFileManager {
	String outputMode;
	
	Map <String,String> classNameTailMap;
	DBCoreGenFileReader configReader;
	
	public DBCoreGenFileManager( DBCoreGenFileReader configReader) {
		this.outputMode = configReader.getOutputMode();
		this.configReader=configReader;
		classNameTailMap=new HashMap<>();
		
		/**
		 * udmtek 심재산 수석의 요청으로 변경되는 JPA repository class Name.
		 * Entity Class ==> EntityName + "DAO"
		 * DB access를 담당하는 DAO Class ==> EntityName + "Impl"
		 * persistence context에서 분리하기 위한 Entity에 매칭 되는 DTO ==> EntityName + "Info"
		 * DAO에 사용자 정의 interface를 추가할 수 있는 빈 Interface를 정의하여 DAO에 Implements 추가
		 */
		if (outputMode.contains("udmtek")) {
			classNameTailMap.put("ENTITY","DAO" );
			classNameTailMap.put("DAO","Impl" );
			classNameTailMap.put("DTO","Info" );
			classNameTailMap.put("MAPPER","Mapper" );
			classNameTailMap.put("INF","Interface" );
		}
		else {
			classNameTailMap.put("ENTITY","" );
			classNameTailMap.put("DAO","DAO" );
			classNameTailMap.put("DTO","DTO" );
			classNameTailMap.put("MAPPER","Mapper" );
			classNameTailMap.put("INF","NONE" );
		}
	}
	
	public void makeFile(TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= DBCoreGenFileWriter.convertCamel(tableName);
		String path=configReader.getExtractPath() + "//" + className;
		String packName = configReader.getPackageName() + "." + className;
		if (!makeDir (path) ) return;

//		DBCoreGenLogger.printInfo(tableInfo.getTableName() + ":"+tableInfo.containsCreateOrUpdateInfo());
//		if ( )
		DBCoreGenEntityWriter.generateFile(configReader, tableInfo,classNameTailMap);
		DBCoreGenDTOWriter.generateFile(path, packName, tableInfo,classNameTailMap);
		DBCoreGenDAOWriter.generateFile(path, packName, tableInfo,classNameTailMap);
		DBCoreGenMapperWriter.generateFile(path, packName, tableInfo,classNameTailMap);
		if (outputMode.contains("udmtek")) {
			DBCoreGenDAOInfWriter.generateFile(path, packName, tableInfo,classNameTailMap);
		}
	}
	
	public boolean makeDir(String Path) {
		File dir = new File (Path);
		boolean dirOK = dir.canExecute() ;
		if ( !dirOK) {
			dirOK=dir.mkdir();
		}	
		if (!dirOK ) 
			DBCoreGenLogger.printInfo("makeDir failure :"+Path);
		return dirOK;
	}
}
