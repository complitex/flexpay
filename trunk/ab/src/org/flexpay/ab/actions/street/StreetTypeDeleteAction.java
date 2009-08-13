package org.flexpay.ab.actions.street;

import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class StreetTypeDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private StreetTypeService streetTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		streetTypeService.disableByIds(objectIds);

		return REDIRECT_SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

}
