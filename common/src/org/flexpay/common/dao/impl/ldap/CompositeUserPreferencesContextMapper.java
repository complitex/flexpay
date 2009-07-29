package org.flexpay.common.dao.impl.ldap;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.springframework.ldap.core.DirContextOperations;

import java.util.List;

/**
 * Mapper that holds a set of mappers and delegates all the work to them
 */
public class CompositeUserPreferencesContextMapper implements UserPreferencesContextMapper {

	private List<UserPreferencesContextMapper> mappers = CollectionUtils.list();

	/**
	 * Do mapping of context attributes to preferences properties
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 * @return updated preferences back
	 */
	@Override
	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {

		for (UserPreferencesContextMapper mapper : mappers) {
			if (mapper.supports(preferences)) {
				mapper.doMapFromContext(ctx, preferences);
			}
		}

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

		for (UserPreferencesContextMapper mapper : mappers) {
			mapper.doMapToContext(ctx, preferences);
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

	public void setMapper(UserPreferencesContextMapper mapper) {
		mappers.add(mapper);
	}
}
