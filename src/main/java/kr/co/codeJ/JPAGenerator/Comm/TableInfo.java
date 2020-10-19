package kr.co.codeJ.JPAGenerator.Comm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			if ( columnInfo.getIsPkey().equals("N") ) {
				Columns.add(columnInfo);
			}
		}
		return Columns;
	}

	public List<ColumnInfo> getKeyColumns() {
		if ( keyColumns != null ) return keyColumns;
		List<ColumnInfo> keyColumns = new ArrayList<>();
		for ( ColumnInfo columnInfo : ColumnInfolist) {
			if (columnInfo.getIsPkey().equals("Y") ) {
				keyColumns.add(columnInfo);
			}
		}
		return keyColumns;
	}

	public List<ColumnInfo> getAllColumns() {
		return ColumnInfolist;
	}
	
	public List<ColumnInfo> getExceptCreateUpdateInfo() {
		List<ColumnInfo> Columns = new ArrayList<>();
		for ( ColumnInfo columnInfo : ColumnInfolist) {
			if ( !columnInfo.getColumnName().equals("creator_id") &&
			     !columnInfo.getColumnName().equals("create_date_time") &&
			     !columnInfo.getColumnName().equals("updator_id") &&
			     !columnInfo.getColumnName().equals("update_date_time") &&
			     columnInfo.getIsPkey().equals("N"))
				Columns.add(columnInfo);
		}
		return Columns;
	}

}
