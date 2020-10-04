package com.codeJ.JPAGenerator.DBconn;

public class PostgresQuery implements QueryMaker {

	@Override
	public String getTableListQuery(String schemaname) {
		StringBuffer getTableSql= new StringBuffer();
		getTableSql.append("SELECT TABLENAME FROM PG_TABLES WHERE SCHEMANAME='" )
					.append( schemaname )
					.append("'");
		return getTableSql.toString();
	}

	@Override
	public String getColumnQuery(String dbName, String tableName) {
		StringBuffer getColumnSql= new StringBuffer();
		getColumnSql.append("SELECT A.columnName,\n"															)
					.append("		A.typeName,\n"																)
					.append("		A.isNullable,\n"															)
					.append("		A.maxLen,\n"																)
					.append("		A.isPkey,\n"																)
					.append("		A.constraintName\n"															)
					.append("FROM ("																			)
					.append("SELECT	CL.COLUMN_NAME               				 AS columnName, \n"				)
					.append("        MAX(CASE WHEN( CL.DATA_TYPE='bigint') THEN CL.DATA_TYPE ELSE CL.UDT_NAME END) 	   AS typeName, \n"					)
					.append("       MAX(CL.IS_NULLABLE )         				    AS isNullable, \n"			)
					.append("		MAX(COALESCE(CL.CHARACTER_MAXIMUM_LENGTH,0))	  AS maxLen, \n" 			) 
					.append("		MAX(CASE WHEN (RIGHT(COALESCE(TC.CONSTRAINT_NAME,'N'),4)='pkey') "			)
					.append("THEN 'Y' ELSE 'N'  END)       AS isPkey  ,\n"										)
					.append("       MAX(COALESCE(TC.CONSTRAINT_NAME,'N'))          AS constraintName  ,\n"		)
					.append("		MAX( CL.ORDINAL_POSITION )						AS ORDINAL_POSITION \n"		)
					.append("	FROM INFORMATION_SCHEMA.COLUMNS CL \n"											)
					.append("		LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE TC \n"						)
					.append("		ON TC.CONSTRAINT_NAME LIKE '%key%'\n"										)
					.append(" 		AND CL.TABLE_NAME  = TC.TABLE_NAME \n"										)
					.append("		AND CL.COLUMN_NAME= TC.COLUMN_NAME \n"        								)
					.append("WHERE	CL.TABLE_CATALOG	= '" + dbName + "' \n"  								)
					.append("		AND CL.TABLE_NAME      = '" + tableName + "' \n" 							)
					.append("GROUP BY CL.COLUMN_NAME ) A  \n"													)
					.append("ORDER BY A.ORDINAL_POSITION; "														);
		return getColumnSql.toString();
	}

	@Override
	public String getConstraintTablename(String constraintName) {
		StringBuffer getConstraintSql= new StringBuffer();
		getConstraintSql.append("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE \r\n")
						.append("WHERE CONSTRAINT_NAME='")
						.append(constraintName)
						.append("'");
		return getConstraintSql.toString();
	}

}
