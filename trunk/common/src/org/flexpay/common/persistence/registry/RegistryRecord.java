package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RegistryRecord extends DomainObject {

	private String serviceCode;
	private String personalAccountExt;
	private String city;
	private String streetType;
	private String streetName;
	private String buildingNum;
	private String buildingBulkNum;
	private String apartmentNum;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date operationDate;
	private Long uniqueOperationNumber;
	private BigDecimal amount;

	private Registry registry;
	private RegistryRecordStatus recordStatus;

	private List<RegistryRecordContainer> containers = Collections.emptyList();

	private RegistryRecordProperties properties;
	private ImportError importError;

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getPersonalAccountExt() {
		return personalAccountExt;
	}

	public void setPersonalAccountExt(String personalAccountExt) {
		this.personalAccountExt = personalAccountExt;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getBuildingNum() {
		return buildingNum;
	}

	public void setBuildingNum(String buildingNum) {
		this.buildingNum = buildingNum;
	}

	public String getBuildingBulkNum() {
		return buildingBulkNum;
	}

	public void setBuildingBulkNum(String buildingBulkNum) {
		this.buildingBulkNum = buildingBulkNum;
	}

	public String getApartmentNum() {
		return apartmentNum;
	}

	public void setApartmentNum(String apartmentNum) {
		this.apartmentNum = apartmentNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public Long getUniqueOperationNumber() {
		return uniqueOperationNumber;
	}

	public void setUniqueOperationNumber(Long uniqueOperationNumber) {
		this.uniqueOperationNumber = uniqueOperationNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<RegistryRecordContainer> getContainers() {
		return containers;
	}

	public void setContainers(List<RegistryRecordContainer> containers) {
		this.containers = containers;
	}

	public RegistryRecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(RegistryRecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	public ImportError getImportError() {
		return importError;
	}

	public void setImportError(ImportError importError) {
		this.importError = importError;
	}

	public RegistryRecordProperties getProperties() {
		return properties;
	}

	public void setProperties(RegistryRecordProperties properties) {
		properties.setRecord(this);
		this.properties = properties;
	}

	@SuppressWarnings ({"UnnecessaryBoxing"})
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("version", version)
				.append("status", recordStatus == null ? "-" : recordStatus.getI18nName())
				.append("status-id", recordStatus == null ? Long.valueOf(0) : recordStatus.getId())
				.append("registry-id", registry == null ? Long.valueOf(0) : registry.getId())
				.append("code", getServiceCode())
				.append("amount", amount)
				.append("firstName", firstName)
				.append("middleName", middleName)
				.append("lastName", lastName)
				.append("city", city)
				.append("street", streetName)
				.append("streetType", streetType)
				.append("building", buildingNum)
				.append("bulk", buildingBulkNum)
				.append("apartment", apartmentNum)
				.toString();
	}

}
