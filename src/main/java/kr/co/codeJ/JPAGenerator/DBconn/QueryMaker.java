package kr.co.codeJ.JPAGenerator.DBconn;

public interface QueryMaker {
	public String getTableListQuery(String schemaname );
	public String getColumnQuery(String dbName, String tableName);
	public String getConstraintTablename(String constraintName );
}
