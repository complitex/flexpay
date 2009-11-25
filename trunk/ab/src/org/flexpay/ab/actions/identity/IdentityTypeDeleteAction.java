package org.flexpay.ab.actions.identity;

import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class IdentityTypeDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private IdentityTypeService identityTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (objectIds == null) {
			log.debug("ObjectIds parameter is null");
			return SUCCESS;
		}

		identityTypeService.disable(objectIds);

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

}
