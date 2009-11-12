package org.flexpay.payments.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ServiceViewAction extends FPActionSupport {

	private Service service = new Service();

	private SPService spService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (service.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}
		service = spService.readFull(stub(service));

		if (service == null) {
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

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

}
