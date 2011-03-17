package org.flexpay.ab.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.StreetSorter;
import org.flexpay.ab.persistence.sorter.StreetSorterStub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.filter.StringValueFilter.*;

public class StreetDaoExtImpl extends HibernateDaoSupport implements StreetDaoExt {

    private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String QUERY_ALL = "%%";

	/**
	 * Find and sort streets
	 *
	 * @param townId  Town key
	 * @param sorters Collection of sorters
	 * @param query query
	 * @param languageId language id for search
	 * @param pager   Pager
	 * @return List of streets
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<Street> findByParentAndQuery(Long townId, Collection<? extends ObjectSorter> sorters, String query, Long languageId, final Page<Street> pager) {
		StreetSorter sorter = findSorter(sorters);
		sorter.setStreetField("s");

		final StringBuilder cnthql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cnthql.append("select count(s) from Street s ").
				append(" left join s.nameTemporals t").
				append(" left join t.value.translations tr").
                append(" left join s.typeTemporals tt");
		hql.append("select distinct s from Street s ").
				append(" left join fetch s.nameTemporals t").
				append(" left join fetch t.value v").
				append(" left join fetch v.translations tr").
                append(" left join fetch s.typeTemporals tt");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where s.parent.id=").append(townId).
                append(" and s.status=").append(Street.STATUS_ACTIVE).
                append(" and t.end='2100-12-31'").
                append(" and (tt is null or tt.end ='2100-12-31')");
		if (!QUERY_ALL.equals(query)) {
			whereClause.append(" and upper(tr.name) like '").append(query).append("'").append(" and tr.lang.id=").append(languageId);
		}
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cnthql.append(" where s.parent.id=").append(townId).
                append(" and s.status=").append(Street.STATUS_ACTIVE).
                append(" and t.end='2100-12-31'").
                append(" and (tt is null or tt.end ='2100-12-31')");
		if (!QUERY_ALL.equals(query)) {
			cnthql.append(" and upper(tr.name) like '").append(query).append("'").append(" and tr.lang.id=").append(languageId);
		}

		StringBuilder orderByClause = new StringBuilder();
		sorter.setOrderBy(orderByClause);
		if (orderByClause.length() > 0) {
			hql.append(" ORDER BY ").append(orderByClause);
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query cntQuery = session.createQuery(cnthql.toString());
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				return session.createQuery(hql.toString())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.list();

			}
		});
	}

	@NotNull
	private StreetSorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof StreetSorter) {
				return (StreetSorter) sorter;
			}
		}

		return new StreetSorterStub();
	}

	@Override
	public void deleteStreetDistricts(final Street street) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Long streetId = street.getId();
				if (streetId == null || streetId <= 0) {
					return null;
				}
				session.getNamedQuery("Street.deleteStreetDistricts")
						.setLong(0, streetId).executeUpdate();
				return null;
			}
		});

	}

	@Override
	public void deleteStreet(final Street street) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Long streetId = street.getId();
				if (streetId == null || streetId <= 0) {
					return null;
				}
				session.getNamedQuery("Street.deleteNameTranslations")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteNameTemporals")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteNames")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteTypeTemporals")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteStreetDistricts")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteStreet")
						.setLong(0, streetId).executeUpdate();
				return null;
			}
		});
	}

/*
SELECT street.id, streetTypeTrans.name, streetTypeTrans.short_name, streetNameTrans.name, townTypeTrans.name, townTypeTrans.short_name, townNameTrans.name FROM ab_streets_tbl street

    left outer join ab_street_types_temporal_tbl streetTypeTemp on streetTypeTemp.street_id=street.id
    left outer join ab_street_types_tbl streetType on streetType.id=streetTypeTemp.street_type_id
    left outer join ab_street_type_translations_tbl streetTypeTrans on streetTypeTrans.id=streetType.id

    left outer join ab_street_names_tbl streetName on street.id=streetName.street_id
    left outer join ab_street_names_temporal_tbl streetNameTemp on street.id=streetNameTemp.street_id
    left outer join ab_street_name_translations_tbl streetNameTrans on streetName.id=streetNameTrans.street_name_id

    inner join ab_towns_tbl town on town.id=street.town_id

    left outer join ab_town_names_tbl townName on town.id=townName.town_id
    left outer join ab_town_names_temporal_tbl townNameTemp on town.id=townNameTemp.town_id
    left outer join ab_town_name_translations_tbl townNameTrans on townName.id=townNameTrans.town_name_id

WHERE street.status = 0
    and townNameTemp.end_date = '2100-12-31' and lower(townNameTrans.name) like "харьков"
    and streetTypeTemp.end_date = '2100-12-31' and lower(streetTypeTrans.name) like "просп"
    and streetNameTemp.end_date = '2100-12-31' and lower(streetNameTrans.name) like "косиора"
    ;
*/
/*
    select distinct street
    from Street street

        left join fetch street.typeTemporals stt
        left join fetch stt.value stv
        left join fetch stv.translations sttr

        left join fetch street.nameTemporals snt
        left join fetch snt.value sntv
        left join fetch sntv.translations str

        inner join fetch street.parent town

        left join fetch town.nameTemporals tnt
        left join fetch tnt.value tntv
        left join fetch tntv.translations ttr

    where street.status=0
          and street.parent.id=?
          and tnt.invalidDate='2100-12-31' and lower(ttr.name) like ?
          and stt.invalidDate='2100-12-31' and lower(sttr.name) like ?
          and snt.invalidDate='2100-12-31' and lower(str.name) like ?
*/

