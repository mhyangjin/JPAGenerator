package kr.co.codeJ.JPAGenerator.Comm;

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
