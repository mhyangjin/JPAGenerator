package com.udmtek.DBCoreGen.Comm;

import java.io.File;
import com.udmtek.DBCoreGen.DBconn.TableInfo;

public class DBCoreGenFileManager {
	String modelPATH;
	String packageName;
	
	public DBCoreGenFileManager( String modelPATH, String packageName) {
		this.modelPATH = modelPATH;
		this.packageName= packageName;
	}
	
	public void makeFile(TableInfo tableInfo) {
		String tableName= tableInfo.getTableName();
		String className= DBCoreGenFileWriter.convertCamel(tableName);
		String path=modelPATH + "//" + className;
		String packName = packageName + "." + className;
		if (!makeDir (path) ) return;
		DBCoreGenEntityWriter.generateFile(path,packName, tableInfo);
		DBCoreGenDTOWriter.generateFile(path, packName, tableInfo);
		DBCoreGenDAOWriter.generateFile(path, packName, tableInfo);
		DBCoreGenMapperWriter.generateFile(path, packName, tableInfo);
	}
	
	public boolean makeDir(String Path) {
		File dir = new File (Path);
		boolean dirOK = dir.canExecute() ;
		if ( !dirOK) {
			dirOK=dir.mkdir();
		}	
		return dirOK;
	}
}
