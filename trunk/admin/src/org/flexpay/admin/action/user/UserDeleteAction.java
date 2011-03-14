package org.flexpay.admin.action.user;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.service.UserPreferencesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class UserDeleteAction extends FPActionSupport {

    private Set<String> objectIds = set();

	private UserPreferencesService preferencesService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (objectIds == null) {
			log.warn("ObjectIds user is null");
			return SUCCESS;
		}

		boolean userDidNotDeleteByError = false;
		for (String objectId : objectIds) {
			if (getUserPreferences().getUsername().equals(objectId)) {
				addActionError("admin.error.user.delete_own");
			} else if (!preferencesService.deleteUser(objectId)) {
				userDidNotDeleteByError = true;
			}
		}
		if (userDidNotDeleteByError) {
			addActionError("admin.error.user.did_not_delete");
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setObjectIds(Set<String> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setPreferencesService(UserPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}
}
