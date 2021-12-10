package app.test.db.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import app.test.db.dao.Dao;

@Repository
public class DaoImpl implements Dao {

	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void selectDummy(String dummyQuery) throws Exception{
		this.jdbcTemplate.queryForList(dummyQuery);
	}

}