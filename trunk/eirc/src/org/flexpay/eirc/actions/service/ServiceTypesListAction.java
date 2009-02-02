package org.flexpay.eirc.actions.service;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class ServiceTypesListAction extends FPActionWithPagerSupport<ServiceType> {

	private List<ServiceType> serviceTypes = Collections.emptyList();

	private ServiceTypeService serviceTypeService;

	@NotNull
	public String doExecute() throws Exception {

		serviceTypes = serviceTypeService.listServiceTypes(getPager());

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<ServiceType> getServiceTypes() {
		return serviceTypes;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
