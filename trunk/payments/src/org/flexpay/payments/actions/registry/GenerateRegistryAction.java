package org.flexpay.payments.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.PaymentPointAwareAction;
import org.jetbrains.annotations.NotNull;

public class GenerateRegistryAction extends FPActionSupport implements PaymentPointAwareAction {

	private Long paymentPointId;
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

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
