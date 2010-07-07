package org.flexpay.common.dao.finder;

import java.lang.reflect.Method;
import java.util.List;

public interface FinderExecutor<T> {
	/**
	 * Execute a finder method with the appropriate arguments
     *
     * @param method method
     * @param queryArgs query arguments
     * @return finder
     */
	List<T> executeFinder(Method method, Object[] queryArgs);

}
