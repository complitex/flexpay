package org.flexpay.eirc.process.quittance.report;

import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Container for all necessary Quittance information with calculated summs, service
 * tarifs, subsidies, etc
 */
public class QuittanceInfo {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

	private boolean addressStub = false;

	private String quittanceNumber;
	private String apartmentAddress;
	private String buildingAddress;
	private String batchBuildingAddress;
	private String personFIO;

	private Date periodBeginDate;
	private Date periodEndDate;
	private Date operationDate;

	private BigDecimal totalSquare;
	private BigDecimal warmSquare;
	private int habitantNumber;
	private int privilegersNumber;

	private BigDecimal incomingBalance = BigDecimal.ZERO;
	private BigDecimal outgoingBalance = BigDecimal.ZERO;
	private BigDecimal charges = BigDecimal.ZERO;
	private BigDecimal recalculation = BigDecimal.ZERO;
	private BigDecimal privilege = BigDecimal.ZERO;
	private BigDecimal subsidy = BigDecimal.ZERO;
	private BigDecimal payed = BigDecimal.ZERO;

	private String serviceOrganisationName;
	private String bankName;
	private String serviceOrganisationAccount;
	private String bankAccount;

	private Map<ServiceType, ServiceTotals> servicesTotals = Collections.emptyMap();

	public QuittanceInfo() {
	}

	public Boolean getNotAddressStub() {
		return !addressStub;
	}

	public void setAddressStub(boolean addressStub) {
		this.addressStub = addressStub;
	}

	public String getQuittanceNumber() {
		return quittanceNumber;
	}

	public void setQuittanceNumber(String quittanceNumber) {
		this.quittanceNumber = quittanceNumber;
	}

	public String getQuittanceNumberWithSumm() {
		return getQuittanceNumber() + ";" + DECIMAL_FORMAT.format(outgoingBalance);
	}

	public String getApartmentAddress() {
		return apartmentAddress;
	}

	public void setApartmentAddress(String apartmentAddress) {
		this.apartmentAddress = apartmentAddress;
	}

	public String getBuildingAddress() {
		return buildingAddress;
	}

	public void setBuildingAddress(String buildingAddress) {
		this.buildingAddress = buildingAddress;
	}

	public String getBatchBuildingAddress() {
		return batchBuildingAddress;
	}

	public void setBatchBuildingAddress(String batchBuildingAddress) {
		this.batchBuildingAddress = batchBuildingAddress;
	}

	public String getPersonFIO() {
		return personFIO;
	}

	public void setPersonFIO(String personFIO) {
		this.personFIO = personFIO;
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

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public BigDecimal getTotalSquare() {
		return totalSquare;
	}

	public void setTotalSquare(BigDecimal totalSquare) {
		this.totalSquare = totalSquare;
	}

	public BigDecimal getWarmSquare() {
		return warmSquare;
	}

	public void setWarmSquare(BigDecimal warmSquare) {
		this.warmSquare = warmSquare;
	}

	public int getHabitantNumber() {
		return habitantNumber;
	}

	public void setHabitantNumber(int habitantNumber) {
		this.habitantNumber = habitantNumber;
	}

	public int getPrivilegersNumber() {
		return privilegersNumber;
	}

	public void setPrivilegersNumber(int privilegersNumber) {
		this.privilegersNumber = privilegersNumber;
	}

	public String getServiceOrganisationAccount() {
		return serviceOrganisationAccount;
	}

	public void setServiceOrganisationAccount(String serviceOrganisationAccount) {
		this.serviceOrganisationAccount = serviceOrganisationAccount;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Map<ServiceType, ServiceTotals> getServicesTotalsMap() {
		return servicesTotals;
	}

	public Collection<ServiceTotals> getServicesTotals() {
		return servicesTotals.values();
	}

	public List<ServiceTotals> getServicesTotalsList() {
		SortedSet<ServiceTotals> set = new TreeSet<ServiceTotals>(new ServiceTotalsComparator<ServiceTotals>());
		set.addAll(servicesTotals.values());
		List<ServiceTotals> result = CollectionUtils.list();
		result.addAll(set);
		return result;
	}

	public void setServicesTotals(Map<ServiceType, ServiceTotals> servicesTotals) {
		this.servicesTotals = servicesTotals;
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

	public BigDecimal getCharges() {
		return charges;
	}

	public void setCharges(BigDecimal charges) {
		this.charges = charges;
	}

	public BigDecimal getRecalculation() {
		return recalculation;
	}

	public void setRecalculation(BigDecimal recalculation) {
		this.recalculation = recalculation;
	}

	public BigDecimal getPrivilege() {
		return privilege;
	}

	public void setPrivilege(BigDecimal privilege) {
		this.privilege = privilege;
	}

	public BigDecimal getSubsidy() {
		return subsidy;
	}

	public void setSubsidy(BigDecimal subsidy) {
		this.subsidy = subsidy;
	}

	public BigDecimal getPayed() {
		return payed;
	}

	public void setPayed(BigDecimal payed) {
		this.payed = payed;
	}

	public String getServiceOrganisationName() {
		return serviceOrganisationName;
	}

	public void setServiceOrganisationName(String serviceOrganisationName) {
		this.serviceOrganisationName = serviceOrganisationName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getFullQuittanceInfo() {
		return "Barcode 2D text go here";
	}

	public String[] getOutgoingBalanceDigits() {

		String[] result = new String[6];
		for (int pos = -2; pos < 4; ++pos) {
			result[pos + 2] = StringUtil.getDigit(outgoingBalance, pos);
		}

		// remove leading zeros
		for (int pos = 5; pos > 2; --pos) {
			if ("0".equals(result[pos])) {
				result[pos] = "";
			} else {
				break;
			}
		}

		return result;

	}
}
