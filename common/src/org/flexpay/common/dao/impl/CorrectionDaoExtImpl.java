package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.CorrectionDaoExt;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CorrectionDaoExtImpl extends SimpleJdbcDaoSupport implements CorrectionDaoExt {

	private HibernateTemplate hibernateTemplate;

	/**
	 * Create or update data correction
	 *
	 * @param correction DataCorrection
	 */
    @Override
	public void save(DataCorrection correction) {
		hibernateTemplate.merge(correction);
	}

	/**
	 * Delete correction
	 *
	 * @param correction DataCorrection
	 */
    @Override
	public void delete(DataCorrection correction) {
		hibernateTemplate.delete(correction);
	}

	/**
	 * Find domain object by correction
	 *
	 * @param externalId DataSource internal object id
	 * @param type	   DomainObject type
	 * @param cls		DomainObject class to retrive
	 * @param sd		 Data source description
	 * @return DomainObject
	 */
	@Nullable
    @Override
	public <T extends DomainObject> Stub<T> findCorrection(String externalId, int type,
														   final Class<T> cls, Stub<DataSourceDescription> sd) {

		Long id = sd != null ?
				  getInternalId(externalId, type, sd) :
				  getInternalCommonId(externalId, type);
		if (id != null) {
			return new Stub<T>(id);
		}
		return null;
	}

	/**
	 * Check if correction exists
	 *
	 * @param externalId DataSource internal object id
	 * @param type	   DomainObject type
	 * @param sd		 DataSourceDescription
	 * @return DomainObject
	 */
    @Override
	public boolean existsCorrection(final String externalId, final int type,
									final Stub<DataSourceDescription> sd) {
		return getInternalId(externalId, type, sd) != null;
	}

    @Override
	public DataCorrection findCorrection(final String externalId, final int type, final Stub<DataSourceDescription> sd) {

		Object[] params = {externalId, type, sd.getId()};
		List<?> result = getJdbcTemplate().query("select id, internal_object_id from common_data_corrections_tbl " +
												 "where external_object_id=? and object_type=? and data_source_description_id=?",
				params, new RowMapper() {
                    @Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						DataCorrection correction = new DataCorrection();
						correction.setId(rs.getLong("id"));
						correction.setInternalObjectId(rs.getLong("internal_object_id"));
						correction.setDataSourceDescription(new DataSourceDescription(sd));
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
	 * @param externalId DataSource internal object id
	 * @param type	   DomainObject type
	 * @param sd		 Data source description
	 * @return DomainObject id if found, or <code>null</null> otherwise
	 */
	@Nullable
	private Long getInternalId(final String externalId, final int type,
							   final Stub<DataSourceDescription> sd) {

		Object[] params = {externalId, type, sd.getId()};
		List<?> result = getJdbcTemplate().query("select internal_object_id from common_data_corrections_tbl " +
												 "where external_object_id=? and object_type=? and data_source_description_id=?",
				params, new SingleColumnRowMapper(Long.class));

		return result.isEmpty() ? null : (Long) result.get(0);
	}

	@Nullable
    @Override
	public String getExternalId(final Long internalId, final int type,
								final Long dataSourceDescriptionId) {

		Object[] params = {internalId, type, dataSourceDescriptionId};
		List<?> result = getJdbcTemplate().query("select external_object_id from common_data_corrections_tbl " +
												 "where internal_object_id=? and object_type=? and data_source_description_id=?",
				params, new SingleColumnRowMapper(String.class));

		return result.isEmpty() ? null : (String) result.get(0);
	}

	private Long getInternalCommonId(final String externalId, final int type) {
		Object[] params = {externalId, type};
		List<?> result = getJdbcTemplate().query("select internal_object_id from common_data_corrections_tbl " +
												 "where external_object_id=? and object_type=? and data_source_description_id IS NULL",
				params, new SingleColumnRowMapper(Long.class));

		return result.isEmpty() ? null : (Long) result.get(0);
	}

    @Required
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
