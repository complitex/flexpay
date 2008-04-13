package org.flexpay.ab.dao.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.HistorySourceDao;
import org.flexpay.ab.persistence.FieldType;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.ObjectType;
import org.flexpay.ab.persistence.SyncAction;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class HistorySourceDaoImpl extends SimpleJdbcDaoSupport implements HistorySourceDao {

	private String sqlGetRecords;

	private String tableName;
	private String fieldRecordId;
	private String fieldRecordDate;
	private String fieldOldValue;
	private String fieldCurrentValue;
	private String fieldObjectTypeId;
	private String fieldObjectId;
	private String fieldFieldName;
	private String fieldActionType;

	public HistorySourceDaoImpl(Properties props) {

		tableName = props.getProperty("historyTableName");
		fieldRecordId = props.getProperty("fieldRecordId");
		fieldRecordDate = props.getProperty("fieldRecordDate");
		fieldOldValue = props.getProperty("fieldOldValue");
		fieldCurrentValue = props.getProperty("fieldCurrentValue");
		fieldObjectTypeId = props.getProperty("fieldObjectTypeId");
		fieldObjectId = props.getProperty("fieldObjectId");
		fieldFieldName = props.getProperty("fieldFieldName");
		fieldActionType = props.getProperty("fieldActionType");

		validateConfig();

		sqlGetRecords = String.format("select * from %s where %s>=?", tableName, fieldRecordId);
	}

	private void validateConfig() {
		if (StringUtils.isBlank(tableName)) {
			throw new IllegalArgumentException("Invalid configuration, property historyTableName cannot be blank.");
		}
		if (StringUtils.isBlank(fieldRecordId)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldRecordId cannot be blank.");
		}
		if (StringUtils.isBlank(fieldRecordDate)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldRecordDate cannot be blank.");
		}
		if (StringUtils.isBlank(fieldOldValue)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldOldValue cannot be blank.");
		}
		if (StringUtils.isBlank(fieldCurrentValue)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldCurrentValue cannot be blank.");
		}
		if (StringUtils.isBlank(fieldObjectTypeId)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldObjectTypeId cannot be blank.");
		}
		if (StringUtils.isBlank(fieldObjectId)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldObjectId cannot be blank.");
		}
		if (StringUtils.isBlank(fieldFieldName)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldFieldName cannot be blank.");
		}
		if (StringUtils.isBlank(fieldActionType)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldActionType cannot be blank.");
		}
	}

	/**
	 * Get list of history records
	 *
	 * @param lastRecord Last record obtained
	 * @return List of new records
	 */
	public List<HistoryRecord> getRecords(Long lastRecord) {
		return getSimpleJdbcTemplate().query(sqlGetRecords, new ParameterizedRowMapper<HistoryRecord>() {
			public HistoryRecord mapRow(ResultSet rs, int i) throws SQLException {
				HistoryRecord record = new HistoryRecord();

				record.setRecordId(rs.getLong(fieldRecordId));
				record.setRecordDate(rs.getTimestamp(fieldRecordDate));
				record.setOldValue(rs.getString(fieldOldValue));
				record.setCurrentValue(rs.getString(fieldCurrentValue));
				record.setObjectType(ObjectType.getById(rs.getInt(fieldObjectTypeId)));
				record.setObjectId(rs.getLong(fieldObjectId));
				if (rs.getObject(fieldFieldName) != null) {
					record.setFieldType(FieldType.getById(rs.getInt(fieldFieldName)));
				}
				record.setSyncAction(SyncAction.getByCode(rs.getInt(fieldActionType)));

				return record;
			}
		}, lastRecord);
	}
}
