package org.flexpay.eirc.dao.impl;

import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.dao.FilterHandler;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.dao.EircAccountDao;
import org.flexpay.eirc.dao.EircAccountDaoExt;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.sorter.EircAccountSorter;
import org.flexpay.eirc.persistence.sorter.EircAccountSorterStub;
import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class EircAccountDaoExtImpl extends HibernateDaoSupport implements EircAccountDaoExt {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private List<FilterHandler> filterHandlers = FilterHandlers.eircAccountFilterHandlers();
    private EircAccountDao eircAccountDao;

	/**
	 * Find EIRC account by person and apartment identifiers
	 *
	 * @param personId	Person key
	 * @param apartmentId Apartment key
	 * @return EircAccount instance if found, or <code>null</code> otherwise
	 */
    @Override
	public EircAccount findAccount(@NotNull Long personId, @NotNull Long apartmentId) {
		Object[] params = {personId, apartmentId};
		List<?> accounts = getHibernateTemplate().findByNamedQuery("EircAccount.findByPersonAndApartment", params);
		if (accounts.isEmpty()) {
			return null;
		}

		return (EircAccount) accounts.get(0);
	}

	/**
	 * Find EIRC account by apartment identifiers
	 *
	 * @param apartmentId Apartment key
	 * @return EircAccount instance if found, or <code>null</code> otherwise
	 */
    @Override
	public EircAccount findAccount(@NotNull Long apartmentId) {
		List<?> accounts = getHibernateTemplate().findByNamedQuery("EircAccount.findByApartmentWithConsumerInfo", apartmentId);
		if (accounts.isEmpty()) {
			return null;
		}

		return (EircAccount) accounts.get(0);
	}

    @Override
    public EircAccount readAccountNotFull(@NotNull Long accountId) {
        List<?> accounts = getHibernateTemplate().findByNamedQuery("EircAccount.read", accountId);
        if (accounts.isEmpty()) {
            return null;
        }

        return (EircAccount) accounts.get(0);
    }

    @SuppressWarnings({"unchecked"})
    private List<EircAccount> getAllEircAccounts(@NotNull Collection<? extends EircAccountSorter> sorters,
                                                 @NotNull Collection<ObjectFilter> filters, final Page<EircAccount> pager) {
        final List<Object> params = list();
        EircAccountSorter sorter = findSorter(sorters);

        final StringBuilder hqlCount = new StringBuilder("select count(a) from EircAccount a " +
                        "left join a.apartment apartment " +
                        "left join a.consumerInfo ci " +
                        "left join a.person p " +
                        "left join p.personIdentities pi ");
        final StringBuilder hql = new StringBuilder("select distinct a from EircAccount a " +
                        "left join fetch a.apartment apartment " +
                        "left join fetch a.consumerInfo ci " +
                        "left join a.person p " +
                        "left join p.personIdentities pi ");

        for (ObjectFilter filter : filters) {
            if (filter.needFilter()) {
                if (filter instanceof BuildingsFilter) {
                    hqlCount.append("inner join apartment.building b " +
                                    "left join b.buildingses buildingses ");
                    hql.append("inner join fetch apartment.building b " +
                                    "left join b.buildingses buildingses ");
                } else if (filter instanceof StreetFilter) {
                    hqlCount.append("inner join apartment.building b " +
                                    "left join b.buildingses buildingses " +
                                    "left join buildingses.street street ");
                    hql.append("inner join fetch apartment.building b " +
                                    "left join b.buildingses buildingses " +
                                    "left join buildingses.street street ");
                } else if (filter instanceof TownFilter) {
                    hqlCount.append("inner join apartment.building b " +
                                    "left join b.buildingses buildingses " +
                                    "left join buildingses.street street " +
                                    "left join street.parent town ");
                    hql.append("inner join fetch apartment.building b " +
                                    "left join b.buildingses buildingses " +
                                    "left join buildingses.street street " +
                                    "left join street.parent town ");
                }
            }
        }

        sorter.setFrom(hql);

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(" where a.status=").append(ObjectWithStatus.STATUS_ACTIVE);
        sorter.setWhere(whereClause);
        hql.append(whereClause);
        hqlCount.append(" where a.status=").append(ObjectWithStatus.STATUS_ACTIVE);

        for (ObjectFilter filter : filters) {
            if (!filter.needFilter()) {
                continue;
            }
            for (FilterHandler handler : filterHandlers) {
                if (handler.supports(filter)) {
                    hql.append(" and ");
                    hqlCount.append(" and ");
                    List<?> parameters = handler.whereClause(filter, hqlCount);
                    handler.whereClause(filter, hql);
                    params.addAll(parameters);
                }
            }
        }

        StringBuilder orderByClause = new StringBuilder();
        sorter.setOrderBy(orderByClause);
        if (orderByClause.length() > 0) {
            hql.append(" order by ").append(orderByClause);
        }

        return getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {

                Query qCount = session.createQuery(hqlCount.toString());
                Query query = session.createQuery(hql.toString());
                for (int n = 0; n < params.size(); ++n) {
                    qCount.setParameter(n, params.get(n));
                    query.setParameter(n, params.get(n));
                }

                Number objectsCount = (Number) qCount.uniqueResult();
                pager.setTotalElements(objectsCount.intValue());

                return query.setFirstResult(pager.getThisPageFirstElementNumber())
                        .setMaxResults(pager.getPageSize())
                        .list();

            }
        });

    }

