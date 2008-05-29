package org.flexpay.eirc.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.imp.OrganisationServiceImpl;

import java.util.HashSet;
import java.util.Set;

public class DeleteServiceProvidersAction extends FPActionSupport {

	private SPService spService;

	private Set<Long> objectIds = new HashSet<Long>();

	public String execute() throws Exception {
		spService.disable(objectIds);

		return SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}