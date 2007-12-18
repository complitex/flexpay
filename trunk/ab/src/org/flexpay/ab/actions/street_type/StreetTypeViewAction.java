package org.flexpay.ab.actions.street_type;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;

public class StreetTypeViewAction extends CommonAction {

	private Long id;
	private StreetTypeService streetTypeService;
	private StreetType streetType;

	public String execute() throws Exception {
		streetType = streetTypeService.read(id);

		return "success";
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StreetType getStreetType() {
		return streetType;
	}

	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
