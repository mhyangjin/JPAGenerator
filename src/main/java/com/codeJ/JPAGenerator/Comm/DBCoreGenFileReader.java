package com.codeJ.JPAGenerator.Comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class DBCoreGenFileReader {
	private  final String properyFile="./src/main/resources/DBInfo.properties";
	@Getter
	private String outputMode;
	@Getter
	private String dbName;
	@Getter
	private String extractPath;
	@Getter
	private String packageName;
	@Getter
	private List<String> tables;
	@Getter
	private List<String> ignoreTables;
	@Getter
	private boolean tableJoin;
	@Getter
	private String schemaName;	
	private File configFile;

	
	public enum ContentsIndex {
		OUTPUTMODE("ouputMode"),
		TABLES("tables"),
		DBNAME("dbName"),
		EXTRACTPATH("extractPath"),
		PACKAGENAME("package"),
		IGNORETABLES("ignoreTables"),
		SCHEMANAME("schemaName"),
		TABLEJOIN("tableJoin");
		private String indexString;
		ContentsIndex(String indexString) {
			this.indexString = indexString;
		}
		public String getIndexString() {
			return indexString;
		}
	}

	public DBCoreGenFileReader() {
		tables=new ArrayList<String>();
		ignoreTables=new ArrayList<String>();
	}
	public  boolean readConfigFile() {
		configFile = new File (properyFile);
		boolean fileExist=configFile.exists();
		if (fileExist) {
			loadConfig();
		}
		return fileExist;
	}
	
	public   void loadConfig() {
		DBCoreGenLogger.printInfo("readFile");
		
		BufferedReader bufferReader=null;
		FileReader fileReader=null;
		String line="";
		try {
			fileReader = new FileReader(configFile);
			bufferReader = new BufferedReader(fileReader);
			ContentsIndex contentsIndex=null;	
			while ((line = bufferReader.readLine()) != null) {			
				if ( line.indexOf("dbName") > -1 ) {
					contentsIndex = ContentsIndex.DBNAME;
				}
				if ( line.indexOf("extractPath") > -1 ) {
					contentsIndex = ContentsIndex.EXTRACTPATH;
				}
				if ( line.indexOf("package") > -1 ) {
					contentsIndex = ContentsIndex.PACKAGENAME;
				}

				if ( line.indexOf("tables") > -1 ) {
					contentsIndex = ContentsIndex.TABLES;
				}
				if ( line.indexOf("ouputMode") > -1 ) {
					contentsIndex = ContentsIndex.OUTPUTMODE;
				}
				if ( line.indexOf("ignoreTables") > -1 ) {
					contentsIndex = ContentsIndex.IGNORETABLES;
				}
				if ( line.indexOf("tableJoin") > -1 ) {
					contentsIndex = ContentsIndex.TABLEJOIN;
				}
				if ( line.indexOf("schemaName") > -1 ) {
					contentsIndex = ContentsIndex.SCHEMANAME;
				}
				String temp="";
				switch (contentsIndex) {
				case OUTPUTMODE:
					outputMode=line.substring(line.indexOf("=")+1);
					DBCoreGenLogger.printInfo("OUTPUTMODE:" + outputMode);
					break;
				case DBNAME:
					dbName=line.substring(line.indexOf("=")+1);
					DBCoreGenLogger.printInfo("CONFIG:DBNAME:" + dbName);
					break;
				case EXTRACTPATH:
					extractPath=line.substring(line.indexOf("=")+1);
					DBCoreGenLogger.printInfo("extractPath:" + extractPath);
					break;
				case PACKAGENAME:
					packageName=line.substring(line.indexOf("=")+1);
					DBCoreGenLogger.printInfo("packageName:" + packageName);
					break;
				case TABLES:
					temp=line.substring(line.indexOf("=")+1);
					if ( temp.isEmpty())
						DBCoreGenLogger.printInfo("table is empty:");
					else
						tables = Arrays.asList(temp.split(","));
					DBCoreGenLogger.printInfo("tables:" + tables);
					break;
				case IGNORETABLES:
					temp=line.substring(line.indexOf("=")+1);
					if ( temp.isEmpty())
						DBCoreGenLogger.printInfo("ignore table is empty:");
					else
						ignoreTables = Arrays.asList(temp.split(","));
					DBCoreGenLogger.printInfo("ignore tables:" + ignoreTables);
				case TABLEJOIN:
					String tableJoinString=line.substring(line.indexOf("=")+1);
					if ( tableJoinString.contentEquals("Y"))
						tableJoin=true;
					else
						tableJoin=false;
					DBCoreGenLogger.printInfo("tableJoin:" + tableJoinString);
					break;
				case SCHEMANAME:
					schemaName=line.substring(line.indexOf("=")+1);
					DBCoreGenLogger.printInfo("schemaName:" + schemaName);
					break;
				}
			}	
			bufferReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		DBCoreGenLogger.printInfo("Buffer:" + tables.size());

	}	
}
