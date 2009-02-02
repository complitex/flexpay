package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;

import java.util.ArrayList;
import java.util.List;

public class IdentityTypeDeleteAction {
	private IdentityTypeService identityTypeService;
	private List<Long> idList;

	public String execute() throws Exception {
		List<IdentityType> identityTypeToDelete = new ArrayList<IdentityType>(idList.size());
		for (Long id : idList) {
			identityTypeToDelete.add(identityTypeService.read(id));
		}
		try {
			identityTypeService.disable(identityTypeToDelete);
		} catch (RuntimeException e) {
			// TODO
		}

		return "afterSubmit";
	}

	/**
	 * Setter for property 'identityTypeService'.
	 * 
	 * @param identityTypeService
	 *            Value to set for property 'identityTypeService'.
	 */
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

}
