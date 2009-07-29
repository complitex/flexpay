package org.flexpay.common.service.imp;

import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.service.UserPreferencesDefaults;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserPreferencesServiceImpl implements UserPreferencesService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<UserPreferencesDefaults> defaultsSetters = CollectionUtils.list();
	private UserPreferencesDao userPreferencesDao;
	private UserPreferencesFactory userPreferencesFactory;

	/**
	 * Locates the user based on the username. In the actual implementation, the search may possibly be case insensitive,
	 * or case insensitive depending on how the implementaion instance is configured. In this case, the
	 * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
	 * actually requested..
	 *
	 * @param username the username presented to the {@link org.springframework.security.providers.dao.DaoAuthenticationProvider}
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws org.springframework.security.userdetails.UsernameNotFoundException
	 *          if the user could not be found or the user has no GrantedAuthority
	 * @throws org.springframework.dao.DataAccessException
	 *          if user could not be found for a repository-specific reason
	 */
	@NotNull
	@Override
	public UserPreferences loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		log.debug("Loading user by name {}", username);

		UserPreferences preferences = userPreferencesDao.findByUserName(username);
		if (preferences == null) {
			preferences = userPreferencesFactory.newInstance();
		}

		// init defaults if needed
		for (UserPreferencesDefaults setter : defaultsSetters) {
			setter.setDefaults(preferences);
		}

		return preferences;
	}

	/**
	 * Update UserPreferences object
	 *
	 * @param preferences UserPreferences to update
	 * @return Updated preferences back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if preferences validation fails
	 */
	@Override
	public UserPreferences save(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer {
		validate(preferences);

		log.debug("Updating user preferences {}", preferences);
		userPreferencesDao.save(preferences);
		return preferences;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(UserPreferences preferences) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (preferences.getLocale() == null) {
			container.addException(new FlexPayException("No locale", "error.common.user.no_locale"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	@Required
	public void setUserPreferencesDao(UserPreferencesDao userPreferencesDao) {
		this.userPreferencesDao = userPreferencesDao;
	}

	@Required
	public void setUserPreferencesFactory(UserPreferencesFactory userPreferencesFactory) {
		this.userPreferencesFactory = userPreferencesFactory;
	}

	public void setUserPreferencesDefaults(UserPreferencesDefaults defaultsSetter) {
		defaultsSetters.add(defaultsSetter);
	}
}
