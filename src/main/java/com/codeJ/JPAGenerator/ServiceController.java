package com.codeJ.JPAGenerator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeJ.JPAGenerator.Comm.ColumnInfo;
import com.codeJ.JPAGenerator.Comm.OutputFileManager;
import com.codeJ.JPAGenerator.Comm.PropertyFileReader;
import com.codeJ.JPAGenerator.Comm.TableInfo;
import com.codeJ.JPAGenerator.DBconn.DBConnector;
import com.codeJ.JPAGenerator.DBconn.PostgresQuery;
import com.codeJ.JPAGenerator.DBconn.QueryMaker;


public class ServiceController {
	private static Logger logger = LoggerFactory.getLogger(ServiceController.class);
	PropertyFileReader configReader;
	DBConnector dbcon;
	List<TableInfo> tableInfos;

	public ServiceController() {
		QueryMaker queryMaker=new PostgresQuery();
		dbcon = new DBConnector(queryMaker);
		tableInfos=new ArrayList<>();	
	}
	
	public boolean readconfig() {
		configReader= new PropertyFileReader();
		boolean cofigFileOK = configReader.readConfigFile();
		return cofigFileOK;
	}
	public void serviceProcess() {
		List<String> getTables=configReader.getTables();
		List<String> ignoreTables=configReader.getIgnoreTables();
		String modelPath=configReader.getExtractPath();
		
		logger.info("serviceProcess: {}",getTables.size());
		if ( getTables.size() == 0)
				getTables= getTables();
		
		OutputFileManager filewriter= new OutputFileManager(configReader);
		if (! filewriter.makeDir(modelPath) ) {
			logger.error("Making dir if fail!! - {}",modelPath);
			return;
		}
		for ( int i=0; i< getTables.size(); i ++) {
			//ignore table에 있는 table 은 생략
			if (ignoreTables.contains(getTables.get(i))) continue;
			
			List<ColumnInfo>  columns = getColumns(getTables.get(i));
			logger.info("ColumnInfos size:{}",columns.size());
			if ( columns != null) {
				TableInfo tableInfo = new TableInfo(getTables.get(i), columns);
				if ( tableInfo.getKeyColumns().size() > 0 )
					tableInfos.add(tableInfo);
				
			}
			
		}
//		for ( String ignoreTable:ignoreTables)
//		logger.info("ignoreTables size:{}",ignoreTable);
		logger.info("tableInfos size:{}",tableInfos.size());
		for ( int i=0; i< tableInfos.size(); i ++) {
			logger.info("Table makeFile:{}", tableInfos.get(i).getTableName());
			filewriter.makeFile(tableInfos.get(i));
		}
		logger.info("Table Info gen complete! {}", tableInfos.size());
	}
	

	public List <String>  getTables() {
		String schemaName=configReader.getSchemaName();
		List <String>  tables = null;
		try {
			tables=dbcon.getTableList(schemaName);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tables;
	}
	

	public List<ColumnInfo> getColumns(String TableName) {
		String dbName=configReader.getDbName();
		
		List<ColumnInfo> columns = null;
		try {
			columns = dbcon.getColumns(dbName, TableName);
			logger.info("columns size:{}",columns.size());
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return columns;
	}
}
