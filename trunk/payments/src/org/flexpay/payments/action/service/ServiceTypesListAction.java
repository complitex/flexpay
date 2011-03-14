package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ServiceTypesListAction extends FPActionWithPagerSupport<ServiceType> {

	private List<ServiceType> serviceTypes = list();

	private ServiceTypeService serviceTypeService;

	@NotNull
	@Override
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
	@Override
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
