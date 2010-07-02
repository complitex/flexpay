package org.flexpay.common.util.config;

import org.springframework.beans.factory.annotation.Required;

public class UserPreferencesFactoryProxy implements UserPreferencesFactory {

	private UserPreferencesFactoryHolder holder;

	/**
	 * Create new UserPreferences instance
	 *
	 * @return module dependent UserPreferences object
	 */
	@Override
	public UserPreferences newInstance() {
		return holder.getFactory().newInstance();
	}

	@Required
	public void setHolder(UserPreferencesFactoryHolder holder) {
		this.holder = holder;
	}
}
