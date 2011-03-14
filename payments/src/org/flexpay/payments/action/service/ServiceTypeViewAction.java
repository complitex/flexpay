package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceTypeViewAction extends FPActionSupport {

	private ServiceType serviceType = new ServiceType();

	private ServiceTypeService serviceTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (serviceType.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}
		serviceType = serviceTypeService.read(stub(serviceType));

		if (serviceType == null) {
			log.error(getText("common.object_not_selected"));
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
