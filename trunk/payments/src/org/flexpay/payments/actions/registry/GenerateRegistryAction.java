package org.flexpay.payments.actions.registry;

import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;

public class GenerateRegistryAction extends AccountantAWPActionSupport {

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
