package org.flexpay.common.dao.impl.ldap;

import org.flexpay.common.util.config.UserPreferences;
import org.springframework.ldap.core.DirContextOperations;

public class CommonUserPreferencesContextMapper implements UserPreferencesContextMapper {

	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {

		preferences.setFullName(ctx.getStringAttribute("cn"));
		preferences.setLastName(ctx.getStringAttribute("sn"));
		preferences.setLanguageCode(ctx.getStringAttribute("flexpayPreferedLocale"));

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

		ctx.setAttributeValue("cn", preferences.getFullName());
		ctx.setAttributeValue("sn", preferences.getLastName());
		ctx.setAttributeValue("flexpayPreferedLocale", preferences.getLanguageCode());
	}

	/**
	 * Check if this mapper supports supplied object
	 *
	 * @param preferences UserPreferences
	 * @return <code>true</code> if mapper able to map any properties to or from context, or <code>false</code> otherwise
	 */
	@Override
	public boolean supports(UserPreferences preferences) {
		return preferences.getObjectClasses().contains("flexpayPerson");
	}
}
