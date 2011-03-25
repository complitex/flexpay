package org.flexpay.common.dao.registry.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.RecordErrorsType;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.persistence.registry.filter.FilterData;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.transform.Number2LongTransformer;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.persistence.DataCorrection.*;
import static org.flexpay.common.persistence.filter.StringValueFilter.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.transform;

public class RegistryRecordDaoExtImpl extends SimpleJdbcDaoSupport implements RegistryRecordDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

    private HibernateTemplate hibernateTemplate;

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
		List<RegistryRecord> records = hibernateTemplate
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

		final List ids = hibernateTemplate.executeFind(new HibernateCallback<List<?>>() {
            @Override
			public List<?> doInHibernate(Session session) throws HibernateException {
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
		List<RegistryRecord> result = hibernateTemplate.executeFind(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				return session.getNamedQuery("RegistryRecord.listRecordsDetails")
						.setParameterList("ids", transform(ids, new Number2LongTransformer()))
						.list();
			}
		});

		watch.stop();
		log.debug("Time spent for listRecordsDetails: {}", watch);

		return result;
	}

    @SuppressWarnings ({"unchecked"})
    @Override
    public List<RegistryRecord> filterRecords(Long registryId, Collection<ObjectFilter> filters, final Page<RegistryRecord> pager) {

        StringBuilder countSql = new StringBuilder("select count(1) ");
        final StringBuilder selectSql = new StringBuilder("select id ");
        StringBuilder fromWhere = new StringBuilder(" from common_registry_records_tbl where registry_id=? ");


        final List<Object> params = list();
        params.add(registryId);

        boolean haveFilter = getFilters(fromWhere, params, filters);

        final StringBuilder sqlCount = haveFilter ?
                                       countSql.append(fromWhere) :
                                       new StringBuilder("select records_number from common_registries_tbl where id=?");
        selectSql.append(fromWhere);

        final List ids = hibernateTemplate.executeFind(new HibernateCallback<List<?>>() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {
                log.debug("Filter records sqls: {}\n{}", sqlCount, selectSql);
                log.debug("Params: {}", params);

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

        if (log.isDebugEnabled()) {
            log.debug("Found {} records", ids.size());
        }

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        StopWatch watch = new StopWatch();
        watch.start();
        List<RegistryRecord> result = hibernateTemplate.executeFind(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return session.getNamedQuery("RegistryRecord.listRecordsDetails")
                        .setParameterList("ids", transform(ids, new Number2LongTransformer()))
                        .list();
            }
        });

        watch.stop();
        log.debug("Time spent for listRecordsDetails: {}", watch);

        return result;

    }

    private boolean getFilters(StringBuilder fromWhere, List<Object> params, Collection<ObjectFilter> filters) {

        boolean haveFilter = false;

        for (ObjectFilter filter : filters) {

            if (filter instanceof RegistryRecordStatusFilter && filter.needFilter()) {
                fromWhere.append(" and record_status_id=? ");
                RegistryRecordStatusFilter registryRecordStatusFilter = (RegistryRecordStatusFilter) filter;
                params.add(registryRecordStatusFilter.getSelectedId());
                haveFilter = true;
            } else if (filter instanceof ImportErrorTypeFilter && filter.needFilter()) {
                ImportErrorTypeFilter importErrorTypeFilter = (ImportErrorTypeFilter) filter;
                if (importErrorTypeFilter.needFilterWithoutErrors()) {
                    fromWhere.append(" and import_error_type is null ");
                } else {
                    fromWhere.append(" and import_error_type=? ");
                    params.add(importErrorTypeFilter.getSelectedType());
                }
                haveFilter = true;
            } else if (filter instanceof StringValueFilter) {
                StringValueFilter aFilter = (StringValueFilter) filter;
                if (aFilter.getType().equals(TYPE_TOWN)) {
                    fromWhere.append(" and town_name like ? ");
                    params.add(aFilter.getValue());
                    haveFilter = true;
                } else if (aFilter.getType().equals(TYPE_STREET)) {
                    fromWhere.append(" and concat(street_type, ' ', street_name) like ? ");
                    params.add(aFilter.getValue());
                    haveFilter = true;
                } else if (aFilter.getType().equals(TYPE_BUILDING)) {
                    fromWhere.append(" and concat(building_number, IFNULL(concat(' ', bulk_number), '')) like ? ");
                    params.add(aFilter.getValue());
                    haveFilter = true;
                } else if (aFilter.getType().equals(TYPE_APARTMENT)) {
                    fromWhere.append(" and apartment_number like ? ");
                    params.add(aFilter.getValue());
                    haveFilter = true;
                } else if (aFilter.getType().equals(TYPE_FIO)) {
                    fromWhere.append(" and concat(last_name, ' ', middle_name, ' ', first_name) like ? ");
                    params.add(aFilter.getValue());
                    haveFilter = true;
                }
            }

        }

        return haveFilter;
        
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<RegistryRecord> filterRecords(Long registryId, Collection<ObjectFilter> filters,
                                              String criteria, List<Object> params, final Page<RegistryRecord> pager) {

        final StringBuilder sql = new StringBuilder("select id ");
        final StringBuilder sqlCount = new StringBuilder("select count(*) ");

        StringBuilder fromWhere = new StringBuilder("from common_registry_records_tbl c where registry_id = ? ");
        fromWhere.append(criteria);

        final List<Object> paramsSql = list();
        paramsSql.add(registryId);
        paramsSql.addAll(params);
        getFilters(fromWhere, paramsSql, filters);

        sql.append(fromWhere);
        sqlCount.append(fromWhere);

        log.debug("Pager = {}", pager);
        log.debug("params = {}", paramsSql);

        final List ids = hibernateTemplate.executeFind(new HibernateCallback<List<?>>() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {
                log.debug("Filter records sqls: {}\n{}", sqlCount, sql);

                StopWatch watch = new StopWatch();
                if (log.isDebugEnabled()) {
                    watch.start();
                }
                Number count = (Number) setParameters(session.createSQLQuery(sqlCount.toString()), paramsSql).uniqueResult();
                log.debug("count = {}", count);
                if (count == null) {
                    count = 0;
                }
                if (log.isDebugEnabled()) {
                    watch.stop();
                    log.debug("Time spent for count query: {}", watch);
                    watch.reset();
                }

                pager.setTotalElements(count.intValue());

                if (log.isDebugEnabled()) {
                    watch.start();
                }
                List<?> result = setParameters(session.createSQLQuery(sql.toString()), paramsSql)
                        .setMaxResults(pager.getPageSize())
                        .setFirstResult(pager.getThisPageFirstElementNumber())
                        .list();

                log.debug("Result = {}", result);

                if (log.isDebugEnabled()) {
                    watch.stop();
                    log.debug("Time spent for listing: {}", watch);
                }

                return result;
            }
        });

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        StopWatch watch = new StopWatch();
        if (log.isDebugEnabled()) {
            watch.start();
        }
        List<RegistryRecord> result = hibernateTemplate.executeFind(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return session.getNamedQuery("RegistryRecord.listRecordsDetails")
                        .setParameterList("ids", transform(ids, new Number2LongTransformer()))
                        .list();
            }
        });

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Time spent for listRecordsDetails: {}", watch);
        }

        return result;
    }

    private void getCorrectionCriteria(StringBuilder fromWhereClause, List<Object> params, RegistryRecord record, String type) throws FlexPayException {

        String recTownType = record.getTownType();
        String recTownName = record.getTownName();
        String recStreetType = record.getStreetType();
        String recStreetName = record.getStreetName();
        String recBuilding = record.getBuildingNum();
        String recBuildingBulk = record.getBuildingBulkNum();
        String recApartment = record.getApartmentNum();
        String recFirstName = record.getFirstName();
        String recMiddleName = record.getMiddleName();
        String recLastName = record.getLastName();

        String isNull = " is ? ";
        String value = " = ? ";

        String corTownType = "and town_type" + (recTownType == null ? isNull : value);
        String corTownName = "and town_name" + (recTownName == null ? isNull : value);
        String corStreetType = "and street_type" + (recStreetType == null ? isNull : value);
        String corStreetName = "and street_name" + (recStreetName == null ? isNull : value);
        String corBuilding = "and building_number" + (recBuilding == null ? isNull : value);
        String corBuildingBulk = "and bulk_number" + (recBuildingBulk == null ? isNull : value);
        String corApartment = "and apartment_number" + (recApartment == null ? isNull : value);
        String corFirstName = "and first_name" + (recFirstName == null ? isNull : value);
        String corMiddleName = "and middle_name" + (recMiddleName == null ? isNull : value);
        String corLastName = "and last_name" + (recLastName == null ? isNull : value);

        if (!CORRECT_TYPE_STREET_TYPE.equals(type)) {
            fromWhereClause.append(corTownType).append(corTownName);
            params.add(recTownType);
            params.add(recTownName);
        }

        if (CORRECT_TYPE_STREET_TYPE.equals(type)) {
            fromWhereClause.append(corStreetType);
            params.add(recStreetType);
        } else if (CORRECT_TYPE_STREET.equals(type)) {
            fromWhereClause.append(corStreetType).append(corStreetName);
            params.add(recStreetType);
            params.add(recStreetName);
        } else if (CORRECT_TYPE_BUILDING.equals(type)) {
            fromWhereClause.append(corStreetType).append(corStreetName).append(corBuilding).append(corBuildingBulk);
            params.add(recStreetType);
            params.add(recStreetName);
            params.add(recBuilding);
            params.add(recBuildingBulk);
        } else if (CORRECT_TYPE_APARTMENT.equals(type)) {
            fromWhereClause.append(corStreetType).append(corStreetName).append(corBuilding).append(corBuildingBulk).append(corApartment);
            params.add(recStreetType);
            params.add(recStreetName);
            params.add(recBuilding);
            params.add(recBuildingBulk);
            params.add(recApartment);
        } else if (CORRECT_TYPE_PERSON.equals(type)) {
            fromWhereClause.append(corStreetType).append(corStreetName).append(corBuilding).append(corBuildingBulk).append(corApartment).
                    append(corFirstName).append(corMiddleName).append(corLastName);
            params.add(recStreetType);
            params.add(recStreetName);
            params.add(recBuilding);
            params.add(recBuildingBulk);
            params.add(recFirstName);
            params.add(recMiddleName);
            params.add(recLastName);
        } else {
            log.warn("Incorrect type parameter {}", type);
            throw new FlexPayException("Incorrect correction type", "payments.error.registry.incorrect_correction_type");
        }

    }

    @SuppressWarnings ({"unchecked"})
    @Override
    public List<RegistryRecord> findRecordsWithThisError(RegistryRecord record, String correctionType, final Page<RegistryRecord> pager) {

        final StringBuilder selectSql = new StringBuilder("select id ");
        StringBuilder fromWhereClause = new StringBuilder(
                "from common_registry_records_tbl use index (I_registry_status, I_registry_errortype) " +
                "where registry_id=? ");


        final List<Object> params = list();
        params.add(record.getRegistry().getId());

        try {
            getCorrectionCriteria(fromWhereClause, params, record, correctionType);
        } catch (FlexPayException e) {
            log.error("Incorrect correction type", e);
            return list();
        }

        fromWhereClause.append("and record_status_id=? ");
        params.add(record.getRecordStatus().getId());
        fromWhereClause.append("and import_error_type=? ");
        params.add(record.getImportErrorType());

        log.debug("Correction criteria: {},\nparams: {}", fromWhereClause, params);

        selectSql.append(fromWhereClause);

        final List ids = hibernateTemplate.executeFind(new HibernateCallback<List<?>>() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {
                log.debug("Filter records hqls: {}", selectSql);

                StopWatch watch = new StopWatch();
                if (log.isDebugEnabled()) {
                    watch.start();
                }

                List result = setParameters(session.createSQLQuery(selectSql.toString()), params)
                        .setMaxResults(pager.getPageSize())
                        .list();

                if (log.isDebugEnabled()) {
                    watch.stop();
                    log.debug("Time spent for listing: {}", watch);
                }

                return result;
            }
        });

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        StopWatch watch = new StopWatch();
        if (log.isDebugEnabled()) {
            watch.start();
        }
        List<RegistryRecord> result = hibernateTemplate.executeFind(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return session.getNamedQuery("RegistryRecord.listRecordsDetails")
                        .setParameterList("ids", transform(ids, new Number2LongTransformer()))
                        .list();
            }
        });

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Time spent for listRecordsDetails: {}", watch);
        }

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
		Number count = (Number) hibernateTemplate.execute(new HibernateCallback<Object>() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery("select count(rr.id) from RegistryRecord rr where rr.registry.id=? and rr.importError.id>0 and rr.importError.status=0")
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
		return hibernateTemplate.executeFind(new HibernateCallback<Object>() {
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

    @Override
    public List<String> findAutocompleterAddresses(Long registryId, FilterData filterData, final Page<String> pager) {

        final StringBuilder sql = new StringBuilder("select distinct ");
        StringBuilder fromWhere = new StringBuilder(" from common_registry_records_tbl rr where registry_id = ? ");
        StringBuilder orderBy = new StringBuilder(" order by n");

        final List<Object> params = list();
        params.add(registryId);

        boolean stringNotEmpty = isNotEmpty(filterData.getString());

        if (TYPE_TOWN.equals(filterData.getType())) {
            sql.append("upper(town_name) n ");
            if (stringNotEmpty) {
                fromWhere.append(" and town_name like ?");
            }
        } else if (TYPE_STREET.equals(filterData.getType())) {
            sql.append("upper(concat(street_type, ' ', street_name)) n ");
            if (stringNotEmpty) {
                fromWhere.append(" and street_name like ?");
            }
        } else if (TYPE_BUILDING.equals(filterData.getType())) {
            sql.append("upper(concat(building_number, IFNULL(concat(' ', bulk_number), ''))) n ");
            if (stringNotEmpty) {
                fromWhere.append(" and concat(building_number, IFNULL(concat(' ', bulk_number), '')) like ?");
            }
        } else if (TYPE_APARTMENT.equals(filterData.getType())) {
            sql.append("upper(apartment_number) n ");
            if (stringNotEmpty) {
                fromWhere.append(" and apartment_number like ?");
            }
        }
        if (stringNotEmpty) {
            params.add(filterData.getString());
        }

        sql.append(fromWhere).append(orderBy);

        @SuppressWarnings({"unchecked"})
        final List<String> objects = hibernateTemplate.executeFind(new HibernateCallback<List<?>>() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {
                log.debug("Filter sqls: {}", sql);
                log.debug("Params: {}", params);

                StopWatch watch = new StopWatch();

                if (log.isDebugEnabled()) {
                    watch.start();
                }

                List<?> result = setParameters(session.createSQLQuery(sql.toString()), params).
                        setMaxResults(pager.getPageSize()).
                        list();

                if (log.isDebugEnabled()) {
                    watch.stop();
                    log.debug("Time spent for listing: {}", watch);
                    log.debug("Found {} objects", result.size());
                }

                return result;
            }
        });

        if (objects.isEmpty()) {
            return Collections.emptyList();
        }

        return objects;
    }

    /*

               select ie.error_key, rr.import_error_type, count(*) count
               FROM common_registry_records_tbl rr
                 inner join common_import_errors_tbl ie on rr.import_error_id = ie.id
               where registry_id = 131 and record_status_id = 2
               group by rr.import_error_type
               order by count DESC;

    */

    @Override
    public List<RecordErrorsType> findErrorsTypes(Long registryId, Collection<ObjectFilter> filters) {

        final StringBuilder sql = new StringBuilder("select ie.error_key, rr.import_error_type, count(*) count ");

        StringBuilder fromWhere = new StringBuilder(" from common_registry_records_tbl rr " +
                                                          "   inner join common_import_errors_tbl ie on rr.import_error_id = ie.id " +
                                                          " where rr.registry_id = ? ");

        StringBuilder groupOrderByClause = new StringBuilder(" group by rr.import_error_type" +
                                                             " order by count DESC");

        final List<Object> params = list();
        params.add(registryId);
        getFilters(fromWhere, params, filters);

        sql.append(fromWhere).append(groupOrderByClause);

        @SuppressWarnings({"unchecked"})
        final List<Object[]> objects = hibernateTemplate.executeFind(new HibernateCallback<List<?>>() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {
                log.debug("Filter sqls: {}", sql);
                log.debug("Params: {}", params);

                StopWatch watch = new StopWatch();

                if (log.isDebugEnabled()) {
                    watch.start();
                }

                List<?> result = setParameters(session.createSQLQuery(sql.toString()), params).list();

                if (log.isDebugEnabled()) {
                    watch.stop();
                    log.debug("Time spent for listing: {}", watch);
                    log.debug("Found {} objects", result.size());
                }

                return result;
            }
        });

        if (objects.isEmpty()) {
            return Collections.emptyList();
        }

        List<RecordErrorsType> types = list();

        for (Object[] o : objects) {
            RecordErrorsType type = new RecordErrorsType();
            type.setI18nName((String) o[0]);
            type.setErrorType((Integer) o[1]);
            type.setNumberOfRecords(((BigInteger) o[2]).longValue());
            types.add(type);
        }

        return types;
    }

    /*
            Example:

                SELECT town_name, street_type, street_name, building_number, bulk_number, first_name, middle_name, last_name, count(*)
                FROM common_registry_records_tbl c
                where registry_id = 131 and record_status_id = 2
                    and import_error_type = 4103
                group by town_name, street_type, street_name, building_number, bulk_number;
    */

    @Override
    public List<RecordErrorsGroup> findErrorsGroups(Long registryId, RecordErrorsGroupSorter sorter, Collection<ObjectFilter> filters, String groupByString, final Page<RecordErrorsGroup> pager) {

        final StringBuilder sql = new StringBuilder("select town_name, " +
                                                    "       street_type, " +
                                                    "       street_name, " +
                                                    "       building_number, " +
                                                    "       bulk_number, " +
                                                    "       apartment_number, " +
                                                    "       last_name, " +
                                                    "       first_name, " +
                                                    "       middle_name, " +
                                                    "       count(*) count ");
        final StringBuilder sqlCount = new StringBuilder("select count(*) " +
                                                         "from (" +
                                                         "   select id ");

        StringBuilder fromWhere = new StringBuilder("from common_registry_records_tbl c where registry_id = ? ");

        final List<Object> params = list();
        params.add(registryId);
        getFilters(fromWhere, params, filters);

        sql.append(fromWhere).append(groupByString);
        sqlCount.append(fromWhere).append(groupByString).append(" ) cc");
        if (sorter != null) {
            addSorting(sql, sorter);
        }

        @SuppressWarnings({"unchecked"})
        final List<Object[]> objects = hibernateTemplate.executeFind(new HibernateCallback<List<?>>() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {
                log.debug("Filter records sqls: {}\n{}", sqlCount, sql);
                log.debug("Params: {}", params);

                StopWatch watch = new StopWatch();

                if (log.isDebugEnabled()) {
                    watch.start();
                }
                Number count = (Number) setParameters(session.createSQLQuery(sqlCount.toString()), params).uniqueResult();
                if (count == null) {
                    count = 0;
                }
                log.debug("Count query: {}", count.intValue());

                if (log.isDebugEnabled()) {
                    watch.stop();
                    log.debug("Time spent for count query: {}", watch);
                    watch.reset();
                }

                pager.setTotalElements(count.intValue());

                if (log.isDebugEnabled()) {
                    watch.start();
                }
                List<?> result = setParameters(session.createSQLQuery(sql.toString()), params)
                        .setMaxResults(pager.getPageSize())
                        .setFirstResult(pager.getThisPageFirstElementNumber())
                        .list();
                if (log.isDebugEnabled()) {
                    watch.stop();
                    log.debug("Time spent for listing: {}", watch);
                    log.debug("Found {} objects", result.size());
                }

                return result;
            }
        });

        if (objects.isEmpty()) {
            return Collections.emptyList();
        }

        List<RecordErrorsGroup> groups = list();

        ImportErrorTypeFilter importErrorTypeFilter = new ImportErrorTypeFilter();

        for (ObjectFilter filter : filters) {

            if (filter instanceof ImportErrorTypeFilter) {
                importErrorTypeFilter = (ImportErrorTypeFilter) filter;
                break;
            }

        }

        for (Object[] o : objects) {
            RecordErrorsGroup group = new RecordErrorsGroup();
            group.setErrorType(importErrorTypeFilter.getSelectedType());
            group.setTownName((String) o[0]);
            group.setStreetType((String) o[1]);
            group.setStreetName((String) o[2]);
            group.setBuildingNumber((String) o[3]);
            group.setBuildingBulk((String) o[4]);
            group.setApartmentNumber((String) o[5]);
            group.setLastName((String) o[6]);
            group.setFirstName((String) o[7]);
            group.setMiddleName((String) o[8]);
            group.setNumberOfRecords(((BigInteger) o[9]).longValue());
            groups.add(group);
        }

        return groups;
    }

    private void addSorting(StringBuilder sql, RecordErrorsGroupSorter sorter) {

        sql.append(" order by ");

        if (sorter != null) {
            StringBuilder orderByClause = new StringBuilder();
            sorter.setOrderBySQL(orderByClause);

            if (orderByClause.length() > 0) {
                sql.append(orderByClause);
            }
        }
    }


    @Override
    public void updateErrorStatus(Collection<Long> recordIds, RegistryRecordStatus newStatus) {

        String idsList = StringUtils.join(recordIds, ", ");

        StopWatch watch = new StopWatch();
        if (log.isDebugEnabled()) {
            watch.start();
        }

        int updated = getSimpleJdbcTemplate().update(
                "update common_registry_records_tbl r set r.import_error_id=null, r.import_error_type=null, record_status_id=" + newStatus.getId() + " where r.id in (" + idsList + ")");

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Updated {} records", updated);
            log.debug("Time spent for disable errors query: {}", watch);
        }

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
		List<?> result = hibernateTemplate
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
    @Override
	public Long[] getMinMaxIdsForProcessing(@NotNull Long registryId, @NotNull Long restrictionMinId) {
		List<?> result = hibernateTemplate
				.findByNamedQuery("RegistryRecord.listRecordsForProcessing.stats.restriction", registryId, restrictionMinId);
		Object[] objs = (Object[]) result.get(0);

		Long[] minMax = CollectionUtils.ar((Long) objs[0], (Long) objs[1], (Long) objs[2]);
		if (minMax[0] == null || minMax[1] == null || minMax[2] == null) {
			return CollectionUtils.ar(0L, 0L, 0L);
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
		List<?> result = hibernateTemplate
				.findByNamedQuery("RegistryRecord.getMinMaxRecordsForImporting", registryId);
		Object[] objs = (Object[]) result.get(0);

		Long[] minMax = CollectionUtils.ar((Long) objs[0], (Long) objs[1]);
		if (minMax[0] == null || minMax[1] == null) {
			return CollectionUtils.ar(0L, 0L);
		}

		return minMax;
	}

    @Required
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
