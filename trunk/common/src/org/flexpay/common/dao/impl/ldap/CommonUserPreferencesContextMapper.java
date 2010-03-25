package org.flexpay.common.dao.impl.ldap;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.service.UserRoleService;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.core.DirContextOperations;

import java.util.List;

public class CommonUserPreferencesContextMapper implements UserPreferencesContextMapper {

	private Logger log = LoggerFactory.getLogger(getClass());

	private UserRoleService userRoleService;

	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {

		preferences.setFullName(ctx.getStringAttribute("cn"));
		preferences.setLastName(ctx.getStringAttribute("sn"));
		preferences.setUsername(ctx.getStringAttribute("uid"));
		preferences.setFirstName(ctx.getStringAttribute("givenName"));

		String externalUserRoleName = ctx.getStringAttribute("flexpayUserRole");

		if (StringUtils.isNotEmpty(externalUserRoleName)) {
			UserRole userRole = userRoleService.findByExternalId(externalUserRoleName);
			if (userRole == null) {
				log.warn("Can`t find user role by external name: {}", externalUserRoleName);
				preferences.setUserRole(null);
			} else {
				preferences.setUserRole(userRole);
			}
		}

		if (preferences.getObjectClasses().contains(LdapConstants.OBJECT_CLASS)) {
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
	public void doMapToContextAdminEdited(DirContextOperations ctx, UserPreferences preferences) {
		setSingleAttribute(ctx, preferences, "cn", preferences.getFullName());
		setSingleAttribute(ctx, preferences, "sn", preferences.getLastName());
		setSingleAttribute(ctx, preferences, "givenName", preferences.getFirstName());
		setSingleAttribute(ctx, preferences, "uid", preferences.getUsername());
	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContextUserEdited(DirContextOperations ctx, UserPreferences preferences) {
		if (!preferences.getObjectClasses().contains(LdapConstants.OBJECT_CLASS)) {
			ctx.addAttributeValue("objectclass", LdapConstants.OBJECT_CLASS);
			preferences.getObjectClasses().add(LdapConstants.OBJECT_CLASS);
		}

		setSingleAttribute(ctx, preferences, "flexpayPreferedLocale", preferences.getLanguageCode());
		setSingleAttribute(ctx, preferences, "flexpayPreferedPagerSize", String.valueOf(preferences.getPageSize()));
	}

	@Override
	public void doMapToContextPassword(DirContextOperations ctx, UserPreferences preferences, String password) {
		setSingleAttribute(ctx, preferences, "userPassword", password);
	}

	@Override
	public void doMapToContextAccessPermissions(DirContextOperations ctx, UserPreferences preferences, List<String> permissions) {
		/*
		String[] allCurrentValues = ctx.getStringAttributes("memberof");
		for (String currentValue : allCurrentValues) {
			ctx.removeAttributeValue("memberof", currentValue);
		}
		*/
		ctx.setAttributeValues("memberof", permissions.toArray());
		if (preferences.getUserRole() != null) {
			setSingleAttribute(ctx, preferences, "flexpayUserRole", preferences.getUserRole().getExternalId());
		} else {
			setSingleAttribute(ctx, preferences, "flexpayUserRole", "");
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
		return true;
	}

	@Required
	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}
}
