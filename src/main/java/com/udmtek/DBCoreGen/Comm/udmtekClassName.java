package com.udmtek.DBCoreGen.Comm;

public enum udmtekClassName {
	ENTITY("<CLASSNAME>"),
	DTO("<CLASSNAME>DTO"),
	DAO("<CLASSNAME>DTO"),
	MAPPER("<CLASSNAME>MAPPER"),;
	
	private String className;
	udmtekClassName(String className) {
		this.className =  className;
	}
}
