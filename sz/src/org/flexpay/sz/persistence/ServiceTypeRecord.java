package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Person;

public class ServiceTypeRecord extends Record {

	// District code
	private Integer extDistrictCode;
	// Organization code
	private Integer extOrganizationCode;
	// dwelling owner/deadhead id
	private Integer dwellingOwnerId;
	// dwelling owner/deadhead individual tax number
	private String dwellingOwnerTaxNumber;
	// dwelling owner/deadhead passport seria and document number
	private String dwellingOwnerPasport;
	// dwelling owner/deadhead first middle and last name
	private String dwellingOwnerName;
	// deadhead individual tax number
	private String deadheadTaxNumber;
	// deadhead passport seria and document number
	private String deadheadPassport;
	// deadhead first middle and last name
	private String deadheadName;
	// post office index
	private Integer postalCode;
	// street code
	private Integer extStreetCode;
	// building number
	private String buildingNumber;
	// optional bulk number
	private String bulkNumber;
	// apartment number
	private String apartmentNumber;
	// deadhead category
	private Integer deadheadCategory;
	// privilege code
	private Integer privilegeCode;
	// privilege start year
	private Integer privilegeStartYear;
	// privilege start month
	private Integer privilegeStartMonth;
	// privilege end year
	private Integer privilegeEndYear;
	// privilege end month
	private Integer privilegeEndMonth;
	// personal account number
	private String personalAccountNumber;
	// service type 
	private Integer serviceType;
	// tariff
	private Integer tariffCode;

	// privelege owner
	private Person deadhead;

	public Integer getExtDistrictCode() {
		return extDistrictCode;
	}

	public void setExtDistrictCode(Integer extDistrictCode) {
		this.extDistrictCode = extDistrictCode;
	}

	public Integer getExtOrganizationCode() {
		return extOrganizationCode;
	}

	public void setExtOrganizationCode(Integer extOrganizationCode) {
		this.extOrganizationCode = extOrganizationCode;
	}

	public Integer getDwellingOwnerId() {
		return dwellingOwnerId;
	}

	public void setDwellingOwnerId(Integer dwellingOwnerId) {
		this.dwellingOwnerId = dwellingOwnerId;
	}

	public String getDwellingOwnerTaxNumber() {
		return dwellingOwnerTaxNumber;
	}

	public void setDwellingOwnerTaxNumber(String dwellingOwnerTaxNumber) {
		this.dwellingOwnerTaxNumber = dwellingOwnerTaxNumber;
	}

	public String getDwellingOwnerPasport() {
		return dwellingOwnerPasport;
	}

	public void setDwellingOwnerPasport(String dwellingOwnerPasport) {
		this.dwellingOwnerPasport = dwellingOwnerPasport;
	}

	public String getDwellingOwnerName() {
		return dwellingOwnerName;
	}

	public void setDwellingOwnerName(String dwellingOwnerName) {
		this.dwellingOwnerName = dwellingOwnerName;
	}

	public String getDeadheadTaxNumber() {
		return deadheadTaxNumber;
	}

	public void setDeadheadTaxNumber(String deadheadTaxNumber) {
		this.deadheadTaxNumber = deadheadTaxNumber;
	}

	public String getDeadheadPassport() {
		return deadheadPassport;
	}

	public void setDeadheadPassport(String deadheadPassport) {
		this.deadheadPassport = deadheadPassport;
	}

	public String getDeadheadName() {
		return deadheadName;
	}

	public void setDeadheadName(String deadheadName) {
		this.deadheadName = deadheadName;
	}

	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	public Integer getExtStreetCode() {
		return extStreetCode;
	}

	public void setExtStreetCode(Integer extStreetCode) {
		this.extStreetCode = extStreetCode;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getBulkNumber() {
		return bulkNumber;
	}

	public void setBulkNumber(String bulkNumber) {
		this.bulkNumber = bulkNumber;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public Integer getDeadheadCategory() {
		return deadheadCategory;
	}

	public void setDeadheadCategory(Integer deadheadCategory) {
		this.deadheadCategory = deadheadCategory;
	}

	public Integer getPrivilegeCode() {
		return privilegeCode;
	}

	public void setPrivilegeCode(Integer privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	public Integer getPrivilegeStartYear() {
		return privilegeStartYear;
	}

	public void setPrivilegeStartYear(Integer privilegeStartYear) {
		this.privilegeStartYear = privilegeStartYear;
	}

	public Integer getPrivilegeStartMonth() {
		return privilegeStartMonth;
	}

	public void setPrivilegeStartMonth(Integer privilegeStartMonth) {
		this.privilegeStartMonth = privilegeStartMonth;
	}

	public Integer getPrivilegeEndYear() {
		return privilegeEndYear;
	}

	public void setPrivilegeEndYear(Integer privilegeEndYear) {
		this.privilegeEndYear = privilegeEndYear;
	}

	public Integer getPrivilegeEndMonth() {
		return privilegeEndMonth;
	}

	public void setPrivilegeEndMonth(Integer privilegeEndMonth) {
		this.privilegeEndMonth = privilegeEndMonth;
	}

	public String getPersonalAccountNumber() {
		return personalAccountNumber;
	}

	public void setPersonalAccountNumber(String personalAccountNumber) {
		this.personalAccountNumber = personalAccountNumber;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getTariffCode() {
		return tariffCode;
	}

	public void setTariffCode(Integer tariffCode) {
		this.tariffCode = tariffCode;
	}

	public Person getDeadhead() {
		return deadhead;
	}

	public void setDeadhead(Person deadhead) {
		this.deadhead = deadhead;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("tariffCode", tariffCode).
				append("extDistrictCode", extDistrictCode).
				append("extOrganizationCode", extOrganizationCode).
				append("dwellingOwnerId", dwellingOwnerId).
				append("dwellingOwnerTaxNumber", dwellingOwnerTaxNumber).
				append("dwellingOwnerPasport", dwellingOwnerPasport).
				append("dwellingOwnerName", dwellingOwnerName).
				append("deadheadTaxNumber", deadheadTaxNumber).
				append("deadheadPassport", deadheadPassport).
				append("deadheadName", deadheadName).
				append("postalCode", postalCode).
				append("extStreetCode", extStreetCode).
				append("buildingNumber", buildingNumber).
				append("bulkNumber", bulkNumber).
				append("apartmentNumber", apartmentNumber).
				append("deadheadCategory", deadheadCategory).
				append("privilegeCode", privilegeCode).
				append("privilegeStartYear", privilegeStartYear).
				append("privilegeStartMonth", privilegeStartMonth).
				append("privilegeEndYear", privilegeEndYear).
				append("privilegeEndMonth", privilegeEndMonth).
				append("personalAccountNumber", personalAccountNumber).
				append("serviceType", serviceType).
				toString();
	}

}
