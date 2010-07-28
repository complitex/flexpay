package org.flexpay.payments.dao.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.DocumentDaoExt;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class DocumentDaoExtImpl extends HibernateDaoSupport implements DocumentDaoExt {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings ({"unchecked"})
	@Override
	public List<Document> searchDocuments(@NotNull Stub<Operation> operation, Long serviceTypeId, BigDecimal minimalSum, BigDecimal maximalSum) {

		final StringBuilder hql = new StringBuilder("SELECT DISTINCT doc FROM Document doc ");
		hql.append(getFilterHql(operation, serviceTypeId, minimalSum, maximalSum));

		return (List<Document>) getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<Document> doInHibernate(Session session) throws HibernateException, SQLException {

				Query query = session.createQuery(hql.toString());
				return (List<Document>) query.list();
			}
		});
	}

	private StringBuilder getFilterHql(@NotNull Stub<Operation> operation, Long serviceTypeId, BigDecimal minimalSum, BigDecimal maximalSum) {

		StringBuilder filterHql = new StringBuilder(" WHERE doc.operation.id = ");
		filterHql.append(operation.getId());

		if (serviceTypeId != null && serviceTypeId > 0) {
			filterHql.append(" AND doc.service.serviceType.id = ");
			filterHql.append(serviceTypeId);
		}

		if (minimalSum != null) {
			filterHql.append(" AND doc.sum >= ");
			filterHql.append(minimalSum);
		}

		if (maximalSum != null) {
			filterHql.append(" AND doc.sum <= ");
			filterHql.append(maximalSum);
		}

		return filterHql;
	}
}
