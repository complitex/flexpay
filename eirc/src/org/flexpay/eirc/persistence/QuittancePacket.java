package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class QuittancePacket extends DomainObjectWithStatus {

	private Long packetNumber;
	private Date creationDate;
	private Date beginDate;
	private Date closeDate;
	private PaymentPoint paymentPoint;
	private Integer controlQuittanciesNumber;
	private BigDecimal controlOverallSumm;
	private Integer quittanciesNumber;
	private BigDecimal overallSumm;
	private String creatorUserName;
	private String closerUserName;

	private Set<QuittancePayment> payments = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public QuittancePacket() {
	}

	public QuittancePacket(@NotNull Long id) {
		super(id);
	}

	public QuittancePacket(@NotNull Stub<QuittancePacket> stub) {
		super(stub.getId());
	}

	public Long getPacketNumber() {
		return packetNumber;
	}

	public void setPacketNumber(Long packetNumber) {
		this.packetNumber = packetNumber;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public PaymentPoint getPaymentPoint() {
		return paymentPoint;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	public Integer getControlQuittanciesNumber() {
		return controlQuittanciesNumber;
	}

	public void setControlQuittanciesNumber(Integer controlQuittanciesNumber) {
		this.controlQuittanciesNumber = controlQuittanciesNumber;
	}

	public BigDecimal getControlOverallSumm() {
		return controlOverallSumm;
	}

	public void setControlOverallSumm(BigDecimal controlOverallSumm) {
		this.controlOverallSumm = controlOverallSumm;
	}

	public Integer getQuittanciesNumber() {
		return quittanciesNumber;
	}

	public void setQuittanciesNumber(Integer quittanciesNumber) {
		this.quittanciesNumber = quittanciesNumber;
	}

	public BigDecimal getOverallSumm() {
		return overallSumm;
	}

	public void setOverallSumm(BigDecimal overallSumm) {
		this.overallSumm = overallSumm;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public String getCloserUserName() {
		return closerUserName;
	}

	public void setCloserUserName(String closerUserName) {
		this.closerUserName = closerUserName;
	}

	public Set<QuittancePayment> getPayments() {
		return payments;
	}

	public void setPayments(Set<QuittancePayment> payments) {
		this.payments = payments;
	}
}
