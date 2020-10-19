package kr.co.codeJ.JPAGenerator.Comm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnInfo {

	String columnName;
	String typeName;
	String isNullable;
	Integer maxLen;
	String isPkey;
	String constraintName;
	String constsraintTable;
	
	public ColumnInfo (	String columnName,
							String typeName,
							String isNullable,
							Integer maxLen,
							String isPkey,
							String constraintName ) {
		this.columnName=columnName;
		this.typeName = typeName;
		this.isNullable = isNullable;
		this.maxLen = maxLen;
		this.isPkey = isPkey;
		this.constraintName =constraintName;
	}

	public boolean isEqual(String columnName) {
		return this.columnName.equals(columnName);
	}
}
