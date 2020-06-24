package com.udmtek.DBCoreGen.DBconn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnInfo {

	String columnName;
	String typeName;
	String isNullable;
	Integer maxLen;
	String constraintName;
	
	public ColumnInfo (	String columnName,
							String typeName,
							String isNullable,
							Integer maxLen,
							String constraintName ) {
		this.columnName=columnName;
		this.typeName = typeName;
		this.isNullable = isNullable;
		this.maxLen = maxLen;
		this.constraintName =constraintName;
	}

}
