package org.flexpay.ab.actions.town;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;

public class TownTypeViewAction extends CommonAction {
	private Long id;
	private TownTypeService townTypeService;
	private TownType townType;

	public String execute() throws Exception {
		townType = townTypeService.read(id);

		return "success";
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TownType getTownType() {
		return townType;
	}

	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
