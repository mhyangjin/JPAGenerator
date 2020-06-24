package com.udmtek.DBCoreGen.Comm;

public enum FileTypeEnum {
	DAO("DAO"),
	Info("Info"),
	Impl("Impl"),
	InfoImpl("InfoImpl"),;
	private String typeString;
	
	FileTypeEnum (String typeString) {
		this.typeString = typeString;
	}
	
	public String getTypeString() {
		return typeString;
	}
}
