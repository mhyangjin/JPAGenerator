package com.udmtek.DBCoreGen.DBconn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.udmtek.DBCoreGen.Comm.DBCoreGenLogger;

public class TableInfo{
	String TableName;
	List<ColumnInfo> ColumnInfolist;
	List<ColumnInfo> keyColumns=null;
	
	public TableInfo (String tableName,List<ColumnInfo> ColumnInfolist) {
		this.TableName = tableName;
		this.ColumnInfolist = ColumnInfolist;
	}
	
	public String getTableName() {
		return TableName;
	}

	public List<ColumnInfo> getColumns() {
		List<ColumnInfo> Columns = new ArrayList<>();
		for ( ColumnInfo columnInfo : ColumnInfolist) {
			if ( columnInfo.getConstraintName().equals("N") ) {
				Columns.add(columnInfo);
			}
		}
		return Columns;
	}

	public List<ColumnInfo> getKeyColumns() {
		if ( keyColumns != null ) return keyColumns;
		List<ColumnInfo> keyColumns = new ArrayList<>();
		for ( ColumnInfo columnInfo : ColumnInfolist) {
			if ( ! columnInfo.getConstraintName().equals("N") ) {
				keyColumns.add(columnInfo);
			}
		}
		return keyColumns;
	}

	public List<ColumnInfo> getAllColumns() {
		return ColumnInfolist;
	}

}
