package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.finder.FinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.FinderExecutor;
import org.flexpay.common.dao.finder.FinderNamingStrategy;
import org.flexpay.common.dao.finder.MethodExecutor;
import org.flexpay.common.dao.finder.impl.SimpleFinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.impl.SimpleFinderNamingStrategy;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Range;
import org.flexpay.common.util.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Hibernate implementation of GenericDao A type safe implementation of CRUD and finder methods based on Hibernate and
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

	private static final Object[] NULL_ARRAY = {null};
	private static final List<?> NULL_LIST = Arrays.asList(NULL_ARRAY);

	/**
	 * Logger
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

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
		final String queryName = type.getSimpleName() + ".readFull";
		return (T) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				queryObject.setParameter(0, id);
				return queryObject.uniqueResult();
			}
		});
	}

	/**
	 * Read full persistent objects info
	 *
	 * @param ids		   Object identifiers
	 * @param preserveOrder whether to save order of elements
	 * @return Objects found
	 */
	@NotNull
	@Override
	public List<T> readFullCollection(final @NotNull Collection<PK> ids, boolean preserveOrder) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}
		final String queryName = type.getSimpleName() + ".readFullCollection";
		List<T> result = (List<T>) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.getNamedQuery(queryName);
				query.setParameterList("ids", ids);
				return query.list();
			}
		});

		if (preserveOrder) {
			Map<Long, DomainObject> map = CollectionUtils.map();
			for (Object t : result) {
				DomainObject o = (DomainObject) t;
				map.put(o.getId(), o);
			}
			result = CollectionUtils.list();
			for (Object id : ids) {
				//noinspection SuspiciousMethodCalls
				result.add((T) map.get(id));
			}
		}

		return result;
	}

	public void update(@NotNull T o) {
		hibernateTemplate.update(o);
	}

	@Override
	public T merge(@NotNull T object) {
		return (T) hibernateTemplate.merge(object);
	}

	public void delete(@NotNull T o) {
		hibernateTemplate.delete(o);
	}

	public List<T> executeFinder(Method method, final Object[] queryArgs) {
		final String queryName = getNamingStrategy().queryNameFromMethod(type, method);
		return (List<T>) findByNamedQuery(queryName, queryArgs);
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

	private List<?> findByNamedQuery(final String queryName, final Object[] values) throws DataAccessException {
		return hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				Query queryCount = getCountQuery(session, queryName);
				Query queryStats = getStatsQuery(session, queryName);
				Page<?> pageParam = null;
				FetchRange range = null;
				int fetchRangeParamPosition = 0;
				if (values != null) {
					int nNamedParam = 1;
					for (int i = 0, fix = 0; i < values.length; i++) {

						// handle page parameter
						if (values[i] instanceof Page) {
							if (pageParam != null || range != null) {
								throw new IllegalStateException("Only one Page or FetchRange parameter allowed");
							}
							++fix;
							pageParam = (Page<?>) values[i];
							continue;
						}
						// handle FetchRange parameter
						if (values[i] instanceof FetchRange) {
							if (range != null) {
								throw new IllegalStateException("Only one FetchRange or Page parameter allowed");
							}
							fetchRangeParamPosition = i - fix;
							// skip 2 "between ? and ?" parameters and add thrown away 1 argument
							fix += -2 + 1;
							range = (FetchRange) values[i];
							continue;
						}
						int nParam = i - fix;
						if (values[i] instanceof Range) {
							Range<?> rangeParam = (Range<?>) values[i];
							queryObject.setParameter(nParam, rangeParam.getStart());
							queryObject.setParameter(nParam + 1, rangeParam.getEnd());
							if (queryCount != null) {
								queryCount.setParameter(nParam, rangeParam.getStart());
								queryCount.setParameter(nParam + 1, rangeParam.getEnd());
							}
							if (queryStats != null) {
								// stats query should not contain additional "between ? and ?"
								nParam = fetchRangeParamPosition != 0 ? nParam - 2 : nParam;
								queryStats.setParameter(nParam, rangeParam.getStart());
								queryStats.setParameter(nParam + 1, rangeParam.getEnd());
							}
							// skip 2 range parameters and add thrown away 1 argument
							fix += -2 + 1;
							continue;
						}
						// handle collection parameter
						if (values[i] instanceof Collection) {
							Collection<?> value = (Collection<?>) values[i];
							if (value.isEmpty()) {
								log.warn("Empty collection parameter {} in query {}",
										PARAM_LIST_PREFIX + nNamedParam, queryName);
								value = NULL_LIST;
							}
							queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, value);
							if (queryCount != null) {
								queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, value);
							}
							if (queryStats != null) {
								queryStats.setParameterList(PARAM_LIST_PREFIX + nNamedParam, value);
							}
							++fix;
							++nNamedParam;
							continue;
						}
						// handle array parameter
						if (values[i] instanceof Object[]) {
							Object[] value = (Object[]) values[i];
							if (value.length == 0) {
								log.warn("Empty collection parameter {} in query {}",
										PARAM_LIST_PREFIX + nNamedParam, queryName);
								value = NULL_ARRAY;
							}
							queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, value);
							if (queryCount != null) {
								queryObject.setParameterList(PARAM_LIST_PREFIX + nNamedParam, value);
							}
							if (queryStats != null) {
								queryStats.setParameterList(PARAM_LIST_PREFIX + nNamedParam, value);
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
						if (queryStats != null) {
							// stats query should not contain additional "between ? and ?"
							nParam = fetchRangeParamPosition != 0 ? nParam - 2 : nParam;
							queryStats.setParameter(nParam, values[i]);
						}
					}
				}

				if (pageParam != null && queryCount != null) {
					Long count = (Long) queryCount.uniqueResult();
					pageParam.setTotalElements(count.intValue());
					log.debug("Setting page for query: {} {} - {}", new Object[]{
							queryName, pageParam.getThisPageFirstElementNumber(), pageParam.getPageSize()});
					queryObject.setFirstResult(pageParam.getThisPageFirstElementNumber());
					queryObject.setMaxResults(pageParam.getPageSize());
				} else if (pageParam != null) {
					log.warn("Page parameter found, but no count query found: {}, invalid API usage", queryName);
				}
				if (range != null && queryStats != null) {
					if (!range.wasInitialized()) {
						Object[] stats = (Object[]) queryStats.uniqueResult();
						range.setMinId((Long) stats[0]);
						range.setMaxId((Long) stats[1]);
						range.setCount(((Long) stats[2]).intValue());
						range.setLowerBound(range.getMinId());
						range.setUpperBound(range.getLowerBound() != null ? range.getLowerBound() + range.getPageSize() : null);
					}

					if (!range.wasInitialized()) {
						log.debug("No records in range");
						return Collections.emptyList();
					}

					queryObject.setLong(fetchRangeParamPosition, range.getLowerBound());
					queryObject.setLong(fetchRangeParamPosition + 1, range.getUpperBound());
				} else if (range != null) {
					throw new IllegalStateException("Found FetchRange parameter, but no stats query found: "
													+ getStatsQueryName(queryName));
				}
				@SuppressWarnings ({"RawUseOfParameterizedType"})
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

	private Query getStatsQuery(Session session, String queryName) {
		try {
			return session.getNamedQuery(getStatsQueryName(queryName));
		} catch (HibernateException e) {
			return null;
		}
	}

	private String getCountQueryName(String queryName) {
		return queryName + ".count";
	}

	private String getStatsQueryName(String queryName) {
		return queryName + ".stats";
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
