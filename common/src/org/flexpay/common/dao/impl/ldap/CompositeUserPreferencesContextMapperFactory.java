package org.flexpay.common.dao.impl.ldap;

/**
 * Simple composite mapper factory
 */
public class CompositeUserPreferencesContextMapperFactory {

	private static final CompositeUserPreferencesContextMapper mapper = new CompositeUserPreferencesContextMapper();

	public CompositeUserPreferencesContextMapper getMapper() {
		return mapper;
	}
}
