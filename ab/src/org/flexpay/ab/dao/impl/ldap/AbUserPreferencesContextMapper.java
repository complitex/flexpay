package org.flexpay.ab.dao.impl.ldap;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextOperations;

import java.util.List;

public class AbUserPreferencesContextMapper implements UserPreferencesContextMapper {

	private final Logger log = LoggerFactory.getLogger(getClass());

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
	public void doMapToContextAdminEdited(DirContextOperations ctx, UserPreferences preferences) {

	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContextUserEdited(DirContextOperations ctx, UserPreferences preferences) {

		if (!preferences.getObjectClasses().contains("flexpayAbPerson")) {
			ctx.addAttributeValue("objectclass", "flexpayAbPerson");
			preferences.getObjectClasses().add("flexpayAbPerson");
		}

		AbUserPreferences userPreferences = (AbUserPreferences) preferences;
		setSingleAttribute(ctx, preferences, "flexpayAbCountryFilter", userPreferences.getCountryFilter());
		setSingleAttribute(ctx, preferences, "flexpayAbRegionFilter", userPreferences.getRegionFilter());
		setSingleAttribute(ctx, preferences, "flexpayAbTownFilter", userPreferences.getTownFilter());
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
	public void doMapToContextAccessPermissions(DirContextOperations ctx, UserPreferences preferences, List<String> permissions) {

	}

	private void setSingleAttribute(DirContextOperations ctx, UserPreferences preferences, String name, Long value) {
		if (value == null) {
			value = 0L;
		}
		if (preferences.attributes().contains(name)) {
			log.debug("Setting attribute {} value: {}", name, value);
			ctx.setAttributeValue(name, String.valueOf(value));
		} else {
			log.debug("Adding attribute {} value: {}", name, value);
			ctx.addAttributeValue(name, String.valueOf(value));
		}
	}

	private Long getFilterValue(String attributeName, Long defaultValue, DirContextOperations ctx) {
		String filterValue = ctx.getStringAttribute(attributeName);
		try {
			return StringUtils.isNotBlank(filterValue) ? Long.parseLong(filterValue) : defaultValue;
		} catch (NumberFormatException ex) {
			log.warn("Unexpected long value: {}", filterValue);
			return defaultValue;
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
		return preferences.getObjectClasses().contains("flexpayAbPerson");
	}

}
