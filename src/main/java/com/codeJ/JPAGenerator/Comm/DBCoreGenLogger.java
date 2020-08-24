package com.codeJ.JPAGenerator.Comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author julu1 <julu1 @ naver.com >
 * @version 0.1.0
 */
public class DBCoreGenLogger {
	private static Logger logger=LoggerFactory.getLogger(DBCoreGenLogger.class);
	
	/**
	 * print info.
	 */
	public static void printInfo(String msg) {
		logger.info(msg);
	}
	
	/**
	 *print error
	 */
	public static void printError(String msg) {
		logger.error(msg);
	}
}
