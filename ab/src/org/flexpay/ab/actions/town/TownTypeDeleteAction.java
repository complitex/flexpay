package org.flexpay.ab.actions.town;

import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class TownTypeDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private TownTypeService townTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		townTypeService.disableByIds(objectIds);

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
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
