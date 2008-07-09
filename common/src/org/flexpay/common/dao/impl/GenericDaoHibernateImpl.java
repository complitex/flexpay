package org.flexpay.common.dao.impl;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.finder.FinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.FinderExecutor;
import org.flexpay.common.dao.finder.FinderNamingStrategy;
import org.flexpay.common.dao.finder.MethodExecutor;
import org.flexpay.common.dao.finder.impl.SimpleFinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.impl.SimpleFinderNamingStrategy;
import org.flexpay.common.dao.paging.Page;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Hibernate implementation of GenericDao A typesafe implementation of CRUD and finder
 * methods based on Hibernate and Spring AOP The finders are implemented through the
 * executeFinder method. Normally called by the FinderIntroductionInterceptor
 */
@SuppressWarnings ({"unchecked"})
public class GenericDaoHibernateImpl<T, PK extends Serializable>
		implements GenericDao<T, PK>, FinderExecutor, MethodExecutor {

	private Logger log = Logger.getLogger(getClass());

	protected HibernateTemplate hibernateTemplate;

	// Default. Can override in config
	private FinderNamingStrategy namingStrategy = new SimpleFinderNamingStrategy();

	// Default. Can override in config
	private FinderArgumentTypeFactory argumentTypeFactory =
			new SimpleFinderArgumentTypeFactory();

	private Class<T> type;

	public GenericDaoHibernateImpl(Class<T> type) {
		this.type = type;
	}

	public PK create(T o) {
		return (PK) hibernateTemplate.save(o);
	}

	public T read(PK id) {
		return (T) hibernateTemplate.get(type, id);
	}

	public T readFull(final PK id) {
		final String queryName = type.getSimpleName() + ".readFull";
		return (T) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				queryObject.setParameter(0, id);
				return queryObject.uniqueResult();
			}
		});
	}

	public void update(T o) {
		hibernateTemplate.update(o);
	}

	public void delete(T o) {
		hibernateTemplate.delete(o);
	}

	public List<T> executeFinder(Method method, final Object[] queryArgs) {
		final String queryName = getNamingStrategy().queryNameFromMethod(type, method);
		return findByNamedQuery(queryName, queryArgs);
	}

	public Integer executeUpdate(Method method, final Object[] values) {
		final String queryName = getNamingStrategy().queryNameFromMethod(type, method);
		return (Integer) hibernateTemplate.execute(new HibernateCallback() {
			public Integer doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
				return queryObject.executeUpdate();
			}
		});
	}

	private List findByNamedQuery(final String queryName, final Object[] values) throws DataAccessException {
		return hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				Query queryCount = getCountQuery(session, queryName);
				Page pageParam = null;
				if (values != null) {
					for (int i = 0, fix = 0; i < values.length; i++) {
						if (values[i] instanceof Page) {
							++fix;
							pageParam = (Page) values[i];
						} else {
							queryObject.setParameter(i - fix, values[i]);
							if (queryCount != null) {
								queryCount.setParameter(i - fix, values[i]);
							}
						}
					}
				}
				if (pageParam != null && queryCount != null) {
					Long count = (Long) queryCount.uniqueResult();
					pageParam.setTotalElements(count.intValue());
					if (log.isDebugEnabled()) {
						log.debug(String.format("Setting page for query: %s %d - %d", queryName,
								pageParam.getThisPageFirstElementNumber(), pageParam.getPageSize()));
					}
					queryObject.setFirstResult(pageParam.getThisPageFirstElementNumber());
					queryObject.setMaxResults(pageParam.getPageSize());
				} else if (pageParam != null) {
					log.warn("Page parameter found, but no count query found: " + queryName + ", invalid API usage");
				}
				List results = queryObject.list();
				if (pageParam != null) {
					pageParam.setElements(results);
				}
				return results;
			}
		});
	}

	private Query getCountQuery(Session session, String queryName) {
		try {
			return session.getNamedQuery(queryName + ".count");
		} catch (HibernateException e) {
			return null;
		}
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public FinderNamingStrategy getNamingStrategy() {
		return namingStrategy;
	}

	public void setNamingStrategy(FinderNamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	public FinderArgumentTypeFactory getArgumentTypeFactory() {
		return argumentTypeFactory;
	}

	public void setArgumentTypeFactory(FinderArgumentTypeFactory argumentTypeFactory) {
		this.argumentTypeFactory = argumentTypeFactory;
	}
}
