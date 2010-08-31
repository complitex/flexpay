package org.flexpay.eirc.dao.impl;

import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.common.dao.FilterHandler;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.dao.EircAccountDaoExt;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.sorter.EircAccountSorter;
import org.flexpay.eirc.persistence.sorter.EircAccountSorterStub;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class EircAccountDaoExtImpl extends HibernateDaoSupport implements EircAccountDaoExt {

    private List<FilterHandler> filterHandlers = FilterHandlers.eircAccountFilterHandlers();

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

    @SuppressWarnings ({"unchecked"})
    @NotNull
    @Override
    public List<EircAccount> findAccounts(@NotNull Collection<? extends EircAccountSorter> sorters, @NotNull Collection<ObjectFilter> filters,
                                                   @NotNull Integer output, final Page<EircAccount> pager) {

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
            if (filter.needFilter() && filter instanceof BuildingsFilter) {
                hqlCount.append("inner join apartment.building b " +
                                "left join b.buildingses buildingses ");
                hql.append("inner join fetch apartment.building b " +
                                "left join b.buildingses buildingses ");
                break;
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

    @NotNull
    private EircAccountSorter findSorter(Collection<? extends EircAccountSorter> sorters) {

        for (EircAccountSorter sorter : sorters) {
            if (sorter.isActivated()) {
                return sorter;
            }
        }

        return new EircAccountSorterStub();
    }

}
