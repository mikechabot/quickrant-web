package com.quickrant.rave.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.javalite.activejdbc.Base;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.quickrant.rave.Configuration;

public class Database {

	private static Logger log = Logger.getLogger(Database.class);

	private static BoneCPDataSource dataSource;
	
	private Timer timer;

	static {
		try {
			/* Load the driver */
			Class.forName("org.postgresql.Driver");

			/* Obtain values from the configuration */
			Configuration config = Configuration.getInstance();
			String username = config.getRequiredString("postgres-username");
			String password = config.getRequiredString("postgres-password");
			String hostname = config.getRequiredString("postgres-hostname");
			String database = config.getRequiredString("postgres-database");

			/* Pool defaults: 5 thread pools with a max of 50 threads each */
			int partitions = config.getOptionalInt("bonecp-partitions", 5);
			int min = config.getOptionalInt("bonecp-min-pool-size", 5);
			int max = config.getOptionalInt("bonecp-max-pool-size", 50);

			/* Initialize the BoneCP connection pool */
			BoneCPConfig boneConfig = new BoneCPConfig();
			boneConfig.setJdbcUrl("jdbc:postgresql://" + hostname + "/"	+ database);
			boneConfig.setUsername(username);
			boneConfig.setPassword(password);
			boneConfig.setPartitionCount(partitions);
			boneConfig.setMinConnectionsPerPartition(min);
			boneConfig.setMaxConnectionsPerPartition(max);

			/* Create the connection pool */
			dataSource = new BoneCPDataSource(boneConfig);
			
		} catch (ClassNotFoundException e) {
			log.error("Driver not found", e);
		} catch (Exception e) {
			log.error("Unable to initalize datasource", e);
		}
	}

	public Database() { }
	
	public void initialize() throws SQLException {
		verifyDatabaseConnectivity();
		startStatisticsJob();
	}
	
	public void startStatisticsJob() {
		timer = new Timer();
        timer.schedule(new RunStatistics(), 10000, 10000);
	}

	public void open() throws SQLException {
		Base.open(dataSource);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public BoneCP getPool() {
		return dataSource.getPool();
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

	public void verifyDatabaseConnectivity() throws SQLException {
		Database database = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			database = new Database();
			database.open();
			preparedStatement = database.getPreparedStatement("select version()");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) log.info("Database reached: " + resultSet.getString(1));
		} finally {
			DatabaseUtil.close(resultSet);
			DatabaseUtil.close(preparedStatement);
			DatabaseUtil.close(database);
		}
	}

	private class RunStatistics extends TimerTask {
		@Override
		public void run() {
			DatabaseStats stats = new DatabaseStats(getPool());
			stats.printStats();
		}
	}

}