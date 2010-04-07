package org.flexpay.admin.action.user;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.UserPreferencesService;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class UserDeleteAction extends FPActionSupport {
	private UserPreferencesService preferencesService;

	private Set<String> objectIds = set();

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (objectIds == null) {
			log.warn("ObjectIds user is null");
			return SUCCESS;
		}

		for (String objectId : objectIds) {
			preferencesService.deleteUser(objectId);
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
