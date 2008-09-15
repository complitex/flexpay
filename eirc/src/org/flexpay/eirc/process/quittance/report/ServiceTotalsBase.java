package org.flexpay.eirc.process.quittance.report;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.eirc.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public abstract class ServiceTotalsBase {

	private ServiceType serviceType;
	private String rate = "";
	private BigDecimal expence = BigDecimal.ZERO;
	private String expenceUnitKey;
	private BigDecimal charges = BigDecimal.ZERO;
	private BigDecimal recalculation = BigDecimal.ZERO;
	private BigDecimal privilege = BigDecimal.ZERO;
	private BigDecimal subsidy = BigDecimal.ZERO;
	private BigDecimal payed = BigDecimal.ZERO;
	private BigDecimal incomingDebt = BigDecimal.ZERO;
	private BigDecimal outgoingDebt = BigDecimal.ZERO;

	protected ServiceTotalsBase(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public BigDecimal getExpence() {
		return expence;
	}

	/**
	 * Add amount to expence
	 *
	 * @param amount Summ to add
	 */
	public void addExpence(BigDecimal amount) {
		expence = addNonNegative(expence, amount);
	}

	public String getExpenceUnitKey() {
		return expenceUnitKey;
	}

	public void setExpenceUnitKey(String expenceUnitKey) {
		this.expenceUnitKey = expenceUnitKey;
	}

	public BigDecimal getCharges() {
		return charges;
	}

	/**
	 * Add amount to charges
	 *
	 * @param amount Summ to add
	 */
	public void addCharges(BigDecimal amount) {
		charges = addNonNegative(charges, amount);
	}

	public BigDecimal getRecalculation() {
		return recalculation;
	}

	/**
	 * Add amount to recalculation
	 *
	 * @param amount Summ to add
	 */
	public void addRecalculation(BigDecimal amount) {
		recalculation = addNonNegative(recalculation, amount);
	}

	public BigDecimal getPrivilege() {
		return privilege;
	}

	/**
	 * Add amount to charges
	 *
	 * @param amount Summ to add
	 */
	public void addPrivilege(BigDecimal amount) {
		privilege = addNonNegative(privilege, amount);
	}

	public BigDecimal getSubsidy() {
		return subsidy;
	}

	/**
	 * Add amount to subsidy
	 *
	 * @param amount Summ to add
	 */
	public void addSubsidy(BigDecimal amount) {
		subsidy = addNonNegative(subsidy, amount);
	}

	public BigDecimal getPayed() {
		return payed;
	}

	/**
	 * Add amount to payed summ
	 *
	 * @param amount Summ to add
	 */
	public void addPayed(BigDecimal amount) {
		payed = addNonNegative(payed, amount);
	}

	public BigDecimal getIncomingDebt() {
		return incomingDebt;
	}

	/**
	 * Add amount to outgoing debt
	 *
	 * @param amount Summ to add
	 */
	public void addIncomingDebt(BigDecimal amount) {
		incomingDebt = addNonNegative(incomingDebt, amount);
	}

	public BigDecimal getOutgoingDebt() {
		return outgoingDebt;
	}

	/**
	 * Add amount to outgoing debt
	 *
	 * @param amount Summ to add
	 */
	public void addOutgoingDebt(BigDecimal amount) {
		outgoingDebt = addNonNegative(outgoingDebt, amount);
	}

	private BigDecimal addNonNegative(@NotNull BigDecimal current, @NotNull BigDecimal val) {

		if (val.compareTo(BigDecimal.ZERO) < 0) {
			return current;
		}

		return current.add(val);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("serviceType", serviceType).
				append("rate", rate).
				append("expence", expence).
				append("expenceUnitKey", expenceUnitKey).
				append("charges", charges).
				append("recalculation", recalculation).
				append("privilege", privilege).
				append("subsidy", subsidy).
				append("payed", payed).
				append("incomingDebt", incomingDebt).
				append("outgoingDebt", outgoingDebt).
				toString();
	}
}
