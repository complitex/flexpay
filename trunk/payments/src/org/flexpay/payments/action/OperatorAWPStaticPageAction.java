package org.flexpay.payments.action;

import org.flexpay.payments.persistence.Operation;
import org.jetbrains.annotations.NotNull;

public class OperatorAWPStaticPageAction extends OperatorAWPActionSupport {

	private Long organizationId;

	// print previously created operation
	private Operation operation = new Operation();

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		getUserPreferences().getCrumbs().removeAllElements();
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

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

}
