package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TownTypeDeleteAction {
    protected Logger log = LoggerFactory.getLogger(getClass());

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
            log.error("Failled delete town type", e);
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
