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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public class OperationDaoExtImpl extends JpaDaoSupport implements OperationDaoExt {

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

        return (List<Operation>) getJpaTemplate().executeFind(new JpaCallback<List<Operation>>() {
			@Override
			public List<Operation> doInJpa(EntityManager entityManager) throws HibernateException {

                javax.persistence.Query cntQuery = entityManager.createQuery(cntHql.toString());
                setCashboxDocumentSearchQueryParameters(cntQuery, filters);
                Long count = (Long) cntQuery.getSingleResult();
                pager.setTotalElements(count.intValue());

                javax.persistence.Query query = entityManager.createQuery(hql.toString());
                setCashboxDocumentSearchQueryParameters(query, filters);
                query.setFirstResult(pager.getThisPageFirstElementNumber());
                query.setMaxResults(pager.getPageSize());

                return (List<Operation>) query.getResultList();
            }
        });
    }

    private StringBuilder getCashboxDocumentSearchHql(@NotNull ArrayStack filters) {
        StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		// opeartion status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3 and o.operationStatus.code <> 6");
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

	private void setCashboxDocumentSearchQueryParameters(javax.persistence.Query query, @NotNull ArrayStack filters) {

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;
            if (filter.needFilter()) {

                if (filter instanceof PaymentPointFilter) {
                    query.setParameter("paymentPointId", ((PaymentPointFilter) filter).getSelectedId());
                } else if (filter instanceof CashboxFilter) {
                    query.setParameter("cashboxId", ((CashboxFilter) filter).getSelectedId());
                } else if (filter instanceof ServiceTypeFilter) {
                    query.setParameter("serviceTypeId", ((ServiceTypeFilter) filter).getSelectedId());
                } else if (filter instanceof BeginDateFilter) {
                    query.setParameter("begin", ((BeginDateFilter) filter).getDate(), TemporalType.TIMESTAMP);
                } else if (filter instanceof EndDateFilter) {
                    query.setParameter("end", ((EndDateFilter) filter).getDate(), TemporalType.TIMESTAMP);
                } else if (filter instanceof MinimalSumFilter) {
                    query.setParameter("minimalSum", ((MinimalSumFilter) filter).getBdValue());
                } else if (filter instanceof MaximalSumFilter) {
                    query.setParameter("maximalSum", ((MaximalSumFilter) filter).getBdValue());
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

        log.debug("Search operations query = {}", hql.toString());

		return (List<Operation>) getJpaTemplate().executeFind(new JpaCallback<List<Operation>>() {
			@Override
			public List<Operation> doInJpa(EntityManager entityManager) throws HibernateException {

				javax.persistence.Query cntQuery = entityManager.createQuery(cntHql.toString());
				setCashboxOperationSearchQueryParameters(cntQuery, filters);
				Long count = (Long) cntQuery.getSingleResult();
				pager.setTotalElements(count.intValue());

				javax.persistence.Query query = entityManager.createQuery(hql.toString());
				setCashboxOperationSearchQueryParameters(query, filters);
                log.debug("Operations search query: {}", query.toString());
				return (List<Operation>) query.setFirstResult(pager.getThisPageFirstElementNumber()).
                        setMaxResults(pager.getPageSize()).
                        getResultList();
			}
		});
    }

    private StringBuilder getCashboxOperationSearchHql(@NotNull ArrayStack filters) {

        StringBuilder joinHql = new StringBuilder("");

		StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		// status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3 and o.operationStatus.code <> 6");

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

    private void setCashboxOperationSearchQueryParameters(javax.persistence.Query query, @NotNull ArrayStack filters) {

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;
            if (filter.needFilter()) {

                if (filter instanceof PaymentPointFilter) {
                    query.setParameter("paymentPointId", ((PaymentPointFilter) filter).getSelectedId());
                } else if (filter instanceof CashboxFilter) {
                    query.setParameter("cashboxId", ((CashboxFilter) filter).getSelectedId());
                } else if (filter instanceof BeginDateFilter) {
                    query.setParameter("begin", ((BeginDateFilter) filter).getDate(), TemporalType.TIMESTAMP);
                } else if (filter instanceof EndDateFilter) {
                    query.setParameter("end", ((EndDateFilter) filter).getDate(), TemporalType.TIMESTAMP);
                } else if (filter instanceof MinimalSumFilter) {
                    query.setParameter("minimalSum", ((MinimalSumFilter) filter).getBdValue());
                } else if (filter instanceof MaximalSumFilter) {
                    query.setParameter("maximalSum", ((MaximalSumFilter) filter).getBdValue());
                } else if (filter instanceof CashboxTradingDayFilter) {
                    query.setParameter("tradingDayProcessId", ((CashboxTradingDayFilter) filter).getSelectedId());
                }

            }
        }

	}

    @SuppressWarnings({"unchecked"})
    @Override
    public Operation getLastPaymentPointPaymentOperation(final Long paymentPointId, final Date beginDate, final Date endDate) {
        List<Operation> result = getJpaTemplate().executeFind(new JpaCallback<List<Operation>>() {
			@Override
			public List<Operation> doInJpa(EntityManager entityManager) throws HibernateException {
				return entityManager.createNamedQuery("Operation.listLastPaymentPointPaymentOperations").
						setParameter(1, paymentPointId).
						setParameter(2, beginDate, TemporalType.TIMESTAMP).
						setParameter(3, endDate, TemporalType.TIMESTAMP).
						setMaxResults(1).getResultList();
			}
		});

        return result.isEmpty() ? null : result.get(0);
    }

	@SuppressWarnings({"unchecked"})
    @Override
    public Operation getLastCashboxPaymentOperation(final Long cashboxId, final Date beginDate, final Date endDate) {
        List<Operation> result = getJpaTemplate().executeFind(new JpaCallback<List<Operation>>() {
			@Override
			public List<Operation> doInJpa(EntityManager entityManager) throws HibernateException {
				return entityManager.createNamedQuery("Operation.listLastCashboxPaymentOperations").
						setParameter(1, cashboxId).
						setParameter(2, beginDate, TemporalType.TIMESTAMP).
						setParameter(3, endDate, TemporalType.TIMESTAMP).
						setMaxResults(1).getResultList();
			}
		});

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
	public Long getBlankOperationsCount() {
		return DataAccessUtils.longResult(getJpaTemplate().find("SELECT COUNT(o) FROM Operation o WHERE o.operationStatus.code = 6"));
	}

	@Override
	public void deleteAllBlankOperations() {
		getJpaTemplate().execute(new JpaCallback<Void>() {
			@Override
			public Void doInJpa(EntityManager entityManager) throws HibernateException {
				entityManager.createNamedQuery("Operation.deleteAllBlankOperations").
						setParameter(1, 6).executeUpdate();
				return null;
			}
		});
	}
}
