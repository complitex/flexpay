package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//TODO: This class has a out-of-date structure. Must be remake
public class IdentityTypeDeleteAction {

	private Logger log = LoggerFactory.getLogger(getClass());

	private List<Long> idList;

	private IdentityTypeService identityTypeService;

	public String execute() throws Exception {
		List<IdentityType> identityTypeToDelete = CollectionUtils.list();
		for (Long id : idList) {
			identityTypeToDelete.add(identityTypeService.read(new Stub<IdentityType>(id))));
		}
		try {
			identityTypeService.disable(identityTypeToDelete);
		} catch (RuntimeException e) {
			log.error("Failed disabling identity types", e);
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
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

}
