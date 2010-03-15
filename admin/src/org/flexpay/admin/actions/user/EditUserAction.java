package org.flexpay.admin.actions.user;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class EditUserAction extends FPActionSupport {
	private UserPreferencesService preferencesService;
	private UserPreferencesFactory userPreferencesFactory;

	private UserPreferences currentUserPreferences;

	private String userName;
	private String firstName;
	private String lastName;
	private String password;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (StringUtils.isEmpty(userName)) {
			addActionError(getText("admin.error.user.user_name_empty"));
			return REDIRECT_ERROR;
		}
		try {
			currentUserPreferences = preferencesService.loadUserByUsername(userName);
			log.debug("username: {}, fullname: {}, lastname: {}", new Object[] {
					currentUserPreferences.getUsername(), currentUserPreferences.getFullName(), currentUserPreferences.getLastName()});
		} catch (Exception e) {
			log.warn("Can not get user preferences with name {}", userName);
			addActionError(getText("admin.error.inner_error"));
			return REDIRECT_ERROR;
		}

		if (StringUtils.isEmpty(currentUserPreferences.getUsername())) {
			log.warn("Can not get user preferences with name {}", userName);
			addActionError(getText("admin.error.user.cant_get_user_preferences"));
			return REDIRECT_ERROR;
		}

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
			try {
				updateUserPreferences();

				addActionMessage(getText("admin.user.saved"));
				return REDIRECT_SUCCESS;
			} catch (FlexPayExceptionContainer e) {
				log.warn("Can not update user preferences with name {}", userName);
				addActionError(getText("admin.error.user.cant_update_user_preferences"));
			}
			return REDIRECT_ERROR;
		}

		return INPUT;
	}

	private void updateUserPreferences() throws FlexPayExceptionContainer {
		currentUserPreferences.setFirstName(firstName);
		currentUserPreferences.setLastName(lastName);
		currentUserPreferences.setFullName(createFullName());
		preferencesService.saveGeneralData(currentUserPreferences);
		if (StringUtils.isNotEmpty(password)) {
			preferencesService.updatePassword(currentUserPreferences, password);
		}
	}

	private boolean doValidate() {
		if (StringUtils.isEmpty(firstName)) {
			log.error("First name is required parameter");
			addActionError(getText("admin.error.user.first_name_empty"));
		}
		if (StringUtils.isEmpty(lastName)) {
			log.error("Last name is required parameter");
			addActionError(getText("admin.error.user.last_name_empty"));
		}
		if (StringUtils.isEmpty(userName)) {
			log.error("User name is required parameter");
			addActionError(getText("admin.error.user.user_name_empty"));
		}

		return !hasActionErrors();
	}

	private String createFullName() {
		if (StringUtils.isEmpty(firstName)) {
			return lastName;
		}
		if (StringUtils.isEmpty(lastName)) {
			return firstName;
		}
		return firstName + " " + lastName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserPreferences getCurrentUserPreferences() {
		return currentUserPreferences;
	}

	public void setCurrentUserPreferences(UserPreferences currentUserPreferences) {
		this.currentUserPreferences = currentUserPreferences;
	}

	@Required
	public void setPreferencesService(UserPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}

	@Required
	public void setUserPreferencesFactory(UserPreferencesFactory userPreferencesFactory) {
		this.userPreferencesFactory = userPreferencesFactory;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}
}
