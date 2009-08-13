package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.actions.FPActionSupport;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class IdentityTypeDeleteAction extends FPActionSupport {

	private Logger log = LoggerFactory.getLogger(getClass());

	// form data
	private List<Long> idList;

	// required services
	private IdentityTypeService identityTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		List<IdentityType> identityTypeToDelete = CollectionUtils.list();
		for (Long id : idList) {
			identityTypeToDelete.add(identityTypeService.read(new Stub<IdentityType>(id)));
		}

		try {
			identityTypeService.disable(identityTypeToDelete);
		} catch (RuntimeException e) {
			log.error("Failed disabling identity types", e);
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {

		return SUCCESS;
	}

	// form data
	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	// required services
	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
