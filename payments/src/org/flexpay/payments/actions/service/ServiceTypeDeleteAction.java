package org.flexpay.payments.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class ServiceTypeDeleteAction extends FPActionSupport implements CashboxAware {

	private Long cashboxId;
	private Set<Long> objectIds = new HashSet<Long>();

	private ServiceTypeService serviceTypeService;

	@NotNull
	public String doExecute() throws Exception {

		serviceTypeService.disable(objectIds);

		return REDIRECT_SUCCESS;
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
		return REDIRECT_SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {

	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
