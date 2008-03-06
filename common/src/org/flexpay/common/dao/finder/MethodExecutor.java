package org.flexpay.common.dao.finder;

import java.lang.reflect.Method;

public interface MethodExecutor {

	/**
	 * Execute update method of the interface
	 * 
	 * @param method interface method
	 * @param queryArgs Query arguments
	 * @return number of update affected rows
	 */
	Integer executeUpdate(Method method, Object[] queryArgs);
}
