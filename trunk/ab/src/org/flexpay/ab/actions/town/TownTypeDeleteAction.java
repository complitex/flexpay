package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

//TODO: This class has a out-of-date structure. Must be remake
public class TownTypeDeleteAction {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private TownTypeService townTypeService;
    private List<Long> idList;

    public String execute() throws Exception {
		List<TownType> townTypeToDelete = CollectionUtils.list();
		for (Long id : idList) {
			townTypeToDelete.add(townTypeService.read(new Stub<TownType>(id)));
		}
		try {
			townTypeService.disable(townTypeToDelete);
		} catch (RuntimeException e) {
            log.error("Failled delete town type", e);
		}

		return "afterSubmit";
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
