package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.CorrectionDaoExt;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class CorrectionDaoExtImpl extends SimpleJdbcDaoSupport implements CorrectionDaoExt {

    private Logger log = LoggerFactory.getLogger(getClass());

	private JpaTemplate jpaTemplate;

	/**
	 * Create or update data correction
	 *
	 * @param correction DataCorrection
	 */
    @Override
	public void save(DataCorrection correction) {
		jpaTemplate.merge(correction);
	}

	/**
	 * Delete correction
	 *
	 * @param correction DataCorrection
	 */
    @Override
	public void delete(DataCorrection correction) {
		jpaTemplate.remove(correction);
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
		List<DataCorrection> result = getJdbcTemplate().query("select id, internal_object_id from common_data_corrections_tbl " +
												 "where external_object_id=? and object_type=? and data_source_description_id=?",
				params, new RowMapper<DataCorrection>() {
                    @Override
					public DataCorrection mapRow(ResultSet rs, int rowNum) throws SQLException {
						DataCorrection correction = new DataCorrection();
						correction.setId(rs.getLong("id"));
						correction.setInternalObjectId(rs.getLong("internal_object_id"));
						correction.setDataSourceDescription(new DataSourceDescription(sd));
						correction.setObjectType(type);
						correction.setExternalId(externalId);

						return correction;
					}
				});

		return result.isEmpty() ? null : result.get(0);
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

        List<?> result = jpaTemplate.executeFind(new JpaCallback() {
            @Override
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {

                String sql = "select internal_object_id from common_data_corrections_tbl " +
                             "where external_object_id=? and object_type=? and data_source_description_id=?";

                List<? extends Serializable> params = list(externalId, type, sd.getId());

                Query query = entityManager.createNativeQuery(sql);
                for (int i = 0; i < params.size(); i++) {
                    query.setParameter(i + 1, params.get(i));
                }

                return query.getResultList();
            }
        });

        return result.isEmpty() ? null : ((BigInteger) result.get(0)).longValue();
	}

	@Nullable
    @Override
	public String getExternalId(final Long internalId, final int type,
								final Long dataSourceDescriptionId) {

        List<?> result = jpaTemplate.executeFind(new JpaCallback() {
            @Override
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {

                String sql = "select external_object_id from common_data_corrections_tbl " +
                             "where internal_object_id=? and object_type=? and data_source_description_id=?";

                List<? extends Serializable> params = list(internalId, type, dataSourceDescriptionId);

                Query query = entityManager.createNativeQuery(sql);
                for (int i = 0; i < params.size(); i++) {
                    query.setParameter(i + 1, params.get(i));
                }

                return query.getResultList();
            }
        });

        return result.isEmpty() ? null : (String) result.get(0);

	}

	private Long getInternalCommonId(final String externalId, final int type) {

        List<?> result = jpaTemplate.executeFind(new JpaCallback() {
            @Override
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {

                String sql = "select internal_object_id from common_data_corrections_tbl " +
                             "where external_object_id=? and object_type=? and data_source_description_id IS NULL";

                List<? extends Serializable> params = list(externalId, type);

                Query query = entityManager.createNativeQuery(sql);
                for (int i = 0; i < params.size(); i++) {
                    query.setParameter(i + 1, params.get(i));
                }

                return query.getResultList();
            }
        });

        return result.isEmpty() ? null : ((BigInteger) result.get(0)).longValue();

	}

    @Required
	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}
}
