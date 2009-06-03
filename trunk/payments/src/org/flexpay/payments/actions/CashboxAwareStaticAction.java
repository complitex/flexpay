package org.flexpay.payments.actions;

import org.flexpay.common.actions.StaticPageAction;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.actions.interceptor.CashboxAware;

public class CashboxAwareStaticAction extends StaticPageAction implements CashboxAware {

	private Long cashboxId;
	private Long organizationId;

	// print previously created operation
	private Operation operation = new Operation();

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

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

}
