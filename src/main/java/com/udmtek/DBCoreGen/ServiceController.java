package com.udmtek.DBCoreGen;

import java.util.ArrayList;
import java.util.List;

import com.udmtek.DBCoreGen.Comm.DBCoreGenLogger;
import com.udmtek.DBCoreGen.Comm.DBCoreGenFileManager;
import com.udmtek.DBCoreGen.Comm.DBCoreGenFileReader;
import com.udmtek.DBCoreGen.DBconn.ColumnInfo;
import com.udmtek.DBCoreGen.DBconn.DBConnector;
import com.udmtek.DBCoreGen.DBconn.TableInfo;


public class ServiceController {
	private String schemaName="mes";
	private String dbName="udmtmesdb";
	private String modelPath;
	private String packageName;
	DBConnector dbcon;
	List<String> getTables;
	List<TableInfo> tableInfos;
	private String runPath;
	
	public ServiceController() {
		dbcon = new DBConnector();
		tableInfos=new ArrayList<>();	
		this.modelPath = runPath;
	}
	
	public boolean readconfig() {
		DBCoreGenFileReader configReader= new DBCoreGenFileReader();
		boolean cofigFileOK = configReader.readConfigFile();
		if (cofigFileOK) {
			dbName= configReader.getDbName();
			modelPath=configReader.getExtractPath();
			packageName= configReader.getPackageName();
			getTables=configReader.getTables();
		}
		else {
			DBCoreGenLogger.printError("DBInfo.properties file is not found!");
		}
		return cofigFileOK;
	}
	public void serviceProcess() {
		DBCoreGenLogger.printInfo("serviceProcess:" + getTables.size());
		if ( getTables.size() == 0)
				getTables= getTables();
		
		DBCoreGenFileManager filewriter= new DBCoreGenFileManager(modelPath,packageName);
		if (! filewriter.makeDir(modelPath) ) {
			DBCoreGenLogger.printError("Making dir if fail!! - " + modelPath);
			return;
		}
		for ( int i=0; i< getTables.size(); i ++) {
			List<ColumnInfo>  columns = getColumns(getTables.get(i));
			if ( columns != null) {
				TableInfo tableInfo = new TableInfo(getTables.get(i), columns);
				if ( tableInfo.getKeyColumns().size() > 0 )
					tableInfos.add(tableInfo);
			}	
		}
		
		for ( int i=0; i< tableInfos.size(); i ++) {
			filewriter.makeFile(tableInfos.get(i));
		}
		DBCoreGenLogger.printInfo("Table Info gen complete!" + tableInfos.size());
	}
	

	public List <String>  getTables() {
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
		
		List<ColumnInfo> columns = null;
		try {
			columns = dbcon.getColumns(dbName, TableName);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return columns;
	}
}
