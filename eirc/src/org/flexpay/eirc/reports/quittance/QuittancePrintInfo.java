package org.flexpay.eirc.reports.quittance;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.StringUtil;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.eirc.persistence.account.Quittance;
import static org.flexpay.eirc.process.quittance.report.util.SumUtil.addNonNegative;
import org.flexpay.eirc.process.quittance.report.ServiceTotals;
import org.flexpay.eirc.process.quittance.report.ServiceTotalsComparator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Container for all necessary Quittance information with calculated sums, service
 * tariffs, subsidies, etc
 */
public class QuittancePrintInfo implements Cloneable, Serializable {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

	private boolean addressStub = false;
	private boolean emptyInfo = false;

	private Stub<Quittance> quittanceStub;
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

	private String serviceOrganizationName;
	private String bankName;
	private String serviceOrganizationAccount;
	private String bankAccount;

	private Map<ServiceType, ServiceTotals> servicesTotals = Collections.emptyMap();

	public QuittancePrintInfo() {
	}

	public Stub<Quittance> getQuittanceStub() {
		return quittanceStub;
	}

	public void setQuittanceStub(Stub<Quittance> quittanceStub) {
		this.quittanceStub = quittanceStub;
	}

	public Long getQuittanceId() {
		return quittanceStub == null ? null : quittanceStub.getId();
	}

	public Boolean getNotAddressStub() {
		return !addressStub;
	}

	public void setAddressStub(boolean addressStub) {
		this.addressStub = addressStub;
	}

	public Boolean getNotEmptyInfo() {
		return !emptyInfo;
	}

	public void setEmptyInfo(boolean emptyInfo) {
		this.emptyInfo = emptyInfo;
	}

	public String getQuittanceNumber() {
		return quittanceNumber;
	}

	public void setQuittanceNumber(String quittanceNumber) {
		this.quittanceNumber = quittanceNumber;
	}

	public String getQuittanceNumberWithSum() {
		return getQuittanceNumber() + ";" + DECIMAL_FORMAT.format(getOutgoingBalance());
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

	public String getServiceOrganizationAccount() {
		return serviceOrganizationAccount;
	}

	public void setServiceOrganizationAccount(String serviceOrganizationAccount) {
		this.serviceOrganizationAccount = serviceOrganizationAccount;
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
		BigDecimal value = BigDecimal.ZERO;
		for (ServiceTotals total : getServicesTotals()) {
			value = addNonNegative(value, total.getIncomingDebt());
		}
		return value;
	}

	public BigDecimal getOutgoingBalance() {
		BigDecimal value = BigDecimal.ZERO;
		for (ServiceTotals total : getServicesTotals()) {
			value = addNonNegative(value, total.getAmount());
		}
		return value;
	}

	public BigDecimal getCharges() {
		BigDecimal value = BigDecimal.ZERO;
		for (ServiceTotals total : getServicesTotals()) {
			value = addNonNegative(value, total.getCharges());
		}
		return value;
	}

	public BigDecimal getRecalculation() {
		BigDecimal value = BigDecimal.ZERO;
		for (ServiceTotals total : getServicesTotals()) {
			value = addNonNegative(value, total.getRecalculation());
		}
		return value;
	}

	public BigDecimal getPrivilege() {
		BigDecimal value = BigDecimal.ZERO;
		for (ServiceTotals total : getServicesTotals()) {
			value = addNonNegative(value, total.getPrivilege());
		}
		return value;
	}

	public BigDecimal getSubsidy() {
		BigDecimal value = BigDecimal.ZERO;
		for (ServiceTotals total : getServicesTotals()) {
			value = addNonNegative(value, total.getSubsidy());
		}
		return value;
	}

	public BigDecimal getPayed() {
		BigDecimal value = BigDecimal.ZERO;
		for (ServiceTotals total : getServicesTotals()) {
			value = addNonNegative(value, total.getPayed());
		}
		return value;
	}

	public String getServiceOrganizationName() {
		return serviceOrganizationName;
	}

	public void setServiceOrganizationName(String serviceOrganizationName) {
		this.serviceOrganizationName = serviceOrganizationName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getFullQuittanceInfo() throws Exception {

		// add common info
		StringBuilder sb = new StringBuilder()
				.append(getQuittanceNumber()).append(";")
				.append(getApartmentAddress()).append(";")
				.append(getPersonFIO()).append(";")
				.append(getOutgoingBalance()).append((char)0x0A).append((char)0x0D);

		// now add services details
		for (ServiceTotals totals : getServicesTotalsList()) {
			sb.append(totals.getServiceTypeName()).append(";")
					.append(totals.getOutgoingDebt()).append((char)0x0A).append((char)0x0D);
		}

		return sb.toString();
	}

	public String[] getOutgoingBalanceDigits() {

		BigDecimal outgoingBalance = getOutgoingBalance();
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

	@Override
	public QuittancePrintInfo clone() throws CloneNotSupportedException {
		return (QuittancePrintInfo) super.clone();
	}

}
