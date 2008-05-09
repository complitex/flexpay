package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.CorrectionsDao;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CorrectionsDaoImpl extends SimpleJdbcDaoSupport implements CorrectionsDao {

	private HibernateTemplate hibernateTemplate;

	/**
	 * Create or update data correction
	 *
	 * @param correction DataCorrection
	 */
	public void save(DataCorrection correction) {
		hibernateTemplate.saveOrUpdate(correction);
	}

	/**
	 * Delete correction
	 *
	 * @param correction DataCorrection
	 */
	public void delete(DataCorrection correction) {
		hibernateTemplate.delete(correction);
	}

	/**
	 * Find domain object by correction
	 *
	 * @param externalId		DataSource internal object id
	 * @param type			  DomainObject type
	 * @param cls			   DomainObject class to retrive
	 * @param sourceDescription Data source description
	 * @return DomainObject
	 */
	public <T extends DomainObject> T findCorrection(String externalId, int type,
													 final Class<T> cls, DataSourceDescription sourceDescription) {

		Long id = sourceDescription != null ?
				  getInternalId(externalId, type, sourceDescription) :
				  getInternalCommonId(externalId, type);
		if (id != null) {
			try {
				T object = cls.newInstance();
				object.setId(id);
				return object;
			} catch (Exception e) {
				throw new RuntimeException("Cannot instantiate reference: " + cls.getName(), e);
			}
		}
		return null;
	}

	/**
	 * Check if correction exists
	 *
	 * @param externalId		DataSource internal object id
	 * @param type			  DomainObject type
	 * @param sourceDescription Data source description
	 * @return DomainObject
	 */
	public boolean existsCorrection(final String externalId, final int type,
									final DataSourceDescription sourceDescription) {
		return getInternalId(externalId, type, sourceDescription) != null;
	}

	public DataCorrection findCorrection(final String externalId, final int type, final DataSourceDescription sourceDescription) {

		Object[] params = {externalId, type, sourceDescription.getId()};
		List result = getJdbcTemplate().query("select id, internal_object_id from common_data_corrections_tbl " +
											  "where external_object_id=? and object_type=? and data_source_description_id=?",
				params, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				DataCorrection correction = new DataCorrection();
				correction.setId(rs.getLong("id"));
				correction.setInternalObjectId(rs.getLong("internal_object_id"));
				correction.setDataSourceDescription(sourceDescription);
				correction.setObjectType(type);
				correction.setExternalId(externalId);

				return correction;
			}
		});

		return result.isEmpty() ? null : (DataCorrection) result.get(0);
	}

	/**
	 * Get internal object id
	 *
	 * @param externalId		DataSource internal object id
	 * @param type			  DomainObject type
	 * @param sourceDescription Data source description
	 * @return DomainObject id if found, or <code>null</null> otherwise
	 */
	public Long getInternalId(final String externalId, final int type,
							  final DataSourceDescription sourceDescription) {

		Object[] params = {externalId, type, sourceDescription.getId()};
		List result = getJdbcTemplate().query("select internal_object_id from common_data_corrections_tbl " +
											  "where external_object_id=? and object_type=? and data_source_description_id=?",
				params, new SingleColumnRowMapper(Long.class));

		return result.isEmpty() ? null : (Long) result.get(0);
	}

	private Long getInternalCommonId(final String externalId, final int type) {
		Object[] params = {externalId, type};
		List result = getJdbcTemplate().query("select internal_object_id from common_data_corrections_tbl " +
											  "where external_object_id=? and object_type=? and data_source_description_id IS NULL",
				params, new SingleColumnRowMapper(Long.class));

		return result.isEmpty() ? null : (Long) result.get(0);
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
