package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.actions.FPActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TownTypeDeleteAction extends FPActionSupport {

	// form data
	private List<Long> idList;

	// required services
	private TownTypeService townTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		List<TownType> townTypeToDelete = CollectionUtils.list();
		for (Long id : idList) {
			townTypeToDelete.add(townTypeService.read(new Stub<TownType>(id)));
		}

		try {
			townTypeService.disable(townTypeToDelete);
		} catch (RuntimeException e) {
            log.error("Failed delete town type", e);
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	// form data
	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	// required services
	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}
}
