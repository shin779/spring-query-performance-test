package app.test.db;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import app.test.db.dao.Dao;
import app.test.db.datasource.DBCPDataSource;

public class TestDb {

	private Logger logger = Logger.getLogger(TestDb.class);
	private ApplicationContext applicationContext;
	private Connection conn = null;
	private Dao dao = null;
	private static final Integer LOOP_COUNT = 500;
	private static final Integer TIMES = 10;

	@Before
	public void init() {
		applicationContext = new ClassPathXmlApplicationContext("**/applicationContext-Spring-Jdbc.xml");
		dao = (Dao) applicationContext.getBean("dao");
		assertNotNull(dao);
		try {
			conn = DBCPDataSource.getConnection();
			conn.setAutoCommit(false);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		assertNotNull(conn);
	}
	
	private void refreshConnection() throws Exception{
		try {
			conn.rollback();
			conn.close();
			if (conn.isClosed()) {

				conn = DBCPDataSource.getConnection();
				conn.setAutoCommit(false);
			}else {
				throw new Exception("Failed to close connection");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	@Test
	public void processSpring() throws Exception {
		logger.info("processSpring start");
		Long now, delta;
		
		String dummyQuery = "SELECT 1 FROM sysibm.sysdummy1";
		
		for (int i =0;i<TIMES;i++) {
			
			Integer tryCount = (i+1)*LOOP_COUNT;

			logger.info("=================================");
			logger.info("Start query "+tryCount+" times using sql "+dummyQuery);
			logger.info("=================================");
			
			now = System.currentTimeMillis();
			for (int j = 0; j < tryCount; j++) {
				dao.selectDummy(dummyQuery);
			}
			delta = (System.currentTimeMillis()-now);
		    logger.info("[Spring] Executed "+tryCount+" queries in "+delta+"(ms)");
			

			now = System.currentTimeMillis();
			for (int j = 0; j < tryCount; j++) {
				conn.createStatement().executeQuery(dummyQuery);
				if ((j+1)%LOOP_COUNT == 0) {
					refreshConnection();
				}
			}
			delta = (System.currentTimeMillis()-now);
		    logger.info("[DBCP] Executed "+tryCount+" queries in "+delta+"(ms)");
			logger.info("=================================");
			logger.info("");
		}
		
		
		
	}

}
