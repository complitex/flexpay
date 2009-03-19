package org.flexpay.eirc.process.quittance.report;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.persistence.ServiceType;
import static org.flexpay.eirc.process.quittance.report.util.SummUtil.addNegative;
import static org.flexpay.eirc.process.quittance.report.util.SummUtil.addNonNegative;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class ServiceTotalsBase implements Serializable {

	private ServiceType serviceType;
	private BigDecimal tarif = BigDecimal.ZERO;
	private BigDecimal expence = BigDecimal.ZERO;
	private String expenceUnitKey;
	private BigDecimal charges = BigDecimal.ZERO;
	private BigDecimal recalculation = BigDecimal.ZERO;
	private BigDecimal privilege = BigDecimal.ZERO;
	private BigDecimal subsidy = BigDecimal.ZERO;
	private BigDecimal payed = BigDecimal.ZERO;
	private BigDecimal incomingDebt = BigDecimal.ZERO;
	private BigDecimal outgoingDebt = BigDecimal.ZERO;
	private BigDecimal amount = BigDecimal.ZERO;

	protected ServiceTotalsBase(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public String getServiceTypeName() throws Exception {
		return TranslationUtil.getTranslation(serviceType.getTypeNames()).getName();
	}

	public Integer getServiceTypeCode() {
		return serviceType.getCode();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void addAmount(BigDecimal am) {
		amount = addNonNegative(amount, am);
	}

	public BigDecimal getTarif() {
		return tarif;
	}

	public void setTarif(BigDecimal tarif) {
		this.tarif = tarif;
	}

	public BigDecimal getExpence() {
		return expence;
	}

	/**
	 * Add amount to expence
	 *
	 * @param amount Summ to add
	 */
	public void addExpence(@NotNull BigDecimal amount) {
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
	public void addCharges(@NotNull BigDecimal amount) {
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
	public void addSubsidy(@NotNull BigDecimal amount) {
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
	public void addIncomingDebt(@NotNull BigDecimal amount) {
		incomingDebt = addNegative(incomingDebt, amount);
	}

	public BigDecimal getOutgoingDebt() {
		return outgoingDebt;
	}

	/**
	 * Add amount to outgoing debt
	 *
	 * @param amount Summ to add
	 */
	public void addOutgoingDebt(@NotNull BigDecimal amount) {
		outgoingDebt = addNegative(outgoingDebt, amount);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("serviceType", serviceType).
				append("tarif", tarif).
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

	public String[] getOutgoingDebtDigits() {

		String[] result = new String[6];
		for (int pos = -2; pos < 4; ++pos) {
			result[pos + 2] = StringUtil.getDigit(outgoingDebt, pos);
		}

		// remove leading zeros
		int pos;
		for (pos = 5; pos > 2; --pos) {
			if ("0".equals(result[pos])) {
				result[pos] = "";
			} else {
				break;
			}
		}

		// add minus if needed
		if (outgoingDebt.compareTo(BigDecimal.ZERO) < 0) {
			pos = pos == 2 ? 3 : pos;
			result[pos] = "- " + result[pos];
		}

		return result;
	}

}
