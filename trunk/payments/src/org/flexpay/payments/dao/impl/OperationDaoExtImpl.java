package org.flexpay.payments.dao.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.dao.OperationDaoExt;
import org.flexpay.payments.persistence.Operation;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OperationDaoExtImpl extends HibernateDaoSupport implements OperationDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings ({"unchecked"})
	public List<Operation> searchDocuments(Organization organization, final Long serviceTypeId, final Date begin, final Date end, final BigDecimal minimalSumm, final BigDecimal maximalSumm, final Page<Operation> pager) {

		final StringBuilder hql = new StringBuilder("SELECT DISTINCT o FROM Operation o LEFT JOIN o.documents doc ");
		final StringBuilder cntHql = new StringBuilder("SELECT COUNT(o) FROM Operation o LEFT JOIN o.documents doc ");		
		final StringBuilder filterHql = getDocumentSearchHql(serviceTypeId, begin, end, minimalSumm, maximalSumm);
		final Long organizationId = organization.getId();

		hql.append(filterHql);
		cntHql.append(filterHql);

		return (List<Operation>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<Operation> doInHibernate(Session session) throws HibernateException, SQLException {

				Query cntQuery = session.createQuery(cntHql.toString());
				setDocumentSearchQueryParameters(cntQuery, organizationId, serviceTypeId, begin, end, minimalSumm, maximalSumm);
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				Query query = session.createQuery(hql.toString());
				setDocumentSearchQueryParameters(query, organizationId, serviceTypeId, begin, end, minimalSumm, maximalSumm);
				query.setFirstResult(pager.getThisPageFirstElementNumber());
				query.setMaxResults(pager.getPageSize());

				return (List<Operation>) query.list();
			}
		});
	}

	private StringBuilder getDocumentSearchHql(Long serviceTypeId, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm) {
		StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		filterHql.append(" AND o.creatorOrganization.id = :organizationId");

		// opeartion status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3");
		filterHql.append(" AND doc.documentStatus.code <> 3");

		if (serviceTypeId != null) {
			filterHql.append(" AND doc.service.serviceType.id = :serviceTypeId");// TODO may be the problem is here?
		}

		if (begin != null) {
			filterHql.append(" AND o.creationDate >= :begin");
		}

		if (end != null) {
			filterHql.append(" AND o.creationDate <= :end");
		}

		if (minimalSumm != null) {
			filterHql.append(" AND doc.summ >= :minimalSumm");
		}

		if (maximalSumm != null) {
			filterHql.append(" AND doc.summ <= :maximalSumm");
		}

		return filterHql;
	}

	private void setDocumentSearchQueryParameters(Query query, Long organizationId, Long serviceTypeId, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm) {

		if (organizationId != null) {
			query.setLong("organizationId", organizationId);
		}

		if (serviceTypeId != null) {
			query.setLong("serviceTypeId", serviceTypeId);
		}

		if (begin != null) {
			query.setTimestamp("begin", begin);
		}

		if (end != null) {
			query.setTimestamp("end", end);
		}

		if (minimalSumm != null) {
			query.setBigDecimal("minimalSumm", minimalSumm);
		}

		if (maximalSumm != null) {
			query.setBigDecimal("maximalSumm", maximalSumm);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings ({"unchecked"})
	public List<Operation> searchOperations(Organization organization, final Date begin, final Date end, final BigDecimal minimalSumm, final BigDecimal maximalSumm, final Page<Operation> pager) {

		final StringBuilder hql = new StringBuilder("SELECT DISTINCT o FROM Operation o LEFT JOIN o.documents doc ");
		final StringBuilder cntHql = new StringBuilder("SELECT COUNT(o) FROM Operation o LEFT JOIN o.documents doc ");
		final StringBuilder filterHql = getOperationSearchHql(begin, end, minimalSumm, maximalSumm);
		final Long organizationId = organization.getId();

		hql.append(filterHql);
		cntHql.append(filterHql);

		return (List<Operation>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<Operation> doInHibernate(Session session) throws HibernateException, SQLException {

				Query cntQuery = session.createQuery(cntHql.toString());
				setOperationSearchQueryParameters(cntQuery, organizationId, begin, end, minimalSumm, maximalSumm);
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				Query query = session.createQuery(hql.toString());
				setOperationSearchQueryParameters(query, organizationId, begin, end, minimalSumm, maximalSumm);
				query.setFirstResult(pager.getThisPageFirstElementNumber());
				query.setMaxResults(pager.getPageSize());

				return (List<Operation>) query.list();
			}
		});
	}

	private StringBuilder getOperationSearchHql(Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm) {
		StringBuilder filterHql = new StringBuilder(" WHERE");

		// operation type filtering (payments only)
		filterHql.append(" (o.operationType.code = 1 OR o.operationType.code = 2 OR o.operationType.code = 5 OR o.operationType.code = 6)");
		filterHql.append(" AND o.creatorOrganization.id = :organizationId");

		// status filtering (non-deleted ones)
		filterHql.append(" AND o.operationStatus.code <> 3");
		filterHql.append(" AND doc.documentStatus.code <> 3");

		if (begin != null) {
			filterHql.append(" AND o.creationDate >= :begin");
		}

		if (end != null) {
			filterHql.append(" AND o.creationDate <= :end");
		}

		if (minimalSumm != null) {
			filterHql.append(" AND o.operationSumm >= :minimalSumm");
		}

		if (maximalSumm != null) {
			filterHql.append(" AND o.operationSumm <= :maximalSumm");
		}

		return filterHql;
	}

	private void setOperationSearchQueryParameters(Query query, Long organizationId, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm) {

		if (organizationId != null) {
			query.setLong("organizationId", organizationId);
		}

		if (begin != null) {
			query.setTimestamp("begin", begin);
		}

		if (end != null) {
			query.setTimestamp("end", end);
		}

		if (minimalSumm != null) {
			query.setBigDecimal("minimalSumm", minimalSumm);
		}

		if (maximalSumm != null) {
			query.setBigDecimal("maximalSumm", maximalSumm);
		}
	}

}
