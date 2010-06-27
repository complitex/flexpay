package org.flexpay.common.dao.registry.impl;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.transform;
import org.flexpay.common.util.transform.Number2LongTransformer;
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
import java.util.Collections;
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
    @Override
	public List<RegistryRecord> listRecordsForImport(Long id, Long minId, Long maxId) {
		Object[] params = {id, minId, maxId};
		StopWatch watch = new StopWatch();

		watch.start();
		List<RegistryRecord> records = getHibernateTemplate()
				.findByNamedQuery("RegistryRecord.listRecordsForImport", params);
		watch.stop();

		log.debug("Time spent fetching records for import: {}, minId={}, maxId={}", new Object[]{watch, minId, maxId});
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
    @Override
	public List<RegistryRecord> filterRecords(Long registryId, ImportErrorTypeFilter importErrorTypeFilter,
											  RegistryRecordStatusFilter recordStatusFilter,
											  final Page<RegistryRecord> pager) {

		StringBuilder countSql = new StringBuilder("select count(1) ");
		final StringBuilder selectSql = new StringBuilder("select id ");
		StringBuilder fromWhereClause = new StringBuilder(
				"from common_registry_records_tbl use index (I_registry_status, I_registry_errortype) " +
				"where registry_id=? ");


		final List<Object> params = list();
		params.add(registryId);

		// filter by record status
		if (recordStatusFilter.needFilter()) {
			fromWhereClause.append("and record_status_id=? ");
			params.add(recordStatusFilter.getSelectedId());
		}

		if (importErrorTypeFilter.needFilter()) {
			if (importErrorTypeFilter.needFilterWithoutErrors()) {
				fromWhereClause.append("and import_error_type is null ");
			} else {
				fromWhereClause.append("and import_error_type=? ");
				params.add(importErrorTypeFilter.getSelectedType());
			}
		}

		final StringBuilder sqlCount = recordStatusFilter.needFilter() || importErrorTypeFilter.needFilter() ?
									   countSql.append(fromWhereClause) :
									   new StringBuilder("select records_number from common_registries_tbl where id=?");
		selectSql.append(fromWhereClause);

		final List ids = getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
			public List doInHibernate(Session session) throws HibernateException {
				log.debug("Filter records hqls: {}\n{}", sqlCount, selectSql);

				StopWatch watch = new StopWatch();
				watch.start();
				Number count = (Number) setParameters(session.createSQLQuery(sqlCount.toString()), params).uniqueResult();
				if (count == null) {
					count = 0;
				}
				watch.stop();
				log.debug("Time spent for count query: {}", watch);
				watch.reset();

				pager.setTotalElements(count.intValue());

				watch.start();
				List result = setParameters(session.createSQLQuery(selectSql.toString()), params)
						.setMaxResults(pager.getPageSize())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.list();
				watch.stop();
				log.debug("Time spent for listing: {}", watch);

				return result;
			}
		});

		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		StopWatch watch = new StopWatch();
		watch.start();
		List<RegistryRecord> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.getNamedQuery("RegistryRecord.listRecordsDetails")
						.setParameterList("ids", transform(ids, new Number2LongTransformer()))
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
    @Override
	public int getErrorsNumber(final Long registryId) {
		Number count = (Number) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery("select count(rr.id) from RegistryRecord rr where rr.registry.id=? and rr.importError.id>0")
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
    @Override
	public List<RegistryRecord> findRecords(final Long registryId, final Collection<Long> objectIds) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
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
    @Override
	public Long[] getMinMaxIdsForProcessing(@NotNull Long registryId) {
		List<?> result = getHibernateTemplate()
				.findByNamedQuery("RegistryRecord.listRecordsForProcessing.stats", registryId);
		Object[] objs = (Object[]) result.get(0);

		Long[] minMax = CollectionUtils.ar((Long) objs[0], (Long) objs[1]);
		if (minMax[0] == null || minMax[1] == null) {
			return CollectionUtils.ar(0L, 0L);
		}

		return minMax;
	}

	/**
	 * Get minimum and maximum record ids for processing
	 *
	 * @param registryId Registry identifier to process
	 * @param  restrictionMinId Constraint on registry record id
	 * @return Minimum-Maximum pair
	 */
	@NotNull
	public Long[] getMinMaxIdsForProcessing(@NotNull Long registryId, @NotNull Long restrictionMinId) {
		List<?> result = getHibernateTemplate()
				.findByNamedQuery("RegistryRecord.listRecordsForProcessing.stats.restriction", new Object[]{registryId, restrictionMinId});
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
    @Override
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
