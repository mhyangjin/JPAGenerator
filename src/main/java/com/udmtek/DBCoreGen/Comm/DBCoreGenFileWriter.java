package com.udmtek.DBCoreGen.Comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class DBCoreGenFileWriter {
	static final String commentString="/***\r\n" + 
			"* @author julu1 <julu1 @ naver.com >\r\n" + 
			"* @version 0.1.0\r\n" +
			"* generated by DBCoreGen\r\n" +
			"*/\r\n";


	public static void writeStream ( BufferedWriter bufferwriter, StringBuffer stringbuf ) throws IOException {
		bufferwriter.write(stringbuf.toString());
		bufferwriter.newLine();
	}
	
	public static void writeStream ( BufferedWriter bufferwriter, String msg ) throws IOException {
		bufferwriter.write(msg);
		bufferwriter.newLine();
	}
	public static void writeStream ( BufferedWriter bufferwriter, String[] msgs ) throws IOException {
		for ( String tmpmsg : msgs) {
			bufferwriter.write(tmpmsg);
			bufferwriter.newLine();
		}
	}

	public static void writeStream ( BufferedWriter bufferwriter, String[][] msgs ) throws IOException {
		for ( String[] tmpmsgs : msgs) {
			for (String tmpmsg : tmpmsgs) {
				bufferwriter.write(tmpmsg);
				bufferwriter.newLine();
			}
			bufferwriter.newLine();
		}
	}
	
	public static void writeStream ( BufferedWriter bufferwriter, List<String> msgs ) throws IOException {
		for ( String tmpmsg : msgs) {
			bufferwriter.write(tmpmsg);
			bufferwriter.newLine();
		}
	}
	
	public static File makeFile(String fileName ) {	
		File writeFile= new File (fileName);
		return writeFile;
	}
	
	public static String convertCamel(String orgString) {
		StringBuffer orgStringBuf=new StringBuffer(orgString.toLowerCase());
		while (true) {
			int point =0;
			if ((point=orgStringBuf.indexOf("_") )== -1)
				break;
			orgStringBuf.deleteCharAt(point);
			orgStringBuf.setCharAt(point, String.valueOf(orgStringBuf.charAt(point)).toUpperCase().charAt(0));
		}
		
		return orgStringBuf.toString();
	}
	

}
