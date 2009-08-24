package org.flexpay.ab.actions.street;

import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class StreetDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private StreetService streetService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		streetService.disableByIds(objectIds);

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

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
