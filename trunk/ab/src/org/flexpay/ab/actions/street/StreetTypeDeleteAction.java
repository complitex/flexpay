package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.actions.FPActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreetTypeDeleteAction extends FPActionSupport {

	// form data
	private Set<Long> idList = new HashSet<Long>();

	// required services
	private StreetTypeService streetTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		List<StreetType> streetTypeToDelete = CollectionUtils.list();
		for (Long id : idList) {
			streetTypeToDelete.add(streetTypeService.read(new Stub<StreetType>(id)));
		}
		try {
			streetTypeService.disable(streetTypeToDelete);
		} catch (RuntimeException e) {
			log.error("Failed disabling street types", e);
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {

		return SUCCESS;
	}

	// form data
	public Set<Long> getIdList() {
		return idList;
	}

	public void setIdList(Set<Long> idList) {
		this.idList = idList;
	}

	// required services
	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

}
