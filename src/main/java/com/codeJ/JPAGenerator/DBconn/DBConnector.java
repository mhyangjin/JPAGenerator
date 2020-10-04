/**
 * 
 */
package com.codeJ.JPAGenerator.DBconn;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.ColumnResult;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.qlrm.mapper.JpaResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;

import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;

import org.springframework.stereotype.Component;

import com.codeJ.JPAGenerator.Comm.ColumnInfo;

/**
 * @author julu1
 *
 */
public class DBConnector{
	private static Logger logger = LoggerFactory.getLogger(DBConnector.class);
	
	EntityManagerFactory entityManagerFactory;
	QueryMaker	queryMaker;
	public DBConnector (	QueryMaker	queryMaker) {
		this.queryMaker = queryMaker;
		entityManagerFactory= Persistence.createEntityManagerFactory("default");

	}

	@SuppressWarnings("unchecked")
	public List<String> getTableList(String schemaname ) {
		
		EntityManager entityManager= entityManagerFactory.createEntityManager();

		String getTableSql=queryMaker.getTableListQuery(schemaname);
		Query query=entityManager.createNativeQuery(getTableSql);
		List<String> tables=(List<String>)query.getResultList();
		entityManager.close();
		return tables;
	}

	public List <ColumnInfo> getColumns(String dbName, String tableName) {
		
		EntityManager entityManager= entityManagerFactory.createEntityManager();
		String getTableSql=queryMaker.getColumnQuery(dbName, tableName);

		Query query=entityManager.createNativeQuery(getTableSql);
		List <ColumnInfo> columns = new ArrayList<ColumnInfo>();
		List<Object[]> results = query.getResultList();
		for ( Object[] result:results) {
//			logger.trace("COLUMN:" +String.valueOf(result[0]));
			ColumnInfo column= new ColumnInfo(String.valueOf(result[0]),
										String.valueOf(result[1]),
										String.valueOf(result[2]),
										Integer.parseInt(String.valueOf(result[3])),
										String.valueOf(result[4]),
										String.valueOf(result[5]));

			columns.add(column);
		}
		
		for ( ColumnInfo column:columns) {
			//PK가 아니면서 constraintName이 있는 칼럼은 fkey로 판단. 관련 table 검색
			if ( column.getIsPkey().contentEquals("N") & !column.getConstraintName().contentEquals("N"))
			{
				String constraintTableString=queryMaker.getConstraintTablename(column.getConstraintName());
				Query  constraintQuery=entityManager.createNativeQuery(constraintTableString);
				List<Object>  constraintResults =  constraintQuery.getResultList();
				for ( Object constraintResult:constraintResults) {
					column.setConstsraintTable(String.valueOf( constraintResult));
				}
			}
		}
		entityManager.close();
		return columns;
	}	
}
