package org.flexpay.common.dao.finder.impl;

import org.flexpay.common.dao.finder.FinderNamingStrategy;

import java.lang.reflect.Method;

/**
 * Looks up Hibernate named queries based on the simple name of the invoced class and the
 * method name of the invocation
 */
public class SimpleFinderNamingStrategy implements FinderNamingStrategy {

    @Override
	public String queryNameFromMethod(Class findTargetType, Method finderMethod) {
		return findTargetType.getSimpleName() + "." + finderMethod.getName();
	}

}
