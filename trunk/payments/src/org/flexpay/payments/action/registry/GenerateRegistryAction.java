package org.flexpay.payments.action.registry;

import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;

public class GenerateRegistryAction extends AccountantAWPActionSupport {

	private Long organizationId;

	@NotNull
    @Override
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
    @Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
