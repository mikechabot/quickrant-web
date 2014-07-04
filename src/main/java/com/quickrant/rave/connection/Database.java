package com.quickrant.rave.connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.javalite.activejdbc.Base;
import org.postgresql.ds.PGPoolingDataSource;

public class Database {
	
	private static Logger log = Logger.getLogger(Database.class);
		
	private static DataSource dataSource;
	
	static {
		
		try {	
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext ic = new InitialContext();
            ic.createSubcontext("jdbc");
            
            dataSource = new PGPoolingDataSource();
            ((PGPoolingDataSource) dataSource).setDataSourceName("quickrant-ds");
            ((PGPoolingDataSource) dataSource).setServerName("localhost");
            ((PGPoolingDataSource) dataSource).setDatabaseName("quickrant");
            ((PGPoolingDataSource) dataSource).setUser("quickrant");
            ((PGPoolingDataSource) dataSource).setPassword("s00perhax0r!");
            ((PGPoolingDataSource) dataSource).setMaxConnections(50);
			
			ic.bind("jdbc/datasource", dataSource);
		}
		catch (Exception e) {
			log.error("Unable to initalize datasource", e);
		}
	} 
	
	public Database() {}
	
	public void open() throws SQLException {
		Base.open("jdbc/datasource");
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	
	public Statement getStatement() throws SQLException {
		return Base.connection().createStatement();
	}
	
	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		return Base.connection().prepareStatement(sql);
	}
	
	public void close() {
		Base.close();
	}
	
}