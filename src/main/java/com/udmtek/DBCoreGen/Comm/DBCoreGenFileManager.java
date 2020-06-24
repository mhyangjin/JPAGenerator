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
		DBCoreGenDAOWriter.generateFile(modelPATH + "\\DAO", packageName, tableInfo);
		DBCoreGenInfoWriter.generateFile(modelPATH + "\\Info", packageName, tableInfo);
		DBCoreGenImplWriter.generateFile(modelPATH + "\\Impl", packageName, tableInfo);
		DBCoreGenInfoImplWriter.generateFile(modelPATH + "\\InfoImpl", packageName, tableInfo);
	}
	
	public boolean makeDir() {
		File dir = new File (modelPATH);
		boolean dirOK = dir.canExecute() ;
		if ( !dirOK) {
			dirOK=dir.mkdir();
		}
		
		dir= new File(modelPATH + "\\DAO");
		if (! dir.canExecute())
			dir.mkdir();
		dir= new File(modelPATH + "\\Info");
		if (! dir.canExecute())
			dir.mkdir();
		dir= new File(modelPATH + "\\Impl");
		if (! dir.canExecute())
			dir.mkdir();
		dir= new File(modelPATH + "\\InfoImpl");
		if (! dir.canExecute())
			dir.mkdir();
		
		return dirOK;
	}
}
