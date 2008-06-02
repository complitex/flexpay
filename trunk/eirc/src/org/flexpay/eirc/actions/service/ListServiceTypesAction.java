package org.flexpay.eirc.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.service.ServiceTypeService;

import java.util.Collections;
import java.util.List;

public class ListServiceTypesAction extends FPActionSupport {

	private ServiceTypeService serviceTypeService;

	private Page<ServiceType> pager = new Page<ServiceType>();
	private List<ServiceType> serviceTypes = Collections.emptyList();

	public String execute() throws Exception {

		serviceTypes = serviceTypeService.listServiceTypes(pager);

		return SUCCESS;
	}

	public List<ServiceType> getServiceTypes() {
		return serviceTypes;
	}

	public Page<ServiceType> getPager() {
		return pager;
	}

	public void setPager(Page<ServiceType> pager) {
		this.pager = pager;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}
