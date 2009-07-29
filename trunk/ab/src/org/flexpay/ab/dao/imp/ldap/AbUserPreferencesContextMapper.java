package org.flexpay.ab.dao.imp.ldap;

import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.springframework.ldap.core.DirContextOperations;

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
		userPreferences.setCountryFilterValue(ctx.getStringAttribute("flexpayAbCountryFilter"));
		userPreferences.setRegionFilterValue(ctx.getStringAttribute("flexpayAbRegionFilter"));
		userPreferences.setTownFilterValue(ctx.getStringAttribute("flexpayAbTownFilter"));
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
		ctx.setAttributeValue("flexpayAbCountryFilter", userPreferences.getCountryFilterValue());
		ctx.setAttributeValue("flexpayAbRegionFilter", userPreferences.getRegionFilterValue());
		ctx.setAttributeValue("flexpayAbTownFilter", userPreferences.getTownFilterValue());
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