/*
 select count.ea_id
from (
  select count(*) c, sum.ea_id ea_id
  from (

select distinct sum((ifnull(bool_value,0) + UNIX_TIMESTAMP(ifnull(date_value,'1970-01-01 00:00:00')) + ifnull(int_value,0) + ifnull(long_value,0) + convert(ifnull(string_value,'0'),SIGNED) + ifnull(double_value,0) + ifnull(decimal_value,0))*type_id) s,
   a.id ea_id, a.account_number account_number, ci.town_type town_type, ci.town_name town_name, ci.street_type_name street_type_name, ci.street_name street_name, ci.building_number building_number, ci.building_bulk building_bulk, ci.apartment_number apartment_number
from eirc_eirc_accounts_tbl a
    inner join eirc_consumer_infos_tbl ci on ci.id=a.consumer_info_id and (ci.status=0)
    left outer join eirc_consumers_tbl c on a.id=c.eirc_account_id
    left outer join eirc_consumer_attributes_tbl ca on ca.consumer_id=c.id
    left outer join eirc_consumer_attribute_types_tbl t on ca.type_id=t.id
    left outer join ab_apartments_tbl apartment on apartment.id=a.apartment_id
    inner join ab_buildings_tbl b on b.id=apartment.building_id
    left outer join ab_building_addresses_tbl buildingses on b.id=buildingses.building_id and (buildingses.status=0)
    left outer join ab_streets_tbl street on street.id=buildingses.street_id and (buildingses.status=0)
where a.status=0 and c.end_date = '2100-12-31' and ca.end_date = '2100-12-31'
  and street.id=1 and t.unique_code in ('ATTR_NUMBER_TENANTS', 'ATTR_NUMBER_REGISTERED_TENANTS', 'ATTR_TOTAL_SQUARE', 'ATTR_LIVE_SQUARE', 'ATTR_HEATING_SQUARE')
group by ca.consumer_id
  ) sum
  group by sum.ea_id
  order by upper(ifnull(sum.town_type,'')),
    upper(ifnull(sum.town_name,'')),
    upper(ifnull(sum.street_type_name,'')),
    upper(ifnull(sum.street_name,'')),
    lpad(convert(ifnull(sum.building_number, '0'), UNSIGNED), 10, '0'),
    lpad(convert(ifnull(sum.building_bulk, '0'), UNSIGNED), 10, '0'),
    lpad(convert(ifnull(sum.apartment_number, '0'), UNSIGNED), 10, '0') desc
) count
where count.c > 1

 */
    @SuppressWarnings({"unchecked"})
    private List<EircAccount> getEircAccountsWithDifferences(@NotNull Collection<? extends EircAccountSorter> sorters,
                                                             @NotNull Collection<ObjectFilter> filters, final Page<EircAccount> pager) {

        final List<Object> params = list();
        EircAccountSorter sorter = findSorter(sorters);

        final StringBuilder sql = new StringBuilder("select count.ea_id from (select count(*) c, sum.ea_id ea_id from ( ");
        final StringBuilder sqlCount = new StringBuilder("select count(count.ea_id) from (select count(*) c, sum.ea_id ea_id from ( ");

        final StringBuilder innerSql = new StringBuilder("select distinct sum((ifnull(bool_value,0) + ifnull(int_value,0) + ifnull(long_value,0) + " +
                                                                            "convert(ifnull(string_value,'0'),SIGNED) + ifnull(double_value,0) + " +
                                                                            "ifnull(decimal_value,0) + UNIX_TIMESTAMP(ifnull(date_value,'1970-01-01 00:00:00')))*type_id) s, " +
                                                                "a.id ea_id, a.account_number account_number, " +
                                                                "ci.town_type town_type, ci.town_name town_name, ci.street_type_name street_type_name, " +
                                                                "ci.street_name street_name, ci.building_number building_number, ci.building_bulk building_bulk, " +
                                                                "ci.apartment_number apartment_number " +
                                                        "from eirc_eirc_accounts_tbl a " +
                                                        "    inner join eirc_consumer_infos_tbl ci on ci.id=a.consumer_info_id and (ci.status=0)" + 
                                                        "    left outer join eirc_consumers_tbl c on a.id=c.eirc_account_id " +
                                                        "    left outer join eirc_consumer_attributes_tbl ca on ca.consumer_id=c.id " +
                                                        "    left outer join eirc_consumer_attribute_types_tbl t on ca.type_id=t.id " +
                                                        "    left outer join ab_apartments_tbl apartment on apartment.id=a.apartment_id ");

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(" where a.status=").append(ObjectWithStatus.STATUS_ACTIVE).append(" and c.end_date = '2100-12-31' and ca.end_date = '2100-12-31' ");

        for (ObjectFilter filter : filters) {

            if (filter.needFilter()) {

                if (filter instanceof BuildingsFilter) {
                    innerSql.append("inner join ab_buildings_tbl b on b.id=apartment.building_id " +
                               "left outer join ab_building_addresses_tbl buildingses on b.id=buildingses.building_id and (buildingses.status=0) ");
                } else if (filter instanceof StreetFilter) {
                    innerSql.append("inner join ab_buildings_tbl b on b.id=apartment.building_id " +
                               "left outer join ab_building_addresses_tbl buildingses on b.id=buildingses.building_id and (buildingses.status=0) " +
                               "left outer join ab_streets_tbl street on street.id=buildingses.street_id and (buildingses.status=0) "
                    );
                } else if (filter instanceof TownFilter) {
                    innerSql.append("inner join ab_buildings_tbl b on b.id=apartment.building_id " +
                               "left outer join ab_building_addresses_tbl buildingses on b.id=buildingses.building_id and (buildingses.status=0) " +
                               "left outer join ab_streets_tbl street on street.id=buildingses.street_id and (buildingses.status=0) " +
                               "left outer join ab_towns_tbl town on town.id=street.town_id "
                    );
                }
            }
        }

        innerSql.append(whereClause);

        for (ObjectFilter filter : filters) {
            if (!filter.needFilter()) {
                continue;
            }
            for (FilterHandler handler : filterHandlers) {
                if (handler.supports(filter)) {
                    innerSql.append(" and ");
                    List<?> parameters = handler.whereClause(filter, innerSql);
                    params.addAll(parameters);
                }
            }
        }

        innerSql.append(" and t.unique_code in (:type_codes) ").append(" group by ca.consumer_id ");

        sqlCount.append(innerSql).append(" ) sum group by sum.ea_id ) count where count.c > 1 ");
        sql.append(innerSql).append(" ) sum group by sum.ea_id");

        StringBuilder orderByClause = new StringBuilder();
        sorter.setOrderBySQL(orderByClause);
        if (orderByClause.length() > 0) {
            sql.append(" order by ").append(orderByClause);
        }
        sql.append(" ) count where count.c > 1 ");

        log.debug("sql = {}", sql);

        List<Number> ids = getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {

                Query qCount = session.createSQLQuery(sqlCount.toString());
                Query query = session.createSQLQuery(sql.toString());

                for (int n = 0; n < params.size(); ++n) {
                    qCount.setParameter(n, params.get(n));
                    query.setParameter(n, params.get(n));
                }

                query.setParameterList("type_codes", ConsumerAttributes.EIRC_ATTRIBUTES);
                qCount.setParameterList("type_codes", ConsumerAttributes.EIRC_ATTRIBUTES);

                Number objectsCount = (Number) qCount.uniqueResult();
                pager.setTotalElements(objectsCount.intValue());

                return query.setFirstResult(pager.getThisPageFirstElementNumber()).
                        setMaxResults(pager.getPageSize()).
                        list();
            }
        });

        List<Long> idsLong = list();
        for (Number id : ids) {
            idsLong.add(id.longValue());
        }

        log.debug("ids = {}", idsLong);

        return eircAccountDao.readFullCollection(idsLong, true);
    }

    @SuppressWarnings ({"unchecked"})
    @NotNull
    @Override
    public List<EircAccount> findAccounts(@NotNull Collection<? extends EircAccountSorter> sorters, @NotNull Collection<ObjectFilter> filters,
                                                   @NotNull Integer output, final Page<EircAccount> pager) {

        if (output == 0) {
            return getAllEircAccounts(sorters, filters, pager);
        } else {
            return getEircAccountsWithDifferences(sorters, filters, pager);
        }
    }

    @NotNull
    private EircAccountSorter findSorter(Collection<? extends EircAccountSorter> sorters) {

        for (EircAccountSorter sorter : sorters) {
            if (sorter.isActivated()) {
                return sorter;
            }
        }

        return new EircAccountSorterStub();
    }

    @Required
    public void setEircAccountDao(EircAccountDao eircAccountDao) {
        this.eircAccountDao = eircAccountDao;
    }
}
