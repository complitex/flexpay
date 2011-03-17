package org.flexpay.common.service.impl;

import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.UserPreferencesDefaults;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;

public class UserPreferencesServiceImpl implements UserPreferencesService, InitializingBean {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<UserPreferencesDefaults> defaultsSetters = list();
	private String usedDao;
	private Map<String, UserPreferencesDao> userPreferencesDaos;
	private UserPreferencesFactory userPreferencesFactory;

	protected UserPreferencesDao getUserPreferencesDao() {
		return userPreferencesDaos.get(usedDao);
	}

	/**
	 * Locates the user based on the username. In the actual implementation, the search may possibly be case insensitive,
	 * or case insensitive depending on how the implementaion instance is configured. In this case, the
	 * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
	 * actually requested..
	 *
	 * @param username the username presented to the {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
	 *          if the user could not be found or the user has no GrantedAuthority
	 * @throws org.springframework.dao.DataAccessException
	 *          if user could not be found for a repository-specific reason
	 */
	@NotNull
	@Override
	public UserPreferences loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		log.debug("Loading user by name {}", username);

		UserPreferences preferences = getUserPreferencesDao().findByUserName(username);
		if (preferences == null) {
			throw new UsernameNotFoundException("User name '" + username + "' did not find");
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
	public UserPreferences saveFullData(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer {
		validate(preferences);

		log.debug("Updating user preferences {}", preferences);
		getUserPreferencesDao().saveAllPreferences(preferences);
		return preferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPreferences saveAdvancedData(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer {
		validate(preferences);

		log.debug("Updating user edited own preferences {}", preferences);
		getUserPreferencesDao().saveUserEditedPreferences(preferences);
		return preferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPreferences saveGeneralData(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer {

		log.debug("Updating admin edited user preferences {}", preferences);
		getUserPreferencesDao().saveAdminEditedPreferences(preferences);
		return preferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPreferences updatePassword(@NotNull UserPreferences preferences, String password) throws FlexPayExceptionContainer {
		log.debug("Updating password user preferences {}", preferences);
		getUserPreferencesDao().updateUserPassword(preferences, password);

		return preferences;
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean checkPassword(@NotNull UserPreferences preferences, String password) throws FlexPayExceptionContainer {
		return getUserPreferencesDao().checkUserPassword(preferences, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserPreferences> listAllUser() throws DataAccessException {
		List<UserPreferences> userPreferences = getUserPreferencesDao().listAllUser();
		for (UserPreferences userPreference : userPreferences) {
			// init defaults if needed
			for (UserPreferencesDefaults setter : defaultsSetters) {
				setter.setDefaults(userPreference);
			}
		}
		return userPreferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPreferences updateUserRole(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer {
		log.debug("Update user role user preferences {}", preferences );

		getUserPreferencesDao().changeUserRole(preferences);
		return preferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUserExist(@NotNull String userName) {
		return getUserPreferencesDao().findByUserName(userName) != null;
	}

	/**
	 * Create user preferences
	 *
	 * @param preferences UserPreferences to create
	 * @param password User password
	 * @return Created preferences back, or <code>null</code> if user did not create
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	@Override
	public UserPreferences createNewUser(@NotNull UserPreferences preferences, String password) throws FlexPayExceptionContainer {
		validate(preferences);

		log.debug("Create new user preferences {}", preferences);
		if (!getUserPreferencesDao().createNewUser(preferences, password)) {
			return null;
		}
		return preferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteUser(@NotNull String userName) {
		return getUserPreferencesDao().delete(userName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPreferences createInstanceUserPreferences() {
		UserPreferences preferences = userPreferencesFactory.newInstance();

		// init defaults if needed
		for (UserPreferencesDefaults setter : defaultsSetters) {
			setter.setDefaults(preferences);
		}

		return preferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Certificate editCertificate(@NotNull UserPreferences preferences, String description, Boolean blocked, InputStream inputStreamCertificate) {
		return getUserPreferencesDao().editCertificate(preferences, description, blocked, inputStreamCertificate);
	}

	/**
	 * {@inheritDoc}
	 */
    @Nullable
	@Override
	public Certificate getCertificate(@NotNull UserPreferences preferences) {
		return getUserPreferencesDao().getCertificate(preferences);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCertificate(@NotNull UserPreferences preferences) {
		getUserPreferencesDao().deleteCertificate(preferences);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getGrantedAuthorities(@NotNull UserPreferences preferences) {
		return getUserPreferencesDao().getGrantedAuthorities(preferences);
	}

    @Override
    public boolean isGrantedAuthorities(@NotNull UserPreferences preferences, @NotNull String role) {

        List<String> roles = getGrantedAuthorities(preferences);
        if (roles == null) {
            log.debug("Can't get roles for userPreferences = {}", preferences);
            return false;
        }
        log.debug("User has {} roles", roles.size());
        for (String r : roles) {
            log.debug("Role = {}", r);
            if (r.equals(role)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	protected void validate(UserPreferences preferences) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (preferences.getLocale() == null) {
			container.addException(new FlexPayException("No locale", "error.common.user.no_locale"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		UserPreferencesDao dao = getUserPreferencesDao();
		if (dao == null) {
			throw new IllegalArgumentException("Know nothing about used dao " + usedDao);
		}
	}

	@Required
	public void setUsedDao(String usedDao) {
		this.usedDao = usedDao;
	}

	@Required
	public void setUserPreferencesDaos(Map<String, UserPreferencesDao> userPreferencesDaos) {
		this.userPreferencesDaos = userPreferencesDaos;
	}

	@Required
	public void setUserPreferencesFactory(UserPreferencesFactory userPreferencesFactory) {
		this.userPreferencesFactory = userPreferencesFactory;
	}

	public void setUserPreferencesDefaults(List<UserPreferencesDefaults> defaultsSetters) {
		this.defaultsSetters = defaultsSetters;
	}
}
