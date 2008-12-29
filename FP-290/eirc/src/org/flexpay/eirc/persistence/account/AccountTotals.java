package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.math.BigDecimal;
import java.util.Date;

public class AccountTotals extends DomainObjectWithStatus {

	private Account account;
	private BigDecimal incomingBalance;
	private BigDecimal outgoingBalance;
	private BigDecimal payments;
	private BigDecimal paymentsReturn;
	private BigDecimal charges;
	private BigDecimal corrections;
	private BigDecimal otherOperations;
	private BigDecimal debet;
	private BigDecimal credit;
	private Date periodBeginDate;
	private Date periodEndDate;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public BigDecimal getIncomingBalance() {
		return incomingBalance;
	}

	public void setIncomingBalance(BigDecimal incomingBalance) {
		this.incomingBalance = incomingBalance;
	}

	public BigDecimal getOutgoingBalance() {
		return outgoingBalance;
	}

	public void setOutgoingBalance(BigDecimal outgoingBalance) {
		this.outgoingBalance = outgoingBalance;
	}

	public BigDecimal getPayments() {
		return payments;
	}

	public void setPayments(BigDecimal payments) {
		this.payments = payments;
	}

	public BigDecimal getPaymentsReturn() {
		return paymentsReturn;
	}

	public void setPaymentsReturn(BigDecimal paymentsReturn) {
		this.paymentsReturn = paymentsReturn;
	}

	public BigDecimal getCharges() {
		return charges;
	}

	public void setCharges(BigDecimal charges) {
		this.charges = charges;
	}

	public BigDecimal getCorrections() {
		return corrections;
	}

	public void setCorrections(BigDecimal corrections) {
		this.corrections = corrections;
	}

	public BigDecimal getOtherOperations() {
		return otherOperations;
	}

	public void setOtherOperations(BigDecimal otherOperations) {
		this.otherOperations = otherOperations;
	}

	public BigDecimal getDebet() {
		return debet;
	}

	public void setDebet(BigDecimal debet) {
		this.debet = debet;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public Date getPeriodBeginDate() {
		return periodBeginDate;
	}

	public void setPeriodBeginDate(Date periodBeginDate) {
		this.periodBeginDate = periodBeginDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	
}
