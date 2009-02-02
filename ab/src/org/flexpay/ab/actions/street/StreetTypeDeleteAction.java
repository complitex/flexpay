package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreetTypeDeleteAction {

	private Logger log = LoggerFactory.getLogger(getClass());

	private StreetTypeService streetTypeService;
	private Set<Long> idList = new HashSet<Long>();

	public String execute() throws Exception {

		log.debug("Ids of street types to disable: {}", idList);


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
