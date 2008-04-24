package org.flexpay.ab.actions.town;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;

public class TownTypeDeleteAction {
	private TownTypeService townTypeService;
	private List<Long> idList;

	public String execute() throws Exception {
		List<TownType> townTypeToDelete = new ArrayList<TownType>(idList.size());
		for (Long id : idList) {
			townTypeToDelete.add(townTypeService.read(id));
		}
		try {
			townTypeService.disable(townTypeToDelete);
		} catch (RuntimeException e) {
			// TODO
		}

		return "afterSubmit";
	}

	/**
	 * Setter for property 'townTypeService'.
	 * 
	 * @param townTypeService
	 *            Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

}
