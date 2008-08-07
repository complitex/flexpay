package org.flexpay.eirc.dao.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.dao.RegistryRecordDaoExt;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegistryRecordDaoExtImpl extends HibernateDaoSupport implements RegistryRecordDaoExt {

	private Logger log = Logger.getLogger(getClass());

	/**
	 * List registry records
	 *
	 * @param id	Registry id
	 * @param pager Pager
	 * @return list of records
	 */
	@SuppressWarnings({"unchecked"})
	public List<SpRegistryRecord> listRecordsForUpdate(final Long id, final Page pager) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				// read cached total elements
				if (pager.getTotalNumberOfElements() <= 0) {
					Long count = (Long) session.getNamedQuery("SpRegistryRecord.listRecords.count")
							.setLong(0, id).uniqueResult();
					pager.setTotalElements(count.intValue());
				}

				return session.getNamedQuery("SpRegistryRecord.listRecords")
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.setLong(0, id)
						.list();
			}
		});
	}

	/**
	 * Filter registry records
	 *
	 * @param registryId			Registry key
	 * @param importErrorTypeFilter Error type filter
	 * @param recordStatusFilter	Records status filter
	 * @param pager				 Page
	 * @return list of registry records
	 */
	@SuppressWarnings({"unchecked"})
	public List<SpRegistryRecord> filterRecords(Long registryId, ImportErrorTypeFilter importErrorTypeFilter,
												RegistryRecordStatusFilter recordStatusFilter, final Page<SpRegistryRecord> pager) {
		final StringBuilder hql = new StringBuilder("select distinct rr from SpRegistryRecord rr " +
				"inner join fetch rr.spRegistry r " +
				"inner join fetch rr.recordStatus rs " +
				"left join fetch rr.importError e where r.id=? ");

		final StringBuilder hqlCount = new StringBuilder("select count(*) from SpRegistryRecord rr " +
				"inner join rr.spRegistry r " +
				"inner join rr.recordStatus rs " +
				"left join rr.importError e where r.id=? ");

		final List<Object> params = new ArrayList<Object>();
		params.add(registryId);

		// filter by record status
		if (recordStatusFilter.needFilter()) {
			hql.append("and rs.code=? ");
			hqlCount.append("and rs.code=? ");
			params.add(recordStatusFilter.getSelectedStatus());
		}

		if (importErrorTypeFilter.needFilter()) {
			if (importErrorTypeFilter.needFilterWithoutErrors()) {
				hql.append("and rr.importError is null ");
				hqlCount.append("and rr.importError is null ");
			} else {
				hql.append("and e.status=? and e.objectType=?");
				hqlCount.append("and e.status=? and e.objectType=?");
				params.add(ImportError.STATUS_ACTIVE);
				params.add(importErrorTypeFilter.getSelectedType());
			}
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				if (log.isDebugEnabled()) {
					log.debug("Filter records hql: " + hqlCount);
				}

				Number count = (Number) setParameters(session.createQuery(hqlCount.toString()), params).uniqueResult();
				pager.setTotalElements(count.intValue());

				return setParameters(session.createQuery(hql.toString()), params)
						.setMaxResults(pager.getPageSize())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.list();
			}
		});
	}

	private Query setParameters(Query query, List<Object> params) {
		for (int n = 0; n < params.size(); ++n) {
			query.setParameter(n, params.get(n));
		}
		return query;
	}

	/**
	 * Count number of error in registry
	 *
	 * @param registryId Registry to count errors for
	 * @return number of errors
	 */
	public int getErrorsNumber(final Long registryId) {
		Number count = (Number) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery("select count(r.id) from SpRegistryRecord r where r.spRegistry.id=? and r.importError is not null")
						.setLong(0, registryId).uniqueResult();
			}
		});
		return count.intValue();
	}

	public DataSourceDescription getDataSourceDescription(final Long id) {
		return (DataSourceDescription) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.getNamedQuery("SpRegistryRecord.findDataSourceDescription")
						.setLong(0, id).uniqueResult();
			}
		});
	}

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	@SuppressWarnings({"unchecked"})
	public List<SpRegistryRecord> findRecords(final Long registryId, final Collection<Long> objectIds) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery("select distinct r from SpRegistryRecord r " +
						"inner join fetch r.spRegistry rr " +
						"inner join fetch rr.registryStatus " +
						"inner join fetch rr.serviceProvider sp " +
						"inner join fetch sp.dataSourceDescription " +
						"inner join fetch r.recordStatus " +
						"inner join fetch rr.registryType " +
						"left join fetch r.importError " +
						"left join fetch r.containers " +
						"where rr.id=:rId and r.id in (:ids)")
						.setParameterList("ids", objectIds)
						.setLong("rId", registryId)
						.list();
			}
		});
	}

	/**
	 * Get minimum and maximum record ids for processing
	 *
	 * @param registryId Registry identifier to process
	 * @return Minimum-Maximum pair
	 */
	@NotNull
	public Long[] getMinMaxIdsForProcessing(@NotNull Long registryId) {
		List result = getHibernateTemplate()
				.findByNamedQuery("SpRegistryRecord.getMinMaxRecordsForProcessing", registryId);
		Object[] objs = (Object[]) result.get(0);

		Long[] minMax = CollectionUtils.ar((Long) objs[0], (Long) objs[1]);
		if (minMax[0] == null || minMax[1] == null) {
			return CollectionUtils.ar(0L, 0L);
		}

		return minMax;
	}
}
