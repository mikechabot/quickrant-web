package com.quickrant.rave.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.javalite.activejdbc.Base;
import org.postgresql.ds.PGPoolingDataSource;

import com.quickrant.rave.Configuration;

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
            
            Configuration conf = Configuration.getInstance();
            ((PGPoolingDataSource) dataSource).setDataSourceName("quickrant-ds");
            ((PGPoolingDataSource) dataSource).setServerName(conf.getRequiredString("postgres-host"));
            ((PGPoolingDataSource) dataSource).setDatabaseName(conf.getRequiredString("postgres-name"));
            ((PGPoolingDataSource) dataSource).setUser(conf.getRequiredString("postgres-username"));
            ((PGPoolingDataSource) dataSource).setPassword(conf.getRequiredString("postgres-password"));
            ((PGPoolingDataSource) dataSource).setMaxConnections(conf.getOptionalInt("postgres-max-connections", 50));
			
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
	
	public static void verifyDatabaseConnectivity() throws SQLException {
		Database database = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			database = new Database();
			database.open();
			preparedStatement = database.getPreparedStatement("select version()");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				log.info("Database reached: " + resultSet.getString(1));
			}
		} finally {
			DatabaseUtils.close(resultSet);
			DatabaseUtils.close(preparedStatement);
			DatabaseUtils.close(database);
		}
	}
	
}