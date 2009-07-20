package org.flexpay.common.util.config;

import org.flexpay.common.util.config.UserPreferencesFactory;

public class UserPreferencesFactoryHolderFactory {

	private static UserPreferencesFactoryHolder factoryHolder = new UserPreferencesFactoryHolder();

	public UserPreferencesFactoryHolder getFactoryHolder() {
		return factoryHolder;
	}
}
