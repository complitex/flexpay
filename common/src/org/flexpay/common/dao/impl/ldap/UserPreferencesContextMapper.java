package org.flexpay.common.dao.impl.ldap;

import org.flexpay.common.util.config.UserPreferences;
import org.springframework.ldap.core.DirContextOperations;

/**
 * Mapper of LDAP attributes to UserPreferences properties
 */
public interface UserPreferencesContextMapper {

	/**
	 * Do mapping of context attributes to preferences properties
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 * @return updated preferences back
	 */
	UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences);

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	void doMapToContextAdminEdited(DirContextOperations ctx, UserPreferences preferences);

	/**
	 * Do mapping of admin edited preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	void doMapToContextUserEdited(DirContextOperations ctx, UserPreferences preferences);

	/**
	 * Do mapping of password to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 * @param password Password
	 */
	void doMapToContextPassword(DirContextOperations ctx, UserPreferences preferences, String password);

	/**
	 * Check if this mapper supports supplied object
	 *
	 * @param preferences UserPreferences
	 * @return <code>true</code> if mapper able to map any properties to or from context, or <code>false</code> otherwise
	 */
	boolean supports(UserPreferences preferences);
}
