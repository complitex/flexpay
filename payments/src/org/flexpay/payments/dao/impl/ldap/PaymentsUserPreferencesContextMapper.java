package org.flexpay.payments.dao.impl.ldap;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.springframework.ldap.core.DirContextOperations;
import org.jetbrains.annotations.Nullable;

public class PaymentsUserPreferencesContextMapper implements UserPreferencesContextMapper {

	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {

		PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) preferences;
		userPreferences.setPaymentPointId(getId(ctx.getStringAttribute("flexpayPaymentsPaymentPointId")));
		userPreferences.setPaymentCollectorId(getId(ctx.getStringAttribute("flexpayPaymentsPaymentCollectorId")));

		return preferences;
	}

	@Nullable
	private Long getId(String strId) {
		if ("0".equals(strId) || StringUtils.isBlank(strId)) {
			return null;
		}
		try {
			return Long.parseLong(strId);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContext(DirContextOperations ctx, UserPreferences preferences) {

		if (!preferences.getObjectClasses().contains("flexpayPaymentsPerson")) {
			ctx.addAttributeValue("objectclass", "flexpayPaymentsPerson");
			preferences.getObjectClasses().add("flexpayPaymentsPerson");
		}

		PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) preferences;
		ctx.setAttributeValue("flexpayPaymentsPaymentPointId", userPreferences.getPaymentPointIdStr());
		ctx.setAttributeValue("flexpayPaymentsPaymentCollectorId", userPreferences.getPaymentCollectorIdStr());
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
