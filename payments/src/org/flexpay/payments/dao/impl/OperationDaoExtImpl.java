package org.flexpay.payments.dao.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.dao.OperationDaoExt;
import org.flexpay.payments.persistence.Operation;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

public class OperationDaoExtImpl extends HibernateDaoSupport implements OperationDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

    @SuppressWarnings ({"unchecked"})
	@Override
	public List<Operation> searchDocuments(Stub<Cashbox> cashbox, final Long serviceTypeId, final Date begin, final Date end,
                                        final BigDecimal minimalSum, final BigDecimal maximalSum, final Page<Operation> pager) {

		final StringBuilder hql = new StringBuilder("SELECT DISTINCT o FROM Operation o LEFT JOIN o.documents doc ");
		final StringBuilder cntHql = new StringBuilder("SELECT COUNT(o) FROM Operation o LEFT JOIN o.documents doc ");
		final StringBuilder filterHql = getCashboxDocumentSearchHql(serviceTypeId, begin, end, minimalSum, maximalSum);
		final Long cashboxId = cashbox.getId();

		hql.append(filterHql);
		cntHql.append(filterHql);

		return (List<Operation>) getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<Operation> doInHibernate(Session session) throws HibernateException {

				Query cntQuery = session.createQuery(cntHql.toString());
				setCashboxDocumentSearchQueryParameters(cntQuery, cashboxId, serviceTypeId, begin, end, minimalSum, maximalSum);
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				Query query = session.createQuery(hql.toString());
				setCashboxDocumentSearchQueryParameters(query, cashboxId, serviceTypeId, begin, end, minimalSum, maximalSum);
				query.setFirstResult(pager.getThisPageFirstElementNumber());
				query.setMaxResults(pager.getPageSize());

				return (List<Operation>) query.list();
			}
		});
	}

    private StringBuilder getCashboxDocumentSearchHql(Long serviceTypeId, Date begin, Date end, BigDecimal minimalSum, BigDecimal maximalSum) {
		StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		filterHql.append(" AND o.cashbox.id = :cashboxId");

		// opeartion status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3");
		filterHql.append(" AND doc.documentStatus.code <> 3");

		if (serviceTypeId != null && serviceTypeId > 0) {
			filterHql.append(" AND doc.service.serviceType.id = :serviceTypeId");
		}

		if (begin != null) {
			filterHql.append(" AND o.creationDate >= :begin");
		}

		if (end != null) {
			filterHql.append(" AND o.creationDate <= :end");
		}

		if (minimalSum != null) {
			filterHql.append(" AND doc.summ >= :minimalSum");
		}

		if (maximalSum != null) {
			filterHql.append(" AND doc.summ <= :maximalSum");
		}

		return filterHql;
	}

	private void setCashboxDocumentSearchQueryParameters(Query query, Long cashboxId, Long serviceTypeId, Date begin, Date end, BigDecimal minimalSum, BigDecimal maximalSum) {

		if (cashboxId != null) {
			query.setLong("cashboxId", cashboxId);
		}

		if (serviceTypeId != null && serviceTypeId > 0) {
			query.setLong("serviceTypeId", serviceTypeId);
		}

		if (begin != null) {
			query.setTimestamp("begin", begin);
		}

		if (end != null) {
			query.setTimestamp("end", end);
		}

		if (minimalSum != null) {
			query.setBigDecimal("minimalSum", minimalSum);
		}

		if (maximalSum != null) {
			query.setBigDecimal("maximalSum", maximalSum);
		}
	}

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings ({"unchecked"})
	@Override
    public List<Operation> searchOperations(Stub<Cashbox> cashbox, final Date begin, final Date end, final BigDecimal minimalSum,
                                     final BigDecimal maximalSum, final Page<Operation> pager) {
        final StringBuilder hql = new StringBuilder("SELECT DISTINCT o FROM Operation o LEFT JOIN o.documents doc ");
		final StringBuilder cntHql = new StringBuilder("SELECT COUNT(o) FROM Operation o LEFT JOIN o.documents doc ");
		final StringBuilder filterHql = getCashboxOperationSearchHql(begin, end, minimalSum, maximalSum);
		final Long cashboxId = cashbox.getId();

		hql.append(filterHql);
		cntHql.append(filterHql);

		return (List<Operation>) getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<Operation> doInHibernate(Session session) throws HibernateException {

				Query cntQuery = session.createQuery(cntHql.toString());
				setCashboxOperationSearchQueryParameters(cntQuery, cashboxId, begin, end, minimalSum, maximalSum);
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				Query query = session.createQuery(hql.toString());
				setCashboxOperationSearchQueryParameters(query, cashboxId, begin, end, minimalSum, maximalSum);
				query.setFirstResult(pager.getThisPageFirstElementNumber());
				query.setMaxResults(pager.getPageSize());

				return (List<Operation>) query.list();
			}
		});
    }

    private StringBuilder getCashboxOperationSearchHql(Date begin, Date end, BigDecimal minimalSum, BigDecimal maximalSum) {
		StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		filterHql.append(" AND o.cashbox.id = :cashboxId");

		// status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3");
		filterHql.append(" AND doc.documentStatus.code <> 3");

		if (begin != null) {
			filterHql.append(" AND o.creationDate >= :begin");
		}

		if (end != null) {
			filterHql.append(" AND o.creationDate <= :end");
		}

		if (minimalSum != null) {
			filterHql.append(" AND o.operationSumm >= :minimalSum");
		}

		if (maximalSum != null) {
			filterHql.append(" AND o.operationSumm <= :maximalSum");
		}

		return filterHql;
	}

    private void setCashboxOperationSearchQueryParameters(Query query, Long cashboxId, Date begin, Date end, BigDecimal minimalSum, BigDecimal maximalSum) {

		if (cashboxId != null) {
			query.setLong("cashboxId", cashboxId);
		}

		if (begin != null) {
			query.setTimestamp("begin", begin);
		}

		if (end != null) {
			query.setTimestamp("end", end);
		}

		if (minimalSum != null) {
			query.setBigDecimal("minimalSum", minimalSum);
		}

		if (maximalSum != null) {
			query.setBigDecimal("maximalSum", maximalSum);
		}
	}

    @SuppressWarnings({"unchecked"})
    @Override
    public Operation getLastPaymentPointPaymentOperation(final Long paymentPointId, final Date beginDate, final Date endDate) {
        return (Operation) getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public Operation doInHibernate(Session session) throws HibernateException {

                List<Operation> results = session.getNamedQuery("Operation.listLastPaymentPointPaymentOperations").
                        setLong(0, paymentPointId).
                        setDate(1, beginDate).
                        setDate(2, endDate).
                        setMaxResults(1).list();

                return results.isEmpty() ? null : results.get(0);
            }
        });
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Operation getLastCashboxPaymentOperation(final Long cashboxId, final Date beginDate, final Date endDate) {
        return (Operation) getHibernateTemplate().executeFind(new HibernateCallback() {
            @Override
            public Operation doInHibernate(Session session) throws HibernateException {

                List<Operation> results = session.getNamedQuery("Operation.listLastCashboxPaymentOperations").
                        setLong(0, cashboxId).
                        setDate(1, beginDate).
                        setDate(2, endDate).
                        setMaxResults(1).list();

                return results.isEmpty() ? null : results.get(0);
            }
        });
    }

    @Override
	public Long getBlankOperationsCount() {
		return DataAccessUtils.longResult(getHibernateTemplate().find("SELECT COUNT(o) FROM Operation o WHERE o.operationStatus.code = 6"));
	}

	@Override
	public void deleteAllBlankOperations() {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				session.getNamedQuery("Operation.deleteAllBlankOperations").setLong(0, 6).executeUpdate();
				return null;
			}
		});
	}
}
