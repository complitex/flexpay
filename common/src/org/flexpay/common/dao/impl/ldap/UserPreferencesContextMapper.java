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
	void doMapToContext(DirContextOperations ctx, UserPreferences preferences);

	/**
	 * Check if this mapper supports supplied object
	 *
	 * @param preferences UserPreferences
	 * @return <code>true</code> if mapper able to map any properties to or from context, or <code>false</code> otherwise
	 */
	boolean supports(UserPreferences preferences);
}
