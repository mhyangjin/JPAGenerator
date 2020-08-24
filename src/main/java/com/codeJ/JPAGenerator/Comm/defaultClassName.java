package com.codeJ.JPAGenerator.Comm;

public enum defaultClassName {
	ENTITY("<CLASSNAME>"),
	DTO("<CLASSNAME>DTO"),
	DAO("<CLASSNAME>DTO"),
	MAPPER("<CLASSNAME>MAPPER"),;
	
	private String className;
	defaultClassName(String className) {
		this.className =  className;
	}
}
