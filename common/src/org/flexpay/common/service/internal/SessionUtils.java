package org.flexpay.common.service.internal;

import java.util.Collection;

/**
 *
 */
public interface SessionUtils {

	public void flush();

	public void clear();

	/**
	 * Evict objects from session
	 *
	 * @param o Object to be evicted
	 */
	public void evict(Object o);

	/**
	 * Evict all objects from a collection
	 *
	 * @param c Collection that elements should be evicted
	 */
	public void evict(Collection<?> c);

	/**
	 * Check if object is a proxy and get real domain object
	 *
	 * @param obj Object to unproxy
	 * @param <T> Object type
	 * @return Object back
	 */
	public <T> T unproxy(T obj);
}