    @SuppressWarnings({"unchecked"})
    @Nullable
    @Override
    public Street findStreet(@NotNull final ArrayStack filters) {

        final StringBuilder hql = new StringBuilder("SELECT DISTINCT street FROM Street street");

        final StringBuilder filterHql = getFilterHql(filters, hql);

        hql.append(filterHql);

        if (log.isDebugEnabled()) {
            for (Object f : filters) {
                ObjectFilter filter = (ObjectFilter) f;

                if (filter.needFilter()) {

                    if (filter instanceof TownFilter) {
                        log.debug("townId = {}", ((TownFilter) filter).getSelectedId());
                    } else if (filter instanceof StringValueFilter) {

                        StringValueFilter svFilter = (StringValueFilter) filter;

                        if (TYPE_TOWN.equals(svFilter.getType())) {
                            log.debug("townName = {}", svFilter.getValue());
                        } else if (TYPE_STREET_TYPE.equals(svFilter.getType())) {
                            log.debug("streetTypeName = {}", svFilter.getValue());
                        } else if (TYPE_STREET.equals(svFilter.getType())) {
                            log.debug("streetName = {}", svFilter.getValue());
                        }
                    }

                }

            }
        }

        List<Street> streets = (List<Street>) getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public List<Street> doInHibernate(Session session) throws HibernateException {

                Query query = session.createQuery(hql.toString());
                setQueryParameters(query, filters);
                log.debug("Streets search query: {}", query);
                return (List<Street>) query.list();
            }
        });

        if (log.isDebugEnabled()) {
            log.debug("Found {} streets", streets.size());
        }

        return streets.isEmpty() ? null : streets.get(0);
    }

    private void setQueryParameters(Query query, @NotNull ArrayStack filters) {

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;

            if (filter.needFilter()) {

                if (filter instanceof TownFilter) {
                    query.setLong("townId", ((TownFilter) filter).getSelectedId());
                } else if (filter instanceof StringValueFilter) {

                    StringValueFilter svFilter = (StringValueFilter) filter;

                    if (TYPE_TOWN.equals(svFilter.getType())) {
                        query.setString("townName", svFilter.getValue().toLowerCase());
                    } else if (TYPE_STREET_TYPE.equals(svFilter.getType())) {
                        query.setString("streetTypeName", svFilter.getValue().toLowerCase());
                    } else if (TYPE_STREET.equals(svFilter.getType())) {
                        query.setString("streetName", svFilter.getValue().toLowerCase());
                    }
                }

            }
        }

    }

    private StringBuilder getFilterHql(@NotNull ArrayStack filters, StringBuilder hql) {

         StringBuilder filterHql = new StringBuilder(" WHERE street.status=").append(ObjectWithStatus.STATUS_ACTIVE);

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;

            if (filter.needFilter()) {

                if (filter instanceof TownFilter) {
                    filterHql.append(" AND street.parent.id=:townId");
                } else if (filter instanceof StringValueFilter) {

                    StringValueFilter svFilter = (StringValueFilter) filter;

                    if (TYPE_TOWN.equals(svFilter.getType())) {
                        hql.append(" inner join fetch street.parent town" +
                                   " left join fetch town.nameTemporals tnt" +
                                   " left join fetch tnt.value tntv" +
                                   " left join fetch tntv.translations ttr");
                        filterHql.append(" AND tnt.invalidDate='2100-12-31' AND lower(ttr.name) like :townName");
                    } else if (TYPE_STREET_TYPE.equals(svFilter.getType())) {
                        hql.append(" left join fetch street.typeTemporals stt" +
                                   " left join fetch stt.value stv" +
                                   " left join fetch stv.translations sttr");
                        filterHql.append(" AND stt.invalidDate='2100-12-31' AND lower(sttr.name) like :streetTypeName");
                    } else if (TYPE_STREET.equals(svFilter.getType())) {
                        hql.append(" left join fetch street.nameTemporals snt" +
                                   " left join fetch snt.value sntv" +
                                   " left join fetch sntv.translations str");
                        filterHql.append(" AND snt.invalidDate='2100-12-31' AND lower(str.name) like :streetName");
                    }
                }

            }

        }

        return filterHql;

    }

}
