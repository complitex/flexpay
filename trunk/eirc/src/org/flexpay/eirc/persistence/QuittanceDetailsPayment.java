package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class QuittanceDetailsPayment extends DomainObject {

	private QuittancePayment payment;
	private QuittancePaymentStatus paymentStatus;
	private QuittanceDetails quittanceDetails;
	private BigDecimal amount;

	/**
	 * Constructs a new DomainObject.
	 */
	public QuittanceDetailsPayment() {
	}

	public QuittanceDetailsPayment(@NotNull Long id) {
		super(id);
	}

	public QuittanceDetailsPayment(@NotNull Stub<QuittanceDetailsPayment> stub) {
		super(stub.getId());
	}

	public QuittancePayment getPayment() {
		return payment;
	}

	public void setPayment(QuittancePayment payment) {
		this.payment = payment;
	}

	public QuittancePaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(QuittancePaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public QuittanceDetails getQuittanceDetails() {
		return quittanceDetails;
	}

	public void setQuittanceDetails(QuittanceDetails quittanceDetails) {
		this.quittanceDetails = quittanceDetails;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}