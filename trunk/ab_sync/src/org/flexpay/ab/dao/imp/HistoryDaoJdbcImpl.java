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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class HistoryDaoJdbcImpl extends SimpleJdbcDaoSupport implements HistoryDao {

	/**
	 * List history records
	 *
	 * @param pager Page instance
	 * @return List of HistoryRecord instances
	 */
	public List<HistoryRecord> getRecords(Page pager) {
		String sqlGetRecords = "select * from ab_sync_changes_tbl where processed=0 order by order_weight, object_id, record_date limit ?,?";
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
		}, pager.getThisPageFirstElementNumber(), pager.getPageSize());
	}

	/**
	 * Set records as processed
	 *
	 * @param records List of history records to mark as processed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setProcessed(List<HistoryRecord> records) {
		for (HistoryRecord record : records) {
			getSimpleJdbcTemplate().update("update ab_sync_changes_tbl set processed=1 where record_id=?", record.getRecordId());
		}
	}

	/**
	 * Create a new history record
	 *
	 * @param record HistoryRecord
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void addRecord(HistoryRecord record) {

		// check if record was already dumped
		int count = getSimpleJdbcTemplate().queryForInt(
				"select count(1) from ab_sync_changes_tbl where record_id=?",
				record.getRecordId());
		if (count > 0) {
			return;
		}

		getSimpleJdbcTemplate().update("INSERT INTO ab_sync_changes_tbl " +
									   "(record_id, record_date, old_value, current_value, object_type, object_id, field, action_type, processed, order_weight)" +
									   " VALUES (?, ?, ?, ?, ?, ?, ?, ? , ?)",
				record.getRecordId(), record.getRecordDate(), record.getOldValue(), record.getCurrentValue(), record.getObjectType().getId(),
				record.getObjectId(), record.getFieldType() != null ? record.getFieldType().getId() : null, record.getSyncAction().getCode(),
				0, record.getObjectType().getOrderWeight());
	}
}
