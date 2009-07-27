package org.flexpay.payments.actions;

import org.flexpay.common.actions.StaticPageAction;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.flexpay.payments.persistence.Operation;
import org.jetbrains.annotations.NotNull;

public class CashboxAwareStaticAction extends StaticPageAction implements CashboxAware {

	private Long cashboxId;
	private Long organizationId;

	// print previously created operation
	private Operation operation = new Operation();

	@Override
	@NotNull
	protected String doExecute() throws Exception {
		getUserPreferences().getCrumbs().removeAllElements();
		return SUCCESS;
	}

	@Override
	public Long getCashboxId() {
		return cashboxId;
	}

	@Override
	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
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
