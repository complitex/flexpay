package org.flexpay.common.service.internal;

import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;

/**
 *
 */
public interface SessionUtils {

	void flush();

	void clear();

	/**
	 * Evict objects from session
	 *
	 * @param o Object to be evicted
	 */
	void evict(Object o);

	/**
	 * Evict all objects from a collection
	 *
	 * @param c Collection that elements should be evicted
	 */
	void evict(Collection<?> c);

	/**
	 * Check if object is a proxy and get real domain object
	 *
	 * @param obj Object to unproxy
	 * @param <T> Object type
	 * @return Object back
	 */
	<T> T unproxy(T obj);

    void setJpaTemplate(JpaTemplate jpaTemplate);
}
