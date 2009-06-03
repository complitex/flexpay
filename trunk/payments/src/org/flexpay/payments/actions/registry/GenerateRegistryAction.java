package org.flexpay.payments.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.jetbrains.annotations.NotNull;

public class GenerateRegistryAction extends FPActionSupport implements CashboxAware {

	private Long cashboxId;
	private Long organizationId;

	@NotNull
	protected String doExecute() throws Exception {

		// TODO implement
		
		if (isSubmit()) {
			addActionMessage(getText("payments.registry.generate.generation_started"));
		} else {
			clearErrorsAndMessages();
		}

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		
		return SUCCESS;
	}

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
