package org.flexpay.payments.dao.impl.ldap;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.Nullable;
import org.springframework.ldap.core.DirContextOperations;

import java.io.InputStream;
import java.util.List;

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

	@Override
	public void doMapToContextAdminEdited(DirContextOperations ctx, UserPreferences preferences) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContextUserEdited(DirContextOperations ctx, UserPreferences preferences) {

		if (!preferences.getObjectClasses().contains(LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS)) {
			ctx.addAttributeValue("objectclass", LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS);
			preferences.getObjectClasses().add(LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS);
		}

		PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) preferences;
		setSingleAttribute(ctx, preferences, "flexpayPaymentsPaymentPointId", userPreferences.getPaymentPointIdStr());
		setSingleAttribute(ctx, preferences, "flexpayPaymentsPaymentCollectorId", userPreferences.getPaymentCollectorIdStr());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doMapToContextPassword(DirContextOperations ctx, UserPreferences preferences, String password) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doMapToContextCertificate(DirContextOperations ctx, UserPreferences preferences, InputStream inputStream, boolean delete) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doMapToContextAccessPermissions(DirContextOperations ctx, UserPreferences preferences, List<String> permissions) {
		
	}

	@Override
	public void doMapToContextNewUser(DirContextOperations ctx, UserPreferences preferences) {
		for (String objectClass : LdapConstants.REQUIRED_OBJECT_CLASSES) {
			ctx.addAttributeValue("objectclass", objectClass);
			preferences.getObjectClasses().add(objectClass);
		}
	}

	private void setSingleAttribute(DirContextOperations ctx, UserPreferences preferences, String name, String value) {
		if (preferences.attributes().contains(name)) {
			ctx.setAttributeValue(name, value);
		} else {
			ctx.addAttributeValue(name, value);
		}
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
