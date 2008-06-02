package org.flexpay.eirc.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.service.ServiceTypeService;

import java.util.Set;
import java.util.HashSet;

public class DeleteServiceTypesAction extends FPActionSupport {

	private ServiceTypeService serviceTypeService;

	private Set<Long> objectIds = new HashSet<Long>();

	public String execute() throws Exception {
		serviceTypeService.disable(objectIds);

		return SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
