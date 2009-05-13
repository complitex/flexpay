package org.flexpay.payments.dao.impl;

import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.dao.DocumentDaoExt;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.jetbrains.annotations.NotNull;
import org.hibernate.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;
import java.math.BigDecimal;
import java.sql.SQLException;

public class DocumentDaoExtImpl extends HibernateDaoSupport implements DocumentDaoExt {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings ({"unchecked"})
	public List<Document> searchDocuments(@NotNull Operation operation, Long serviceTypeId, BigDecimal minimalSumm, BigDecimal maximalSumm) {

		final StringBuilder hql = new StringBuilder("SELECT DISTINCT doc FROM Document doc ");
		hql.append(getFilterHql(operation, serviceTypeId, minimalSumm, maximalSumm));

		return (List<Document>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<Document> doInHibernate(Session session) throws HibernateException, SQLException {

				Query query = session.createQuery(hql.toString());
				return (List<Document>) query.list();
			}
		});
	}

	private StringBuilder getFilterHql(@NotNull Operation operation, Long serviceTypeId, BigDecimal minimalSumm, BigDecimal maximalSumm) {

		StringBuilder filterHql = new StringBuilder(" WHERE doc.operation.id = ");
		filterHql.append(operation.getId());

		if (serviceTypeId != null) {
			filterHql.append(" AND doc.service.serviceType.id = ");
			filterHql.append(serviceTypeId);
		}

		if (minimalSumm != null) {
			filterHql.append(" AND doc.summ >= ");
			filterHql.append(minimalSumm);
		}

		if (maximalSumm != null) {
			filterHql.append(" AND doc.summ <= ");
			filterHql.append(maximalSumm);
		}

		return filterHql;
	}
}
