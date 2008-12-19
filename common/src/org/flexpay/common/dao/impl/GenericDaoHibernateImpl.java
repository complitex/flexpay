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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Hibernate implementation of GenericDao A typesafe implementation of CRUD and finder methods based on Hibernate and
 * Spring AOP The finders are implemented through the executeFinder method. Normally called by the
 * FinderIntroductionInterceptor
 */
@SuppressWarnings ({"unchecked"})
public class GenericDaoHibernateImpl<T, PK extends Serializable>
		implements GenericDao<T, PK>, FinderExecutor<T>, MethodExecutor {

	/**
	 * prefix of a named query parameters followed by list number, like list_1, list_2
	 */
	public static final String PARAM_LIST_PREFIX = "list_";

	/**
	 * Logger
	 */
	@NonNls
	private final Logger log = Logger.getLogger(getClass());

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

	@NotNull
	public PK create(@NotNull T o) {
		return (PK) hibernateTemplate.save(o);
	}

	@Nullable
	public T read(@NotNull PK id) {
		return (T) hibernateTemplate.get(type, id);
	}

	@Nullable
	public T readFull(@NotNull final PK id) {
		@NonNls
		final String queryName = type.getSimpleName() + ".readFull";
		return (T) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				queryObject.setParameter(0, id);
				return queryObject.uniqueResult();
			}
		});
	}

	public void update(@NotNull T o) {
		hibernateTemplate.update(o);
	}

	public void delete(@NotNull T o) {
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
//						.setCacheable(true).setCacheRegion(queryName);
				Query queryCount = getCountQuery(session, queryName);
				Page<?> pageParam = null;
				if (values != null) {
					int nNamedParam = 1;
					for (int i = 0, fix = 0; i < values.length; i++) {

						// handle page parameter
						if (values[i] instanceof Page) {
							++fix;
							pageParam = (Page<?>) values[i];
							continue;
						}
						int nParam = i - fix;
						// handle collection parameter
						if (values[i] instanceof Collection) {
							queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, (Collection<?>) values[i]);
							if (queryCount != null) {
								queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, (Collection<?>) values[i]);
							}
							++fix;
							++nNamedParam;
							continue;
						}
						// handle array parameter
						if (values[i] instanceof Object[]) {
							queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, (Object[]) values[i]);
							if (queryCount != null) {
								queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, (Object[]) values[i]);
							}
							++fix;
							++nNamedParam;
							continue;
						}

						// usual parameter, just set it
						queryObject.setParameter(nParam, values[i]);
						if (queryCount != null) {
							queryCount.setParameter(nParam, values[i]);
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
			return session.getNamedQuery(getCountQueryName(queryName));
		} catch (HibernateException e) {
			return null;
		}
	}

	@NonNls
	private String getCountQueryName(String queryName) {
		return queryName + ".count";
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
