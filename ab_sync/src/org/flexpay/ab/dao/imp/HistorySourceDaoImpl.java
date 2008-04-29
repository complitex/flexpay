package org.flexpay.ab.dao.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.HistorySourceDao;
import org.flexpay.ab.persistence.FieldType;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.ObjectType;
import org.flexpay.ab.persistence.SyncAction;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

	private HistoryRecordIterator iterator;

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
		sqlGetRecords = props.getProperty("sqlGetRecords");

		validateConfig();
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
	 * @throws Exception if failure occurs
	 */
	public Iterator<HistoryRecord> getRecords(Long lastRecord) throws Exception {

		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sqlGetRecords);
			ps.setLong(1, lastRecord);

			ResultSet rs = ps.executeQuery();

			HistoryRecordIterator it = new HistoryRecordIterator();
			it.con = con;
			it.ps = ps;
			it.rs = rs;
			it.source = this;

			this.iterator = it;

			return it;
		} catch (SQLException ex) {
			// Release Connection early, to avoid potential connection pool deadlock
			// in the case when the exception translator hasn't been initialized yet.
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(con, getDataSource());
			throw getExceptionTranslator().translate("PreparedStatementCallback", sqlGetRecords, ex);
		}
	}

	public void close() {
		if (iterator != null) {
			iterator.close();
			iterator = null;
		}
	}

	private static class HistoryRecordIterator implements Iterator<HistoryRecord> {

		private Connection con;
		private PreparedStatement ps;
		private ResultSet rs;
		private HistorySourceDaoImpl source;

		private List<HistoryRecord> recordsBuffer = new ArrayList<HistoryRecord>(1000);
		private Iterator<HistoryRecord> it;

		public boolean hasNext() {
			if (it == null || !it.hasNext()) {
				initBuffer();
			}
			return it.hasNext();
		}

		private void initBuffer() {
			recordsBuffer.clear();
			for (int n = 0; n < 1000; ++n) {
				try {
					if (!rs.next()) {
						break;
					}

					HistoryRecord record = new HistoryRecord();

					record.setRecordId(rs.getLong(source.fieldRecordId));
					record.setRecordDate(rs.getTimestamp(source.fieldRecordDate));
					record.setOldValue(rs.getString(source.fieldOldValue));
					record.setCurrentValue(rs.getString(source.fieldCurrentValue));
					record.setObjectType(ObjectType.getById(rs.getInt(source.fieldObjectTypeId)));
					record.setObjectId(rs.getLong(source.fieldObjectId));
					if (rs.getObject(source.fieldFieldName) != null) {
						record.setFieldType(FieldType.getById(rs.getInt(source.fieldFieldName)));
					}
					record.setSyncAction(SyncAction.getByCode(rs.getInt(source.fieldActionType)));

					recordsBuffer.add(record);
				} catch (SQLException e) {
					throw source.getExceptionTranslator().translate("PreparedStatementCallback", source.sqlGetRecords, e);
				}
			}
			it = recordsBuffer.iterator();
		}

		public HistoryRecord next() {
			return it.next();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void close() {
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(con, source.getDataSource());
		}
	}
}
