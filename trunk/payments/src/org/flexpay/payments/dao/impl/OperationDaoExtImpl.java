package org.flexpay.payments.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.payments.dao.OperationDaoExt;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.filters.CashboxTradingDayFilter;
import org.flexpay.payments.persistence.filters.MaximalSumFilter;
import org.flexpay.payments.persistence.filters.MinimalSumFilter;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Date;
import java.util.List;

public class OperationDaoExtImpl extends HibernateDaoSupport implements OperationDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

    @SuppressWarnings({"unchecked"})
    @Override
    public List<Operation> searchDocuments(OperationSorter operationSorter, @NotNull final ArrayStack filters, final Page<Operation> pager) {

        final StringBuilder hql = new StringBuilder("SELECT DISTINCT o FROM Operation o LEFT JOIN o.documents doc");
        final StringBuilder cntHql = new StringBuilder("SELECT COUNT(o) FROM Operation o LEFT JOIN o.documents doc");
        final StringBuilder filterHql = getCashboxDocumentSearchHql(filters);

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

        return (List<Operation>) getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public List<Operation> doInHibernate(Session session) throws HibernateException {

                Query cntQuery = session.createQuery(cntHql.toString());
                setCashboxDocumentSearchQueryParameters(cntQuery, filters);
                Long count = (Long) cntQuery.uniqueResult();
                pager.setTotalElements(count.intValue());

                Query query = session.createQuery(hql.toString());
                setCashboxDocumentSearchQueryParameters(query, filters);
                query.setFirstResult(pager.getThisPageFirstElementNumber());
                query.setMaxResults(pager.getPageSize());

                return (List<Operation>) query.list();
            }
        });
    }

    private StringBuilder getCashboxDocumentSearchHql(@NotNull ArrayStack filters) {
        StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		// opeartion status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3");
        filterHql.append(" AND doc.documentStatus.code <> 3");

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;

            if (filter.needFilter()) {

                if (filter instanceof PaymentPointFilter) {
                    filterHql.append(" AND o.paymentPoint.id = :paymentPointId");
                } else if (filter instanceof CashboxFilter) {
                    filterHql.append(" AND o.cashbox.id = :cashboxId");
                } else if (filter instanceof ServiceTypeFilter) {
                    filterHql.append(" AND doc.service.serviceType.id = :serviceTypeId");
                } else if (filter instanceof BeginDateFilter) {
                    filterHql.append(" AND o.creationDate >= :begin");
                } else if (filter instanceof EndDateFilter) {
                    filterHql.append(" AND o.creationDate <= :end");
                } else if (filter instanceof MinimalSumFilter) {
                    filterHql.append(" AND doc.sum >= :minimalSum");
                } else if (filter instanceof MaximalSumFilter) {
                    filterHql.append(" AND doc.sum <= :maximalSum");
                }

            }

        }

		return filterHql;
	}

	private void setCashboxDocumentSearchQueryParameters(Query query, @NotNull ArrayStack filters) {

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;
            if (filter.needFilter()) {

                if (filter instanceof PaymentPointFilter) {
                    query.setLong("paymentPointId", ((PaymentPointFilter) filter).getSelectedId());
                } else if (filter instanceof CashboxFilter) {
                    query.setLong("cashboxId", ((CashboxFilter) filter).getSelectedId());
                } else if (filter instanceof ServiceTypeFilter) {
                    query.setLong("serviceTypeId", ((ServiceTypeFilter) filter).getSelectedId());
                } else if (filter instanceof BeginDateFilter) {
                    query.setTimestamp("begin", ((BeginDateFilter) filter).getDate());
                } else if (filter instanceof EndDateFilter) {
                    query.setTimestamp("end", ((EndDateFilter) filter).getDate());
                } else if (filter instanceof MinimalSumFilter) {
                    query.setBigDecimal("minimalSum", ((MinimalSumFilter) filter).getBdValue());
                } else if (filter instanceof MaximalSumFilter) {
                    query.setBigDecimal("maximalSum", ((MaximalSumFilter) filter).getBdValue());
                }

            }
        }
	}

    @SuppressWarnings ({"unchecked"})
	@Override
    public List<Operation> searchOperations(OperationSorter operationSorter, @NotNull final ArrayStack filters, final Page<Operation> pager) {
        final StringBuilder hql = new StringBuilder("SELECT DISTINCT o FROM Operation o");
		final StringBuilder cntHql = new StringBuilder("SELECT COUNT(o) FROM Operation o");
		final StringBuilder filterHql = getCashboxOperationSearchHql(filters);

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

		return (List<Operation>) getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<Operation> doInHibernate(Session session) throws HibernateException {

				Query cntQuery = session.createQuery(cntHql.toString());
				setCashboxOperationSearchQueryParameters(cntQuery, filters);
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				Query query = session.createQuery(hql.toString());
				setCashboxOperationSearchQueryParameters(query, filters);
                log.debug("Operations search query: {}", query);
				return (List<Operation>) query.setFirstResult(pager.getThisPageFirstElementNumber()).
                        setMaxResults(pager.getPageSize()).
                        list();
			}
		});
    }

    private StringBuilder getCashboxOperationSearchHql(@NotNull ArrayStack filters) {

        StringBuilder joinHql = new StringBuilder("");

		StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		// status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3");

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;

            if (filter.needFilter()) {

                if (filter instanceof PaymentPointFilter) {
                    filterHql.append(" AND o.paymentPoint.id = :paymentPointId");
                } else if (filter instanceof CashboxFilter) {
                    filterHql.append(" AND o.cashbox.id = :cashboxId");
                } else if (filter instanceof BeginDateFilter) {
                    filterHql.append(" AND o.creationDate >= :begin");
                } else if (filter instanceof EndDateFilter) {
                    filterHql.append(" AND o.creationDate <= :end");
                } else if (filter instanceof MinimalSumFilter) {
                    filterHql.append(" AND o.operationSum >= :minimalSum");
                } else if (filter instanceof MaximalSumFilter) {
                    filterHql.append(" AND o.operationSum <= :maximalSum");
                } else if (filter instanceof CashboxTradingDayFilter) {
                    joinHql.append(" left join o.cashbox c");
                    filterHql.append(" AND c.tradingDayProcessInstanceId = :tradingDayProcessId");
                }

            }

        }

		return joinHql.append(filterHql);
	}

    private void setCashboxOperationSearchQueryParameters(Query query, @NotNull ArrayStack filters) {

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;
            if (filter.needFilter()) {

                if (filter instanceof PaymentPointFilter) {
                    query.setLong("paymentPointId", ((PaymentPointFilter) filter).getSelectedId());
                } else if (filter instanceof CashboxFilter) {
                    query.setLong("cashboxId", ((CashboxFilter) filter).getSelectedId());
                } else if (filter instanceof BeginDateFilter) {
                    query.setTimestamp("begin", ((BeginDateFilter) filter).getDate());
                } else if (filter instanceof EndDateFilter) {
                    query.setTimestamp("end", ((EndDateFilter) filter).getDate());
                } else if (filter instanceof MinimalSumFilter) {
                    query.setBigDecimal("minimalSum", ((MinimalSumFilter) filter).getBdValue());
                } else if (filter instanceof MaximalSumFilter) {
                    query.setBigDecimal("maximalSum", ((MaximalSumFilter) filter).getBdValue());
                } else if (filter instanceof CashboxTradingDayFilter) {
                    query.setLong("tradingDayProcessId", ((CashboxTradingDayFilter) filter).getSelectedId());
                }

            }
        }

	}

    @SuppressWarnings({"unchecked"})
    @Override
    public Operation getLastPaymentPointPaymentOperation(final Long paymentPointId, final Date beginDate, final Date endDate) {
        List<Operation> result = getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public List<Operation> doInHibernate(Session session) throws HibernateException {
                return session.getNamedQuery("Operation.listLastPaymentPointPaymentOperations").
                        setLong(0, paymentPointId).
                        setTimestamp(1, beginDate).
                        setTimestamp(2, endDate).
                        setMaxResults(1).list();
            }
        });

        return result.isEmpty() ? null : result.get(0);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Operation getLastCashboxPaymentOperation(final Long cashboxId, final Date beginDate, final Date endDate) {
        List<Operation> result = getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public List<Operation> doInHibernate(Session session) throws HibernateException {
                return session.getNamedQuery("Operation.listLastCashboxPaymentOperations").
                        setLong(0, cashboxId).
                        setTimestamp(1, beginDate).
                        setTimestamp(2, endDate).
                        setMaxResults(1).list();
            }
        });

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
	public Long getBlankOperationsCount() {
		return DataAccessUtils.longResult(getHibernateTemplate().find("SELECT COUNT(o) FROM Operation o WHERE o.operationStatus.code = 6"));
	}

	@Override
	public void deleteAllBlankOperations() {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				session.getNamedQuery("Operation.deleteAllBlankOperations").setLong(0, 6).executeUpdate();
				return null;
			}
		});
	}
}
