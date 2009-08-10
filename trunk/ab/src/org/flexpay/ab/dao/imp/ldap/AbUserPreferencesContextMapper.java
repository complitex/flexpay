package org.flexpay.ab.dao.imp.ldap;

import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.springframework.ldap.core.DirContextOperations;
import org.apache.commons.lang.StringUtils;

public class AbUserPreferencesContextMapper implements UserPreferencesContextMapper {

	/**
	 * Do mapping of context attributes to preferences properties
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 * @return updated preferences back
	 */
	@Override
	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {
		AbUserPreferences userPreferences = (AbUserPreferences) preferences;
		userPreferences.setCountryFilter(getFilterValue("flexpayAbCountryFilter", 0L, ctx));
		userPreferences.setRegionFilter(getFilterValue("flexpayAbRegionFilter", 0L, ctx));
		userPreferences.setTownFilter(getFilterValue("flexpayAbTownFilter", 0L, ctx));
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

		if (!preferences.getObjectClasses().contains("flexpayAbPerson")) {
			ctx.addAttributeValue("objectclass", "flexpayAbPerson");
			preferences.getObjectClasses().add("flexpayAbPerson");
		}

		AbUserPreferences userPreferences = (AbUserPreferences) preferences;
		ctx.setAttributeValue("flexpayAbCountryFilter", userPreferences.getCountryFilter());
		ctx.setAttributeValue("flexpayAbRegionFilter", userPreferences.getRegionFilter());
		ctx.setAttributeValue("flexpayAbTownFilter", userPreferences.getTownFilter());
	}

	private Long getFilterValue(String attributeName, Long defaultValue, DirContextOperations ctx) {
		String filterValue = ctx.getStringAttribute(attributeName);
		return StringUtils.isNotBlank(filterValue) ? Long.parseLong(filterValue) : defaultValue;
	}

	/**
	 * Check if this mapper supports supplied object
	 *
	 * @param preferences UserPreferences
	 * @return <code>true</code> if mapper able to map any properties to or from context, or <code>false</code> otherwise
	 */
	@Override
	public boolean supports(UserPreferences preferences) {
		return preferences.getObjectClasses().contains("flexpayAbPerson");
	}

}
