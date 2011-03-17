package org.flexpay.payments.dao.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.payments.dao.ServiceDaoExt;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.filters.ParentServiceFilterMarker;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ServiceDaoExtImpl extends HibernateDaoSupport implements ServiceDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings ({"unchecked"})
	public List<ServiceType> getServiceTypes() {
		return getHibernateTemplate().find("select distinct t from ServiceType t " +
										   "left join fetch t.typeNames n left join fetch n.lang where t.status=0");
	}

	/**
	 * Find Service type by its code
	 *
	 * @param code service type code
	 * @return ServiceType instance
	 */
	public ServiceType findByCode(int code) {
		try {
			getHibernateTemplate().setMaxResults(1);
			List<?> objects = getHibernateTemplate().find("from ServiceType where code=? and status=0", code);
			return objects.isEmpty() ? null : (ServiceType) objects.get(0);
		} finally {
			getHibernateTemplate().setMaxResults(0);
		}
	}

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	@SuppressWarnings ({"unchecked"})
	public List<Service> findServices(List<ObjectFilter> filters, final Page<Service> pager) {
		final StringBuilder hql = new StringBuilder("select distinct o from Service o " +
													"left join fetch o.descriptions " +
													"left join fetch o.childServices c " +
													"inner join fetch o.serviceType t left join fetch t.typeNames " +
													"inner join fetch o.serviceProvider p " +
													"inner join fetch p.organization org left join fetch org.names " +
													"where 1=1 ");
		final StringBuilder hqlCount = new StringBuilder("select count(*) from Service o " +
														 "inner join o.serviceType t inner join o.serviceProvider p where 1=1 ");
		final List<Object> params = list();

		for (ObjectFilter filter : filters) {
			if (!filter.needFilter()) {
				continue;
			}
			if (filter instanceof ServiceProviderFilter) {
				ServiceProviderFilter providerFilter = (ServiceProviderFilter) filter;
				hql.append("and p.id=? ");
				hqlCount.append("and p.id=? ");
				params.add(providerFilter.getSelectedId());
			} else if (filter instanceof BeginDateFilter) {
				BeginDateFilter dateFilter = (BeginDateFilter) filter;
				hql.append("and o.endDate>=? ");
				hqlCount.append("and o.endDate>=? ");
				params.add(dateFilter.getDate());
			} else if (filter instanceof EndDateFilter) {
				EndDateFilter dateFilter = (EndDateFilter) filter;
				hql.append("and o.beginDate<=? ");
				hqlCount.append("and o.beginDate<=? ");
				params.add(dateFilter.getDate());
			} else if (filter instanceof ParentServiceFilterMarker) {
				hql.append("and o.parentService is null ");
				hqlCount.append("and o.parentService is null ");
			} else {
				log.warn("Unexpected filter: {}", filter);
			}
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Number count = (Number) setParameters(session.createQuery(hqlCount.toString()), params).uniqueResult();
				pager.setTotalElements(count.intValue());

				return setParameters(session.createQuery(hql.toString()), params)
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.list();
			}
		});
	}

	private Query setParameters(Query query, List<Object> params) {
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		return query;
	}

	/**
	 * Find provider services of the specified type for date interval
	 *
	 * @param providerId Service provider identifier
	 * @param typeId	 Service type identifier
	 * @param beginDate  Interval begin date
	 * @param endDate	interval end date
	 * @return List of services
	 */
	@SuppressWarnings ({"unchecked"})
	public List<Service> findIntersectingServices(Long providerId, Long typeId, Date beginDate, Date endDate) {
		Object[] params = {providerId, typeId, beginDate, endDate, beginDate, endDate};
		return getHibernateTemplate().findByNamedQuery("Service.findIntersectingServices", params);
	}
}
