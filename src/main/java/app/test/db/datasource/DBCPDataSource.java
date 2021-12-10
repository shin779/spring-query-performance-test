package app.test.db.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {

	private static BasicDataSource ds = new BasicDataSource();

	static {
		Properties properties = new Properties();
		try {
			properties.load(DBCPDataSource.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		ds.setUrl(properties.getProperty("dataSource.url"));
		ds.setUsername(properties.getProperty("dataSource.username"));
		ds.setPassword(properties.getProperty("dataSource.password"));
		ds.setPoolPreparedStatements(Boolean.valueOf(properties.getProperty("dataSource.poolPreparedStatements")));
		ds.setDriverClassName(properties.getProperty("dataSource.driverClassName"));
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}