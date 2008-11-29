package org.flexpay.ab.actions.street;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreetTypeDeleteAction {

	private Logger log = Logger.getLogger(getClass());

	private StreetTypeService streetTypeService;
	private Set<Long> idList = new HashSet<Long>();

	public String execute() throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("Ids of street types to disable: " + idList);
		}

		List<StreetType> streetTypeToDelete = new ArrayList<StreetType>();
		for (Long id : idList) {
			streetTypeToDelete.add(streetTypeService.read(id));
		}
		try {
			streetTypeService.disable(streetTypeToDelete);
		} catch (RuntimeException e) {
			log.error("Failed disabling street types", e);
		}

		return "afterSubmit";
	}

	public Set<Long> getIdList() {
		return idList;
	}

	public void setIdList(Set<Long> idList) {
		this.idList = idList;
	}

	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
