package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.account.Quittance;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.math.BigDecimal;

public class QuittancePayment extends DomainObject {

	private QuittancePacket packet;
	private QuittancePaymentStatus paymentStatus;
	private Quittance quittance;
	private BigDecimal amount;
	private Set<QuittanceDetailsPayment> detailsPayments = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public QuittancePayment() {
	}

	public QuittancePayment(@NotNull Long id) {
		super(id);
	}

	public QuittancePayment(@NotNull Stub<QuittancePayment> stub) {
		super(stub.getId());
	}

	public QuittancePacket getPacket() {
		return packet;
	}

	public void setPacket(QuittancePacket packet) {
		this.packet = packet;
	}

	public QuittancePaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(QuittancePaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Quittance getQuittance() {
		return quittance;
	}

	public void setQuittance(Quittance quittance) {
		this.quittance = quittance;
	}

	public Set<QuittanceDetailsPayment> getDetailsPayments() {
		return detailsPayments;
	}

	public void setDetailsPayments(Set<QuittanceDetailsPayment> detailsPayments) {
		this.detailsPayments = detailsPayments;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
