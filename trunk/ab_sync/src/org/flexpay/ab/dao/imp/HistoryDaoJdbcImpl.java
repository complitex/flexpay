package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.HistoryDao;
import org.flexpay.ab.persistence.FieldType;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.ObjectType;
import org.flexpay.ab.persistence.SyncAction;
import org.flexpay.common.dao.paging.Page;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class HistoryDaoJdbcImpl extends SimpleJdbcDaoSupport implements HistoryDao {

	private org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * List history records
	 *
	 * @param pager Page instance
	 * @return List of HistoryRecord instances
	 */
	public List<HistoryRecord> getRecords(Page pager) {
		String sqlGetRecords = "select * from ab_sync_changes_tbl where processed=0 order by order_weight, object_id, record_date limit ?";
		return getSimpleJdbcTemplate().query(sqlGetRecords, new ParameterizedRowMapper<HistoryRecord>() {
			public HistoryRecord mapRow(ResultSet rs, int i) throws SQLException {
				HistoryRecord record = new HistoryRecord();

				record.setRecordId(rs.getLong("record_id"));
				record.setRecordDate(rs.getTimestamp("record_date"));
				record.setOldValue(rs.getString("old_value"));
				record.setCurrentValue(rs.getString("current_value"));
				record.setObjectType(ObjectType.getById(rs.getInt("object_type")));
				record.setObjectId(rs.getLong("object_id"));
				if (rs.getObject("field") != null) {
					record.setFieldType(FieldType.getById(rs.getInt("field")));
				}
				record.setSyncAction(SyncAction.getByCode(rs.getInt("action_type")));

				return record;
			}
		}, pager.getPageSize());
	}

	/**
	 * Set records as processed
	 *
	 * @param records List of history records to mark as processed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setProcessed(List<HistoryRecord> records) {
		for (HistoryRecord record : records) {
			getSimpleJdbcTemplate().update(getSetProcessedSql(record), getParams(record));
		}
	}

	private Object[] getParams(HistoryRecord record) {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(record.getRecordId());
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

		return params.toArray(new Object[params.size()]);
	}

	private String getWhere(HistoryRecord record) {
		return ("where record_id=? and record_date=? and old_value $oldValue and " +
				"current_value $currentValue and object_type=? and object_id=? and field $field and action_type=?")
				.replace("$oldValue", record.getOldValue() == null ? "is null" : "= ?")
				.replace("$currentValue", record.getCurrentValue() == null ? "is null" : "= ?")
				.replace("$field", record.getFieldType() == null ? "is null" : "= ?");
	}

	private String getSetProcessedSql(HistoryRecord record) {
		return "update ab_sync_changes_tbl set processed=1 " + getWhere(record);
	}

	/**
	 * Create a new history record
	 *
	 * @param record HistoryRecord
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void addRecord(HistoryRecord record) {

		// check if record was already dumped
		int count = getSimpleJdbcTemplate().queryForInt("select count(1) from ab_sync_changes_tbl " + getWhere(record),
				getParams(record));
		if (count > 0) {
			log.info("Record alread exists: " + record);
			return;
		}

		getSimpleJdbcTemplate().update("INSERT INTO ab_sync_changes_tbl " +
									   "(record_id, record_date, old_value, current_value, object_type, object_id, field, action_type, processed, order_weight)" +
									   " VALUES (?, ?, ?, ?, ?, ?, ?, ? , ?, ?)",
				record.getRecordId(), record.getRecordDate(), record.getOldValue(), record.getCurrentValue(), record.getObjectType().getId(),
				record.getObjectId(), record.getFieldType() != null ? record.getFieldType().getId() : null, record.getSyncAction().getCode(),
				0, record.getObjectType().getOrderWeight());
	}
}
