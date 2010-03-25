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

	@Override
	public void doMapToContextAdminEdited(DirContextOperations ctx, UserPreferences preferences) {
		for (UserPreferencesContextMapper mapper : mappers) {
			mapper.doMapToContextAdminEdited(ctx, preferences);
		}
	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContextUserEdited(DirContextOperations ctx, UserPreferences preferences) {

		for (UserPreferencesContextMapper mapper : mappers) {
			mapper.doMapToContextUserEdited(ctx, preferences);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doMapToContextPassword(DirContextOperations ctx, UserPreferences preferences, String password) {
		for (UserPreferencesContextMapper mapper : mappers) {
			mapper.doMapToContextPassword(ctx, preferences, password);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doMapToContextAccessPermissions(DirContextOperations ctx, UserPreferences preferences, List<String> permissions) {
		for (UserPreferencesContextMapper mapper : mappers) {
			mapper.doMapToContextAccessPermissions(ctx, preferences, permissions);
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
