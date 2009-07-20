package org.flexpay.payments.util.config;

import org.flexpay.common.util.config.UserPreferencesFactory;

public class PaymentsUserPreferencesFactory implements UserPreferencesFactory {

	/**
	 * Create new UserPreferences instance
	 *
	 * @return module dependent UserPreferences object
	 */
	@Override
	public PaymentsUserPreferences newInstance() {
		return new PaymentsUserPreferences();
	}
}
