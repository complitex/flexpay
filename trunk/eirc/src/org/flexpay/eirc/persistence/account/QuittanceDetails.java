package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.SpRegistryRecord;

import java.math.BigDecimal;
import java.util.Date;

public class QuittanceDetails extends DomainObject {

	private Consumer consumer;
	private SpRegistryRecord registryRecord;

	private Service subservice;
	private BigDecimal incomingBalance;
	private BigDecimal outgoingBalance;
	private BigDecimal amount;
	private BigDecimal expence;
	private String rate;
	private BigDecimal recalculation;
	private BigDecimal benifit;
	private BigDecimal subsidy;
	private BigDecimal payment;
	private Date month;

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	public Service getSubservice() {
		return subservice;
	}

	public void setSubservice(Service subservice) {
		this.subservice = subservice;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getExpence() {
		return expence;
	}

	public void setExpence(BigDecimal expence) {
		this.expence = expence;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public BigDecimal getRecalculation() {
		return recalculation;
	}

	public void setRecalculation(BigDecimal recalculation) {
		this.recalculation = recalculation;
	}

	public BigDecimal getBenifit() {
		return benifit;
	}

	public void setBenifit(BigDecimal benifit) {
		this.benifit = benifit;
	}

	public BigDecimal getSubsidy() {
		return subsidy;
	}

	public void setSubsidy(BigDecimal subsidy) {
		this.subsidy = subsidy;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public SpRegistryRecord getRegistryRecord() {
		return registryRecord;
	}

	public void setRegistryRecord(SpRegistryRecord registryRecord) {
		this.registryRecord = registryRecord;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}
}
