package org.flexpay.admin.actions.user;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class UserListAction extends FPActionWithPagerSupport<UserPreferences> {
	private UserPreferencesService preferencesService;
	private List<UserPreferences> allUserPreferences = list();

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		allUserPreferences = preferencesService.listAllUser();
		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<UserPreferences> getAllUserPreferences() {
		return allUserPreferences;
	}

	@Required
	public void setPreferencesService(UserPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}
}
