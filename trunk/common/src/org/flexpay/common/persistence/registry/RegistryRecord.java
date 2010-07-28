package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RegistryRecord extends DomainObject {

	private String serviceCode;
	// лиц. счёт поставщика услуг
	private String personalAccountExt;
    private String townType;
	private String townName;
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

	private List<RegistryRecordContainer> containers = CollectionUtils.list();

	private RegistryRecordProperties properties;
	private ImportError importError;
	private Integer importErrorType;

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

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
    }

    public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
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

	public void addContainer(RegistryRecordContainer container) {
		container.setOrder(containers.size());
		container.setRecord(this);
		containers.add(container);
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
		importErrorType = importError != null ? importError.getObjectType() : null;
	}

	public Integer getImportErrorType() {
		return importErrorType;
	}

	public void setImportErrorType(Integer importErrorType) {
		this.importErrorType = importErrorType;
	}

	public RegistryRecordProperties getProperties() {
		return properties;
	}

	public void setProperties(RegistryRecordProperties properties) {
		properties.setRecord(this);
		this.properties = properties;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("serviceCode", serviceCode).
				append("personalAccountExt", personalAccountExt).
                append("townType", townType).
				append("townName", townName).
				append("streetType", streetType).
				append("streetName", streetName).
				append("buildingNum", buildingNum).
				append("buildingBulkNum", buildingBulkNum).
				append("apartmentNum", apartmentNum).
				append("firstName", firstName).
				append("middleName", middleName).
				append("lastName", lastName).
				append("operationDate", operationDate).
				append("uniqueOperationNumber", uniqueOperationNumber).
				append("amount", amount).
				toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		return true;
	}
}
