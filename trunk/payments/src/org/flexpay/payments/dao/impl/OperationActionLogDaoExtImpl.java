package org.flexpay.payments.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.payments.dao.OperationActionLogDaoExt;
import org.flexpay.payments.persistence.OperationActionLog;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.hibernate.HibernateException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.List;

public class OperationActionLogDaoExtImpl extends JpaDaoSupport implements OperationActionLogDaoExt {

    private Logger log = LoggerFactory.getLogger(getClass());

    @SuppressWarnings ({"unchecked"})
    @Override
    public List<OperationActionLog> searchOperationActionLogs(OperationSorter operationSorter, @NotNull final ArrayStack filters, final Page<OperationActionLog> pager) {

        final StringBuilder hql = new StringBuilder("SELECT DISTINCT o FROM OperationActionLog o");
		final StringBuilder cntHql = new StringBuilder("SELECT COUNT(o) FROM OperationActionLog o");
		final StringBuilder filterHql = getOperationActionLogsSearchHql(filters);

		hql.append(filterHql);
		cntHql.append(filterHql);

        if (operationSorter != null) {
            hql.append(" order by ");
            StringBuilder orderByClause = new StringBuilder();
            operationSorter.setOrderBy(orderByClause);

            if (orderByClause.length() > 0) {
                hql.append(orderByClause);
            }
        }

        log.debug("Search operation action logs query = {}", hql.toString());

		return (List<OperationActionLog>) getJpaTemplate().executeFind(new JpaCallback<List<OperationActionLog>>() {
			@Override
			public List<OperationActionLog> doInJpa(EntityManager entityManager) throws HibernateException {

                Query cntQuery = entityManager.createQuery(cntHql.toString());
				setOperationActionLogsSearchQueryParameters(cntQuery, filters);
				Long count = (Long) cntQuery.getSingleResult();
				pager.setTotalElements(count.intValue());

				Query query = entityManager.createQuery(hql.toString());
				setOperationActionLogsSearchQueryParameters(query, filters);

				return (List<OperationActionLog>) query.setFirstResult(pager.getThisPageFirstElementNumber()).
                        setMaxResults(pager.getPageSize()).
                        getResultList();
			}
		});
    }

    private void setOperationActionLogsSearchQueryParameters(Query query, @NotNull ArrayStack filters) {

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;
            if (filter.needFilter()) {

                if (filter instanceof PaymentCollectorFilter) {
                    query.setParameter("paymentCollectorId", ((PaymentCollectorFilter) filter).getSelectedId());
                } else if (filter instanceof PaymentPointFilter) {
                    query.setParameter("paymentPointId", ((PaymentPointFilter) filter).getSelectedId());
                } else if (filter instanceof CashboxFilter) {
                    query.setParameter("cashboxId", ((CashboxFilter) filter).getSelectedId());
                } else if (filter instanceof BeginDateFilter) {
                    query.setParameter("begin", ((BeginDateFilter) filter).getDate(), TemporalType.TIMESTAMP);
                } else if (filter instanceof EndDateFilter) {
                    query.setParameter("end", ((EndDateFilter) filter).getDate(), TemporalType.TIMESTAMP);
                }

            }
        }

	}

    private StringBuilder getOperationActionLogsSearchHql(@NotNull ArrayStack filters) {

        StringBuilder joinHql = new StringBuilder(" left join o.cashbox c left join c.paymentPoint p");

		StringBuilder filterHql = new StringBuilder(" WHERE");

        boolean isNotFirst = false;

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;

            if (filter.needFilter()) {

                if (isNotFirst) {
                    filterHql.append(" AND ");
                } else {
                    isNotFirst = true;
                }

                if (filter instanceof PaymentCollectorFilter) {
                    filterHql.append(" p.collector.id = :paymentCollectorId");
                } else if (filter instanceof PaymentPointFilter) {
                    filterHql.append(" p.id = :paymentPointId");
                } else if (filter instanceof CashboxFilter) {
                    filterHql.append(" o.cashbox.id = :cashboxId");
                } else if (filter instanceof BeginDateFilter) {
                    filterHql.append(" o.actionDate >= :begin");
                } else if (filter instanceof EndDateFilter) {
                    filterHql.append(" o.actionDate <= :end");
                }

            }

        }

		return joinHql.append(filterHql);
	}

}
