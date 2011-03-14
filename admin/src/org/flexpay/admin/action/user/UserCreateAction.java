package org.flexpay.admin.action.user;

import org.apache.commons.lang.StringUtils;
import org.flexpay.admin.persistence.UserModel;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.service.UserRoleService;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class UserCreateAction extends FPActionSupport {

	private UserModel model = UserModel.getInstance();
	private UserPreferences currentUserPreferences;
	private List<UserRole> userRoles;

    private UserPreferencesService preferencesService;
    private UserRoleService userRoleService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		userRoles = userRoleService.getAllUserRoles();
		currentUserPreferences = preferencesService.createInstanceUserPreferences();

		if (isSubmit()) {
			currentUserPreferences.setUsername(model.getUserName());
			currentUserPreferences.setFirstName(model.getFirstName());
			currentUserPreferences.setLastName(model.getLastName());
			currentUserPreferences.setFullName(model.getFullName());
			setUserRole();
			
			if (!doValidate()) {
				return INPUT;
			}
			try {
				if (!createUserPreferences()) {
                    log.warn("User preferences with name {} didn't create", model.getUserName());
					addActionError("admin.error.user.did_not_create");
					return REDIRECT_ERROR;
				}
				addActionMessage(getText("admin.user.saved"));
			} catch (FlexPayExceptionContainer e) {
				log.warn("Can not create user preferences with name {}", model.getUserName());
				addActionError(getText("admin.error.user.cant_update_user_preferences"));
                return REDIRECT_ERROR;
			}
            return REDIRECT_SUCCESS;
		}
		return INPUT;
	}

	private boolean createUserPreferences() throws FlexPayExceptionContainer {
		return preferencesService.createNewUser(currentUserPreferences, model.getPassword()) != null;
	}

	private void setUserRole() {
		if (model.getRoleId() != null && model.getRoleId() > 0) {
			UserRole userRole = userRoleService.readFull(new Stub<UserRole>(model.getRoleId()));
			if (userRole == null) {
				log.warn("Can`t find role with id={}. User role do not set", model.getRoleId());
			} else {
				currentUserPreferences.setUserRole(userRole);
			}
		}
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	private boolean doValidate() {
		if (isEmpty(model.getUserName())) {
			log.error("User name is required parameter");
			addActionError(getText("admin.error.user.user_name_empty"));
		}
		if (isEmpty(model.getFirstName())) {
			log.error("First name is required parameter");
			addActionError(getText("admin.error.user.first_name_empty"));
		}
		if (isEmpty(model.getLastName())) {
			log.error("Last name is required parameter");
			addActionError(getText("admin.error.user.last_name_empty"));
		}
		if (isEmpty(model.getPassword())) {
			log.error("Password is required parameter");
			addActionError(getText("admin.error.user.password_empty"));
		}
		if (isEmpty(model.getReEnterPassword())) {
			log.error("Reenter password is required parameter");
			addActionError(getText("admin.error.user.reenter_password_empty"));
		}
		if (!StringUtils.equals(model.getPassword(), model.getReEnterPassword())) {
			log.error("The passwords you entered do not match");
			addActionError(getText("admin.error.user.passwords_do_not_match"));
		}
		if (preferencesService.isUserExist(model.getUserName())) {
			log.error("User exist");
			addActionError(getText("admin.error.user.exist"));
		}

		return !hasActionErrors();
	}

	public UserModel getModel() {
		return model;
	}

	public void setModel(UserModel model) {
		this.model = model;
	}

	public UserPreferences getCurrentUserPreferences() {
		return currentUserPreferences;
	}

	public void setCurrentUserPreferences(UserPreferences currentUserPreferences) {
		this.currentUserPreferences = currentUserPreferences;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public boolean isPasswordRequired() {
		return true;
	}

	@Required
	public void setPreferencesService(UserPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}

	@Required
	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}
}
