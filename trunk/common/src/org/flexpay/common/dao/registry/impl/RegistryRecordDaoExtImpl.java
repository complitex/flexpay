package org.flexpay.common.dao.registry.impl;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class RegistryRecordDaoExtImpl extends HibernateDaoSupport implements RegistryRecordDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * List registry records for import operation
	 *
	 * @param id	Registry id
	 * @param minId Minimum registry record id to retrieve
	 * @param maxId Maximum registry record id to retrieve
	 * @return list of records
	 */
	@SuppressWarnings ({"unchecked"})
	public List<RegistryRecord> listRecordsForImport(Long id, Long minId, Long maxId) {
		Object[] params = {id, minId, maxId};
		StopWatch watch = new StopWatch();

		watch.start();
		List<RegistryRecord> records = getHibernateTemplate()
				.findByNamedQuery("RegistryRecord.listRecordsForImport", params);
		watch.stop();

		log.debug("Time spent fetching records for import: {}", watch);
		return records;
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
	@SuppressWarnings ({"unchecked"})
	public List<RegistryRecord> filterRecords(Long registryId, ImportErrorTypeFilter importErrorTypeFilter,
											  RegistryRecordStatusFilter recordStatusFilter,
											  final Page<RegistryRecord> pager) {

		StringBuilder fromClause = new StringBuilder("from RegistryRecord rr ");


		final StringBuilder hql = new StringBuilder("select distinct rr ");

		StringBuilder whereClause = new StringBuilder("where rr.registry.id=? ");

		final List<Object> params = list();
		params.add(registryId);

		// filter by record status
		if (recordStatusFilter.needFilter()) {
			fromClause.append("left join rr.recordStatus rs ");
			whereClause.append("and rs.code=? ");
			params.add(recordStatusFilter.getSelectedStatus());
		}

		if (importErrorTypeFilter.needFilter()) {
			fromClause.append("left join fetch rr.importError e ");
			if (importErrorTypeFilter.needFilterWithoutErrors()) {
				whereClause.append("and rr.importError is null ");
			} else {
				whereClause.append("and e.status=? and e.objectType=? ");
				params.add(ImportError.STATUS_ACTIVE);
				params.add(importErrorTypeFilter.getSelectedType());
			}
		}

		final StringBuilder hqlCount = recordStatusFilter.needFilter() || importErrorTypeFilter.needFilter() ?
				new StringBuilder("select count(*) ").append(fromClause).append(whereClause) :
				new StringBuilder("select recordsNumber from Registry where id=?");
		hql.append(fromClause).append(whereClause);

		List<RegistryRecord> records = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List doInHibernate(Session session) throws HibernateException {
				log.debug("Filter records hqls: {}\n{}", hqlCount, hql);

				StopWatch watch = new StopWatch();
				watch.start();
				Number count = (Number) setParameters(session.createQuery(hqlCount.toString()), params).uniqueResult();
				watch.stop();
				log.debug("Time spent for count query: {}", watch);
				watch.reset();

				pager.setTotalElements(count.intValue());

				watch.start();
				List result = setParameters(session.createQuery(hql.toString()), params)
						.setMaxResults(pager.getPageSize())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.list();
				watch.stop();
				log.debug("Time spent for listing: {}", watch);

				return result;
			}
		});

		StopWatch watch = new StopWatch();
		watch.start();
		final Collection<Long> ids = DomainObject.collectionIds(records);
		List<RegistryRecord> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.getNamedQuery("RegistryRecord.listRecordsDetails")
						.setParameterList("ids", ids)
						.list();
			}
		});

		watch.stop();
		log.debug("Time spent for listRecordsDetails: {}", watch);

		return result;
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
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery("select count(r.id) from RegistryRecord r where r.registry.id=? and r.importError is not null")
						.setLong(0, registryId).uniqueResult();
			}
		});
		return count.intValue();
	}

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	@SuppressWarnings ({"unchecked"})
	public List<RegistryRecord> findRecords(final Long registryId, final Collection<Long> objectIds) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery("select distinct r from RegistryRecord r " +
										   "inner join fetch r.properties " +
										   "inner join fetch r.registry rr " +
										   "inner join fetch rr.registryStatus " +
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
		List<?> result = getHibernateTemplate()
				.findByNamedQuery("RegistryRecord.getMinMaxRecordsForProcessing", registryId);
		Object[] objs = (Object[]) result.get(0);

		Long[] minMax = CollectionUtils.ar((Long) objs[0], (Long) objs[1]);
		if (minMax[0] == null || minMax[1] == null) {
			return CollectionUtils.ar(0L, 0L);
		}

		return minMax;
	}

	/**
	 * Get minimum and maximum record ids for importing
	 *
	 * @param registryId Registry identifier to import
	 * @return Minimum-Maximum pair
	 */
	@NotNull
	public Long[] getMinMaxIdsForImporting(@NotNull Long registryId) {
		List<?> result = getHibernateTemplate()
				.findByNamedQuery("RegistryRecord.getMinMaxRecordsForImporting", registryId);
		Object[] objs = (Object[]) result.get(0);

		Long[] minMax = CollectionUtils.ar((Long) objs[0], (Long) objs[1]);
		if (minMax[0] == null || minMax[1] == null) {
			return CollectionUtils.ar(0L, 0L);
		}

		return minMax;
	}
}
