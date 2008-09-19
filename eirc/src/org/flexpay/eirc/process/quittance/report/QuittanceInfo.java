package org.flexpay.eirc.process.quittance.report;

import org.flexpay.eirc.persistence.ServiceType;

import java.math.BigDecimal;
import java.util.*;

/**
 * Container for all necessary Quittance information with calculated summs, service
 * tarifs, subsidies, etc
 */
public class QuittanceInfo {

	private String quittanceNumber;
	private String apartmentAddress;
	private String buildingAddress;
	private String personFIO;

	private Date periodBeginDate;
	private Date periodEndDate;
	private Date operationDate;

	private BigDecimal summToPay;

	private BigDecimal totalSquare;
	private BigDecimal warmSquare;
	private BigDecimal privileges;
	private int habitantNumber;
	private int privilegersNumber;

	private BigDecimal incomingDebt;
	private BigDecimal outgoingDebt;

	private String jksBankAccount;
	private String bankAccount;

	private Map<ServiceType, ServiceTotals> servicesTotals;

	public QuittanceInfo() {
	}

	public String getQuittanceNumber() {
		return quittanceNumber;
	}

	public void setQuittanceNumber(String quittanceNumber) {
		this.quittanceNumber = quittanceNumber;
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

	public BigDecimal getSummToPay() {
		return summToPay;
	}

	public void setSummToPay(BigDecimal summToPay) {
		this.summToPay = summToPay;
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

	public BigDecimal getPrivileges() {
		return privileges;
	}

	public void setPrivileges(BigDecimal privileges) {
		this.privileges = privileges;
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

	public BigDecimal getIncomingDebt() {
		return incomingDebt;
	}

	public void setIncomingDebt(BigDecimal incomingDebt) {
		this.incomingDebt = incomingDebt;
	}

	public BigDecimal getOutgoingDebt() {
		return outgoingDebt;
	}

	public void setOutgoingDebt(BigDecimal outgoingDebt) {
		this.outgoingDebt = outgoingDebt;
	}

	public String getJksBankAccount() {
		return jksBankAccount;
	}

	public void setJksBankAccount(String jksBankAccount) {
		this.jksBankAccount = jksBankAccount;
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
		return new ArrayList<ServiceTotals>(set);
	}

	public void setServicesTotals(Map<ServiceType, ServiceTotals> servicesTotals) {
		this.servicesTotals = servicesTotals;
	}
}
