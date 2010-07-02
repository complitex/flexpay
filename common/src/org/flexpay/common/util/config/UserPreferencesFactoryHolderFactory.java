package org.flexpay.common.util.config;

public class UserPreferencesFactoryHolderFactory {

	private static UserPreferencesFactoryHolder factoryHolder = new UserPreferencesFactoryHolder();

	public UserPreferencesFactoryHolder getFactoryHolder() {
		return factoryHolder;
	}
}
