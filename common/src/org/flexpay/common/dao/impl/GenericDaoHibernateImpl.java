package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.finder.FinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.FinderExecutor;
import org.flexpay.common.dao.finder.FinderNamingStrategy;
import org.flexpay.common.dao.finder.impl.SimpleFinderArgumentTypeFactory;
import org.flexpay.common.dao.finder.impl.SimpleFinderNamingStrategy;
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
		final String queryName = getNamingStrategy().queryNameFromMethod(type, method);
		return hibernateTemplate.findByNamedQuery(queryName, queryArgs);
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
