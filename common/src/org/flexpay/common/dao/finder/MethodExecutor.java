package org.flexpay.common.dao.finder;

import java.lang.reflect.Method;

public interface MethodExecutor {

	/**
	 * 
	 * @param method
	 * @param queryArgs
	 * @return
	 */
	Integer executeUpdate(Method method, Object[] queryArgs);
}
