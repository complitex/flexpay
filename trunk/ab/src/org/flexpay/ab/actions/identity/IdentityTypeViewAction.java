package org.flexpay.ab.actions.identity;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;

public class IdentityTypeViewAction extends CommonAction {

	private Long id;
	private IdentityTypeService identityTypeService;
	private IdentityType identityType;

	public String execute() throws Exception {
		identityType = identityTypeService.read(id);

		return "success";
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IdentityType getIdentityType() {
		return identityType;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}