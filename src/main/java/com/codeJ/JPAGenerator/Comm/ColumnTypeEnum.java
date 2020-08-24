package com.codeJ.JPAGenerator.Comm;

public enum ColumnTypeEnum {
	VARCHAR("String"),
	TIMESTAMP("Date"),
	TIMESTAMPTZ("Date"),
	INT2("int"),
	TIMETZ("Date"),
	NUMERIC("float"),
	INT4("int"),
	INT8("long"),
	CHAR("char"),
	OID("int"),
	TEXT("String"),
	FLOAT8("double"),
	BOOL("boolean"),
	UUID("int"),
	BYTEA("String"),
	BPCHAR("String");
	
	private String ColumnTypeString;
	
	ColumnTypeEnum(String columntype) {
		this.ColumnTypeString = columntype;
	}
	
	public String getJavaType() {
		return ColumnTypeString;
	}

}
