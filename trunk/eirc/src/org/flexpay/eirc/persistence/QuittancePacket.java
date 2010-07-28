package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class QuittancePacket extends DomainObjectWithStatus {

	private Long packetNumber;
	private Date creationDate;
	private Date beginDate = getFutureInfinite();
	private Date closeDate = getFutureInfinite();
	private PaymentPoint paymentPoint;
	private Integer controlQuittanciesNumber;
	private BigDecimal controlOverallSum;
	private Integer quittanciesNumber = 0;
	private BigDecimal overallSum = new BigDecimal("0.00");
	private String creatorUserName;
	private String closerUserName;

	private Set<QuittancePayment> payments = Collections.emptySet();

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

	public BigDecimal getControlOverallSum() {
		return controlOverallSum;
	}

	public void setControlOverallSum(BigDecimal controlOverallSum) {
		this.controlOverallSum = controlOverallSum;
	}

	public Integer getQuittanciesNumber() {
		return quittanciesNumber;
	}

	public void setQuittanciesNumber(Integer quittanciesNumber) {
		this.quittanciesNumber = quittanciesNumber;
	}

	public BigDecimal getOverallSum() {
		return overallSum;
	}

	public void setOverallSum(BigDecimal overallSum) {
		this.overallSum = overallSum;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("status", getStatus()).
				append("packetNumber", packetNumber).
				append("creationDate", creationDate).
				append("beginDate", beginDate).
				append("closeDate", closeDate).
				append("paymentPoint", paymentPoint).
				append("controlQuittanciesNumber", controlQuittanciesNumber).
				append("controlOverallSum", controlOverallSum).
				append("quittanciesNumber", quittanciesNumber).
				append("overallSum", overallSum).
				append("creatorUserName", creatorUserName).
				append("closerUserName", closerUserName).
				toString();
	}

}
