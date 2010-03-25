package org.flexpay.admin.action.user;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.service.UserRoleService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class UserEditAction extends FPActionSupport {
	private UserPreferencesService preferencesService;
	private UserPreferencesFactory userPreferencesFactory;

	private UserRoleService userRoleService;

	private UserPreferences currentUserPreferences;

	private String userName;
	private String firstName;
	private String lastName;
	private String password;
	private String reEnterPassword;
	private String oldPassword;
	private Long roleId;

	private boolean checkOldPassword = false;

	private List<UserRole> userRoles;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		userRoles = userRoleService.getAllUserRoles();

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

		if (userName.equals(SecurityUtil.getUserName())) {
			checkOldPassword = true;
		}

		if (isSubmit()) {
			if (!doValidate()) {
				currentUserPreferences.setFirstName(firstName);
				currentUserPreferences.setLastName(lastName);
				currentUserPreferences.setFullName(createFullName());
				setNewUserRole();
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

	private boolean setNewUserRole() {
		if (roleId != null && roleId > 0 &&
			(currentUserPreferences.getUserRole() == null ||
				!roleId.equals(currentUserPreferences.getUserRole().getId()))) {
			UserRole userRole = userRoleService.readFull(new Stub<UserRole>(roleId));
			if (userRole == null) {
				log.warn("Can`t find role with id={}. User role do not change", roleId);
			} else {
				return true;
			}
		} else if ((roleId == null || roleId <= 0) && currentUserPreferences.getUserRole() != null) {
			currentUserPreferences.setUserRole(null);
			return true;
		}
		return false;
	}

	private void updateUserPreferences() throws FlexPayExceptionContainer {
		currentUserPreferences.setFirstName(firstName);
		currentUserPreferences.setLastName(lastName);
		currentUserPreferences.setFullName(createFullName());
		preferencesService.saveGeneralData(currentUserPreferences);
		if (StringUtils.isNotEmpty(password)) {
			preferencesService.updatePassword(currentUserPreferences, password);
		}
		if (setNewUserRole()) {
			preferencesService.updateUserRole(currentUserPreferences);
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
		if ((StringUtils.isNotEmpty(password) || StringUtils.isNotEmpty(reEnterPassword)) &&
				StringUtils.equals(password, reEnterPassword)) {
			log.error("The passwords you entered do not match");
			addActionError(getText("admin.error.user.passwords_do_not_match"));
		}
		if (StringUtils.isEmpty(password) && StringUtils.isNotEmpty(oldPassword)) {
			log.error("Password was not change. New password is empty");
			addActionError(getText("admin.error.user.new_password_empty"));
		}
		if (userName.equals(SecurityUtil.getUserName()) &&
				StringUtils.isNotEmpty(password) && StringUtils.isEmpty(oldPassword)) {
			log.error("Password was not change. Old password is empty");
			addActionError(getText("admin.error.user.old_password_empty"));
		}
		try {
			if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(oldPassword)
					&& !preferencesService.checkPassword(currentUserPreferences, oldPassword)) {
				log.error("Failed old password");
				addActionError(getText("admin.error.user.failed_old_password"));
			}
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Can`t check old password", flexPayExceptionContainer);
			addActionError(getText("admin.error.inner_error"));
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

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setReEnterPassword(String reEnterPassword) {
		this.reEnterPassword = reEnterPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public UserPreferences getCurrentUserPreferences() {
		return currentUserPreferences;
	}

	public void setCurrentUserPreferences(UserPreferences currentUserPreferences) {
		this.currentUserPreferences = currentUserPreferences;
	}

	public boolean isCheckOldPassword() {
		return checkOldPassword;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	@Required
	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
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
