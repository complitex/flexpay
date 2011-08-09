package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class QuittancePayment extends DomainObject {

	private QuittancePacket packet;
	private QuittancePaymentStatus paymentStatus;
	private Quittance quittance;
	private BigDecimal amount;
	private Set<QuittanceDetailsPayment> detailsPayments = set();

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

	@Nullable
	public QuittanceDetailsPayment getPayment(QuittanceDetails qd) {

		for (QuittanceDetailsPayment payment : detailsPayments) {
			if (payment.getQuittanceDetails().equals(qd)) {
				return payment;
			}
		}

		return null;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void addDetailsPayment(QuittanceDetailsPayment payment) {
		payment.setPayment(this);
		if (detailsPayments == null) {
			detailsPayments = set();
		}
		detailsPayments.add(payment);
	}
}
