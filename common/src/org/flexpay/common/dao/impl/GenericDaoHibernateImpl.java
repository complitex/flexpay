package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.finder.FinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.FinderExecutor;
import org.flexpay.common.dao.finder.FinderNamingStrategy;
import org.flexpay.common.dao.finder.impl.SimpleFinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.impl.SimpleFinderNamingStrategy;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Hibernate implementation of GenericDao A typesafe implementation of CRUD and finder
 * methods based on Hibernate and Spring AOP The finders are implemented through the
 * executeFinder method. Normally called by the FinderIntroductionInterceptor
 */
@SuppressWarnings ({"unchecked"})
public class GenericDaoHibernateImpl<T, PK extends Serializable>
		implements GenericDao<T, PK>, FinderExecutor {
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

	public void update(T o) {
		hibernateTemplate.update(o);
	}

	public void delete(T o) {
		hibernateTemplate.delete(o);
	}

	public List<T> executeFinder(Method method, final Object[] queryArgs) {
		final Query namedQuery = prepareQuery(method, queryArgs);
		return (List<T>) namedQuery.list();
	}

	public Iterator<T> iterateFinder(Method method, final Object[] queryArgs) {
		final Query namedQuery = prepareQuery(method, queryArgs);
		return (Iterator<T>) namedQuery.iterate();
	}

//    public ScrollableResults scrollFinder(Method method, final Object[] queryArgs)
//    {
//        final Query namedQuery = prepareQuery(method, queryArgs);
//        return (ScrollableResults) namedQuery.scroll();
//    }

	private Query prepareQuery(Method method, Object[] queryArgs) {
		final String queryName = getNamingStrategy().queryNameFromMethod(type, method);
		final Query namedQuery = getSession().getNamedQuery(queryName);
		String[] namedParameters = namedQuery.getNamedParameters();
		if (namedParameters.length == 0) {
			setPositionalParams(queryArgs, namedQuery);
		} else {
			setNamedParams(namedParameters, queryArgs, namedQuery);
		}
		return namedQuery;
	}

	private void setPositionalParams(Object[] queryArgs, Query namedQuery) {
		// Set parameter. Use custom Hibernate Type if necessary
		if (queryArgs != null) {
			for (int i = 0; i < queryArgs.length; i++) {
				Object arg = queryArgs[i];
				Type argType = getArgumentTypeFactory().getArgumentType(arg);
				if (argType != null) {
					namedQuery.setParameter(i, arg, argType);
				} else {
					namedQuery.setParameter(i, arg);
				}
			}
		}
	}

	private void setNamedParams(String[] namedParameters, Object[] queryArgs, Query namedQuery) {
		// Set parameter. Use custom Hibernate Type if necessary
		if (queryArgs != null) {
			for (int i = 0; i < queryArgs.length; i++) {
				Object arg = queryArgs[i];
				Type argType = getArgumentTypeFactory().getArgumentType(arg);
				if (argType != null) {
					namedQuery.setParameter(namedParameters[i], arg, argType);
				} else {
					if (arg instanceof Collection) {
						namedQuery.setParameterList(namedParameters[i], (Collection) arg);
					} else {
						namedQuery.setParameter(namedParameters[i], arg);
					}
				}
			}
		}
	}

	public Session getSession() {
		return SessionFactoryUtils.getSession(hibernateTemplate.getSessionFactory(), true);
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
