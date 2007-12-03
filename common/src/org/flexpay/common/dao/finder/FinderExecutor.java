package org.flexpay.common.dao.finder;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public interface FinderExecutor<T> {
	/**
	 * Execute a finder method with the appropriate arguments
	 */
	List<T> executeFinder(Method method, Object[] queryArgs);
}
