/**
 * 
 */
package com.udmtek.DBCoreGen.DBconn;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.ColumnResult;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;

import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;

import org.springframework.stereotype.Component;

import com.udmtek.DBCoreGen.Comm.DBCoreGenLogger;

/**
 * @author julu1
 *
 */
public class DBConnector{
	EntityManagerFactory entityManagerFactory;
	public DBConnector () {
		entityManagerFactory= Persistence.createEntityManagerFactory("default");
	}

	@SuppressWarnings("unchecked")
	public List<String> getTableList(String schemaname ) {
		EntityManager entityManager= entityManagerFactory.createEntityManager();
		String getTableSql="SELECT TABLENAME FROM PG_TABLES WHERE SCHEMANAME='" + schemaname 
							+"' AND TABLENAME NOT LIKE '%temp%'";
//		String getTableSql="SELECT TABLENAME FROM PG_TABLES WHERE SCHEMANAME='" + schemaname 
//		+"' AND TABLENAME ='factory'";
		Query query=entityManager.createNativeQuery(getTableSql);
		List<String> tables=(List<String>)query.getResultList();
		entityManager.close();
		return tables;
	}

	public List <ColumnInfo> getColumns(String dbName, String tableName) {
		
		EntityManager entityManager= entityManagerFactory.createEntityManager();
		String getTableSql="\n" +
                            "SELECT	CL.COLUMN_NAME                AS columnName, \n" + 
							"       CL.UDT_NAME                   AS typeName, \n"   + 
							"       CL.IS_NULLABLE                AS isNullable, \n" + 
							"     	COALESCE(CL.CHARACTER_MAXIMUM_LENGTH,0)	  AS maxLen, \n"     + 
							"       COALESCE(TC.CONSTRAINT_NAME,'N')          AS constraintName \n" + 
							"FROM INFORMATION_SCHEMA.COLUMNS CL\n"                      + 
							"     LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE TC \n"  + 
							"     ON CL.TABLE_NAME  = TC.TABLE_NAME \n"                 + 
							"     AND CL.COLUMN_NAME= TC.COLUMN_NAME \n"                + 
							"WHERE CL.TABLE_CATALOG	= '" + dbName + "' \n"              +
							"AND CL.TABLE_NAME      = '" + tableName + "' \n"           +
							"ORDER BY CL.ORDINAL_POSITION; ";

		Query query=entityManager.createNativeQuery(getTableSql);
//		JpaResultMapper jpaResultMapper = new JpaResultMapper();
//		List <ColumnInfoImpl> columns = jpaResultMapper.list(query, ColumnInfoImpl.class);
		List <ColumnInfo> columns = new ArrayList<ColumnInfo>();
		List<Object[]> results = query.getResultList();
		for ( Object[] result:results) {
//			DBCoreGenLogger.printInfo("ColumnName:" + String.valueOf(result[0]));
//			DBCoreGenLogger.printInfo("typeName:" + String.valueOf(result[1]));
//			DBCoreGenLogger.printInfo("isNullable:" + String.valueOf(result[2]));
//			DBCoreGenLogger.printInfo("maxLen:" + String.valueOf(result[3]));
//			DBCoreGenLogger.printInfo("constraintName:" + String.valueOf(result[4]));
//
			ColumnInfo column= new ColumnInfo(String.valueOf(result[0]),
										String.valueOf(result[1]),
										String.valueOf(result[2]),
										Integer.parseInt(String.valueOf(result[3])),
										String.valueOf(result[4]));
			columns.add(column);
		}
//		DBCoreGenLogger.printInfo("COLUMNS:" + columns.size());

		entityManager.close();
		return columns;
	}	
}
