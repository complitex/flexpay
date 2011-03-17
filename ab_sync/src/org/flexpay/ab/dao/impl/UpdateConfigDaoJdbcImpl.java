package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.UpdateConfigDao;
import org.flexpay.ab.persistence.UpdateConfig;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class UpdateConfigDaoJdbcImpl extends SimpleJdbcDaoSupport implements UpdateConfigDao {

	/**
	 * Read update config
	 *
	 * @return UpdateConfig instance
	 */
    @Override
	public UpdateConfig getConfig() {
		return getSimpleJdbcTemplate().queryForObject("select * from ab_sync_config_tbl", new RowMapper<UpdateConfig>() {
            @Override
			public UpdateConfig mapRow(ResultSet rs, int i) throws SQLException {
				UpdateConfig config = new UpdateConfig();
				config.setLastUpdateDate(rs.getTimestamp("last_update"));
				config.setLastDumpedRecordId(rs.getLong("last_record_id"));
				return config;
			}
		});
	}

	/**
	 * Save update config
	 *
	 * @param config UpdateConfig to store
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
	public void saveConfig(UpdateConfig config) {
		getSimpleJdbcTemplate().update("update ab_sync_config_tbl set last_update=?, last_record_id=?",
				config.getLastUpdateDate(), config.getLastDumpedRecordId());
	}
}
