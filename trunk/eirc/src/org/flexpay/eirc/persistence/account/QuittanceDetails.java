package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class QuittanceDetails extends DomainObject {

	private Consumer consumer;
	private RegistryRecord registryRecord;

	private BigDecimal incomingBalance;
	private BigDecimal outgoingBalance;
	private BigDecimal amount;
	private BigDecimal expence;
	private BigDecimal rate;
	private BigDecimal recalculation;
	private BigDecimal benifit;
	private BigDecimal subsidy;
	private BigDecimal payment;
	private Date month;

    private Set<QuittanceDetailsQuittance> quittanceDetailsQuittances = set();

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	@NotNull
	private BigDecimal value(@Nullable BigDecimal bd) {
		return bd == null ? BigDecimal.ZERO : bd;
	}

	@NotNull
	public BigDecimal getIncomingBalance() {
		return value(incomingBalance);
	}

	public void setIncomingBalance(BigDecimal incomingBalance) {
		this.incomingBalance = incomingBalance;
	}

	@NotNull
	public BigDecimal getOutgoingBalance() {
		return value(outgoingBalance);
	}

	public void setOutgoingBalance(BigDecimal outgoingBalance) {
		this.outgoingBalance = outgoingBalance;
	}

	@NotNull
	public BigDecimal getAmount() {
		return value(amount);
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@NotNull
	public BigDecimal getExpence() {
		return value(expence);
	}

	public void setExpence(BigDecimal expence) {
		this.expence = expence;
	}

	@NotNull
	public BigDecimal getRate() {
		return value(rate);
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@NotNull
	public BigDecimal getRecalculation() {
		return value(recalculation);
	}

	public void setRecalculation(BigDecimal recalculation) {
		this.recalculation = recalculation;
	}

	@NotNull
	public BigDecimal getBenifit() {
		return value(benifit);
	}

	public void setBenifit(BigDecimal benifit) {
		this.benifit = benifit;
	}

	@NotNull
	public BigDecimal getSubsidy() {
		return value(subsidy);
	}

	public void setSubsidy(BigDecimal subsidy) {
		this.subsidy = subsidy;
	}

	@NotNull
	public BigDecimal getPayment() {
		return value(payment);
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public RegistryRecord getRegistryRecord() {
		return registryRecord;
	}

	public void setRegistryRecord(RegistryRecord registryRecord) {
		this.registryRecord = registryRecord;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

    public Set<QuittanceDetailsQuittance> getQuittanceDetailsQuittances() {
        return quittanceDetailsQuittances;
    }

    public void setQuittanceDetailsQuittances(Set<QuittanceDetailsQuittance> quittanceDetailsQuittances) {
        this.quittanceDetailsQuittances = quittanceDetailsQuittances;
    }
}
