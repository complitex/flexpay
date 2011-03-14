package org.flexpay.ab.action.town;

import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.action.FPActionSupport;
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

		if (objectIds == null) {
			log.warn("ObjectIds parameter is null");
			return SUCCESS;
		}

		townTypeService.disable(objectIds);

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
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
