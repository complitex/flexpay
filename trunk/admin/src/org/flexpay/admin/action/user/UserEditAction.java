package org.flexpay.admin.action.user;

import org.apache.commons.lang.StringUtils;
import org.flexpay.admin.persistence.UserModel;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.service.UserRoleService;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class UserEditAction extends FPActionSupport {

	private UserPreferences currentUserPreferences;
	private UserModel model = UserModel.getInstance();
	private boolean checkOldPassword = false;
	private List<UserRole> userRoles;

    private UserPreferencesService preferencesService;
    private UserRoleService userRoleService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		userRoles = userRoleService.getAllUserRoles();

		if (StringUtils.isEmpty(model.getUserName())) {
			addActionError(getText("admin.error.user.user_name_empty"));
			return REDIRECT_ERROR;
		}
		try {
			currentUserPreferences = preferencesService.loadUserByUsername(model.getUserName());
			log.debug("username: {}, fullname: {}, lastname: {}", new Object[] {
					currentUserPreferences.getUsername(), currentUserPreferences.getFullName(), currentUserPreferences.getLastName()});
		} catch (Exception e) {
			log.warn("Can not get user preferences with name {}", model.getUserName());
			addActionError(getText("admin.error.inner_error"));
			return REDIRECT_ERROR;
		}

		if (StringUtils.isEmpty(currentUserPreferences.getUsername())) {
			log.warn("Can not get user preferences with name {}", model.getUserName());
			addActionError(getText("admin.error.user.cant_get_user_preferences"));
			return REDIRECT_ERROR;
		}

		if (model.getUserName().equals(SecurityUtil.getUserName())) {
			checkOldPassword = true;
		}

		if (isSubmit()) {
			if (!doValidate()) {
				currentUserPreferences.setFirstName(model.getFirstName());
				currentUserPreferences.setLastName(model.getLastName());
				currentUserPreferences.setFullName(model.getFullName());
				setNewUserRole();
				return INPUT;
			}
			try {
				updateUserPreferences();

				addActionMessage(getText("admin.user.saved"));
				return REDIRECT_SUCCESS;
			} catch (FlexPayExceptionContainer e) {
				log.warn("Can not update user preferences with name {}", model.getUserName());
				addActionError(getText("admin.error.user.cant_update_user_preferences"));
			}
			return REDIRECT_ERROR;
		}

		return INPUT;
	}

	private boolean setNewUserRole() {
		if (model.getRoleId() != null && model.getRoleId() > 0 &&
			(currentUserPreferences.getUserRole() == null ||
				!model.getRoleId().equals(currentUserPreferences.getUserRole().getId()))) {
			UserRole userRole = userRoleService.readFull(new Stub<UserRole>(model.getRoleId()));
			if (userRole == null) {
				log.warn("Can`t find role with id={}. User role do not change", model.getRoleId());
			} else {
				currentUserPreferences.setUserRole(userRole);
				return true;
			}
		} else if ((model.getRoleId() == null || model.getRoleId() <= 0) && currentUserPreferences.getUserRole() != null) {
			currentUserPreferences.setUserRole(null);
			return true;
		}
		return false;
	}

	private void updateUserPreferences() throws FlexPayExceptionContainer {
		currentUserPreferences.setFirstName(model.getFirstName());
		currentUserPreferences.setLastName(model.getLastName());
		currentUserPreferences.setFullName(model.getFullName());
		preferencesService.saveGeneralData(currentUserPreferences);
		if (StringUtils.isNotEmpty(model.getPassword())) {
			preferencesService.updatePassword(currentUserPreferences, model.getPassword());
		}
		if (setNewUserRole()) {
			preferencesService.updateUserRole(currentUserPreferences);
		}
	}

	private boolean doValidate() {
		if (StringUtils.isEmpty(model.getFirstName())) {
			log.error("First name is required parameter");
			addActionError(getText("admin.error.user.first_name_empty"));
		}
		if (StringUtils.isEmpty(model.getLastName())) {
			log.error("Last name is required parameter");
			addActionError(getText("admin.error.user.last_name_empty"));
		}
		if (StringUtils.isEmpty(model.getUserName())) {
			log.error("User name is required parameter");
			addActionError(getText("admin.error.user.user_name_empty"));
		}
		if ((StringUtils.isNotEmpty(model.getPassword()) || StringUtils.isNotEmpty(model.getReEnterPassword())) &&
				StringUtils.equals(model.getPassword(), model.getReEnterPassword())) {
			log.error("The passwords you entered do not match");
			addActionError(getText("admin.error.user.passwords_do_not_match"));
		}
		if (StringUtils.isEmpty(model.getPassword()) && StringUtils.isNotEmpty(model.getOldPassword())) {
			log.error("Password was not change. New password is empty");
			addActionError(getText("admin.error.user.new_password_empty"));
		}
		if (model.getUserName().equals(SecurityUtil.getUserName()) &&
				StringUtils.isNotEmpty(model.getPassword()) && StringUtils.isEmpty(model.getOldPassword())) {
			log.error("Password was not change. Old password is empty");
			addActionError(getText("admin.error.user.old_password_empty"));
		}
		try {
			if (StringUtils.isNotEmpty(model.getPassword()) && StringUtils.isNotEmpty(model.getOldPassword())
					&& !preferencesService.checkPassword(currentUserPreferences, model.getOldPassword())) {
				log.error("Failed old password");
				addActionError(getText("admin.error.user.failed_old_password"));
			}
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Can`t check old password", flexPayExceptionContainer);
			addActionError(getText("admin.error.inner_error"));
		}

		return !hasActionErrors();
	}

    @NotNull
    @Override
    protected String getErrorResult() {
        return INPUT;
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

	public UserModel getModel() {
		return model;
	}

	public void setModel(UserModel model) {
		this.model = model;
	}

	public boolean isPasswordRequired() {
		return false;
	}

	@Required
	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	@Required
	public void setPreferencesService(UserPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}

}
