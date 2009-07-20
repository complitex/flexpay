package org.flexpay.payments.dao.impl.ldap;

import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.springframework.ldap.core.DirContextOperations;

public class PaymentsUserPreferencesContextMapper implements UserPreferencesContextMapper {

	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {

		PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) preferences;
		userPreferences.setPaymentPointId(Long.parseLong(ctx.getStringAttribute("flexpayPaymentsPaymentPointId")));

		return preferences;
	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContext(DirContextOperations ctx, UserPreferences preferences) {

		PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) preferences;
		ctx.setAttributeValue("flexpayPaymentsPaymentPointId", userPreferences.getPaymentPointIdStr());
	}

	/**
	 * Check if this mapper supports supplied object
	 *
	 * @param preferences UserPreferences
	 * @return <code>true</code> if mapper able to map any properties to or from context, or <code>false</code> otherwise
	 */
	@Override
	public boolean supports(UserPreferences preferences) {
		return preferences.getObjectClasses().contains("flexpayPaymentsPerson");
	}
}
