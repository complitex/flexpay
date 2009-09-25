package org.flexpay.common.dao.impl.ldap;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextOperations;

public class CommonUserPreferencesContextMapper implements UserPreferencesContextMapper {

	private Logger log = LoggerFactory.getLogger(getClass());

	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {

		preferences.setFullName(ctx.getStringAttribute("cn"));
		preferences.setLastName(ctx.getStringAttribute("sn"));
		preferences.setUsername(ctx.getStringAttribute("uid"));

		if (preferences.getObjectClasses().contains("flexpayPerson")) {
			preferences.setLanguageCode(ctx.getStringAttribute("flexpayPreferedLocale"));
			preferences.setPageSize(getFilterValue("flexpayPreferedPagerSize", 20, ctx));
		}

		return preferences;
	}

	private Integer getFilterValue(String attributeName, Integer defaultValue, DirContextOperations ctx) {
		String filterValue = ctx.getStringAttribute(attributeName);
		try {
			return StringUtils.isNotBlank(filterValue) ? Integer.parseInt(filterValue) : defaultValue;
		} catch (NumberFormatException ex) {
			log.warn("Unexpected int value: {}", filterValue);
			return defaultValue;
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

		if (!preferences.getObjectClasses().contains("flexpayPerson")) {
			ctx.addAttributeValue("objectclass", "flexpayPerson");
			preferences.getObjectClasses().add("flexpayPerson");
		}
		ctx.setAttributeValue("flexpayPreferedLocale", preferences.getLanguageCode());
		ctx.setAttributeValue("flexpayPreferedPagerSize", String.valueOf(preferences.getPageSize()));
	}

	/**
	 * Check if this mapper supports supplied object
	 *
	 * @param preferences UserPreferences
	 * @return <code>true</code> if mapper able to map any properties to or from context, or <code>false</code> otherwise
	 */
	@Override
	public boolean supports(UserPreferences preferences) {
		return true;
	}
}
