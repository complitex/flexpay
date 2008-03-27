package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.persistence.Service;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceDaoJDBC extends SimpleJdbcDaoSupport {

	public Service findService(Long providerId, Long typeId) {
		return getSimpleJdbcTemplate().queryForObject(
				"select id from eirc_services_tbl where provider_id=? and type_id=?",
				new ParameterizedRowMapper<Service>() {
					public Service mapRow(ResultSet resultSet, int i) throws SQLException {
						return new Service(resultSet.getLong("id"));
					}
				}, providerId, typeId);
	}
}
