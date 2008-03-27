package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.UpdateConfigDao;
import org.flexpay.ab.persistence.UpdateConfig;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateConfigDaoJdbcImpl extends SimpleJdbcDaoSupport implements UpdateConfigDao {

	/**
	 * Read update config
	 *
	 * @return UpdateConfig instance
	 */
	public UpdateConfig getConfig() {
		return getSimpleJdbcTemplate().queryForObject("select * ab_sync_config_tbl", new ParameterizedRowMapper<UpdateConfig>() {
			public UpdateConfig mapRow(ResultSet rs, int i) throws SQLException {
				UpdateConfig config = new UpdateConfig();
				config.setLastUpdateDate(rs.getDate("last_update"));
				config.setLastRecordUpdateTime(rs.getDate("last_record_update"));
				return config;
			}
		});
	}

	/**
	 * Save update config
	 *
	 * @param config UpdateConfig to store
	 */
	public void saveConfig(UpdateConfig config) {
		getSimpleJdbcTemplate().update("update ab_sync_config_tbl set last_update=?, last_record_update=?",
				config.getLastUpdateDate(), config.getLastRecordUpdateTime());
	}
}
