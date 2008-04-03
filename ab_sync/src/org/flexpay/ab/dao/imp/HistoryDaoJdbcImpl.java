package org.flexpay.ab.dao.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.HistoryDao;
import org.flexpay.ab.persistence.FieldType;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.ObjectType;
import org.flexpay.ab.persistence.SyncAction;
import org.flexpay.common.dao.paging.Page;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

public class HistoryDaoJdbcImpl extends SimpleJdbcDaoSupport implements HistoryDao {

	private String sqlGetRecords;
	private String sqlSetProcessed;

	private String tableName;
	private String fieldRecordDate;
	private String fieldOldValue;
	private String fieldCurrentValue;
	private String fieldObjectTypeId;
	private String fieldObjectId;
	private String fieldFieldName;
	private String fieldActionType;

	public HistoryDaoJdbcImpl(Properties props) {

		tableName = props.getProperty("historyTableName");
		fieldRecordDate = props.getProperty("fieldRecordDate");
		fieldOldValue = props.getProperty("fieldOldValue");
		fieldCurrentValue = props.getProperty("fieldCurrentValue");
		fieldObjectTypeId = props.getProperty("fieldObjectTypeId");
		fieldObjectId = props.getProperty("fieldObjectId");
		fieldFieldName = props.getProperty("fieldFieldName");
		fieldActionType = props.getProperty("fieldActionType");

		validateConfig();

		sqlGetRecords = String.format("select * from %s where %2$s >= ? and processed=0 order by order_weight, %2$s limit ?,?", tableName, fieldRecordDate);
		sqlSetProcessed = String.format("update %s set processed=1 where %s=? and %s $oldValue and %s $currentValue and %s=? and %s=? and %s $field and %s=?",
				tableName, fieldRecordDate, fieldOldValue, fieldCurrentValue, fieldObjectTypeId,
				fieldObjectId, fieldFieldName, fieldActionType);
	}

	private void validateConfig() {
		if (StringUtils.isBlank(tableName)) {
			throw new IllegalArgumentException("Invalid configuration, property historyTableName cannot be blank.");
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
		if (StringUtils.isBlank(fieldFieldName)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldFieldName cannot be blank.");
		}
		if (StringUtils.isBlank(fieldActionType)) {
			throw new IllegalArgumentException("Invalid configuration, property fieldActionType cannot be blank.");
		}
	}

	/**
	 * List history records
	 *
	 * @param pager			Page instance
	 * @param lastModifiedDate Date to filter records
	 * @return List of HistoryRecord instances
	 */
	public List<HistoryRecord> getRecords(Page pager, Date lastModifiedDate) {
		return getSimpleJdbcTemplate().query(sqlGetRecords, new ParameterizedRowMapper<HistoryRecord>() {
			public HistoryRecord mapRow(ResultSet rs, int i) throws SQLException {
				HistoryRecord record = new HistoryRecord();

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
		}, lastModifiedDate, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}

	/**
	 * Set records as processed
	 *
	 * @param records List of history records to mark as processed
	 */
	public void setProcessed(List<HistoryRecord> records) {
		for (HistoryRecord record : records) {
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(record.getRecordDate());
			if (record.getOldValue() != null) {
				params.add(record.getOldValue());
			}
			if (record.getCurrentValue() != null) {
				params.add(record.getCurrentValue());
			}
			params.add(record.getObjectType().getId());
			params.add(record.getObjectId());
			if (record.getFieldType() != null) {
				params.add(record.getFieldType().getId());
			}
			params.add(record.getSyncAction().getCode());
			getSimpleJdbcTemplate().update(getSetProcessedSql(record), params.toArray());
		}
	}

	private String getSetProcessedSql(HistoryRecord record) {
		return sqlSetProcessed
				.replace("$oldValue", record.getOldValue() == null ? "is null" : "= ?")
				.replace("$currentValue", record.getCurrentValue() == null ? "is null" : "= ?")
				.replace("$field", record.getFieldType() == null ? "is null" : "= ?" );
	}
}
