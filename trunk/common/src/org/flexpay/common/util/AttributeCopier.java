package org.flexpay.common.util;

/**
 * Interface that copies attribute(s) from one object to another
 */
public interface AttributeCopier<T> {

	/**
	 * Do attribute(s) copying
	 *
	 * @param from Object to read attribute(s) from
	 * @param to Object to set attribute(s) to
	 */
	void copy(T from, T to);
}
