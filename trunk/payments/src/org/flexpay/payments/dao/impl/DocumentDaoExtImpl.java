package org.flexpay.payments.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.payments.dao.DocumentDaoExt;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.filters.MaximalSumFilter;
import org.flexpay.payments.persistence.filters.MinimalSumFilter;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class DocumentDaoExtImpl extends HibernateDaoSupport implements DocumentDaoExt {

	@SuppressWarnings({"unchecked"})
    @Override
	public List<Document> searchDocuments(@NotNull Stub<Operation> operation, @NotNull ArrayStack filters) {

		final StringBuilder hql = new StringBuilder("SELECT DISTINCT doc FROM Document doc ");
		hql.append(getFilterHql(operation, filters));

		return (List<Document>) getHibernateTemplate().executeFind(new HibernateCallback<List<Document>>() {
			@Override
			public List<Document> doInHibernate(Session session) throws HibernateException {

				Query query = session.createQuery(hql.toString());
				return query.list();
			}
		});
	}

	private StringBuilder getFilterHql(@NotNull Stub<Operation> operation, @NotNull ArrayStack filters) {

		StringBuilder filterHql = new StringBuilder(" WHERE doc.operation.id = ");
		filterHql.append(operation.getId());

        for (Object f : filters) {

            ObjectFilter filter = (ObjectFilter) f;

            if (filter.needFilter()) {

                if (filter instanceof ServiceTypeFilter) {
                    filterHql.append(" AND doc.service.serviceType.id = ").
                            append(((ServiceTypeFilter) filter).getSelectedId());
                } else if (filter instanceof MinimalSumFilter) {
                    filterHql.append(" AND doc.sum >= ").append(((MinimalSumFilter) filter).getBdValue());
                } else if (filter instanceof MaximalSumFilter) {
                    filterHql.append(" AND doc.sum <= ").append(((MaximalSumFilter) filter).getBdValue());
                }

            }

        }

		return filterHql;
	}
}
