package org.flexpay.ab.actions.street;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;

public class StreetTypeDeleteAction {
	private StreetTypeService streetTypeService;
	private List<Long> idList;

	public String execute() throws Exception {
		List<StreetType> streetTypeToDelete = new ArrayList<StreetType>(idList.size());
		for (Long id : idList) {
			streetTypeToDelete.add(streetTypeService.read(id));
		}
		try {
			streetTypeService.disable(streetTypeToDelete);
		} catch (RuntimeException e) {
			// TODO
		}

		return "afterSubmit";
	}

	/**
	 * Setter for property 'streetTypeService'.
	 * 
	 * @param streetTypeService
	 *            Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

}
