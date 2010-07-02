package org.flexpay.common.util.config;

public class UserPreferencesFactoryHolder {

	private static UserPreferencesFactory factory = new CommonUserPreferencesFactory();

	public UserPreferencesFactory getFactory() {
		return factory;
	}

	public void setFactory(UserPreferencesFactory factory) {
		UserPreferencesFactoryHolder.factory = factory;
	}
}
