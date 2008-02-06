package org.flexpay.sz.persistence;

import org.flexpay.ab.persistence.Person;

public class ServiceTypeRecord extends Record {

	// District code
	private Integer extDistrictCode;
	// Organisation code
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
	// tarif
	private Integer tarifCode;

	// privelege owner
	private Person deadhead;

	/**
	 * Getter for property 'cod'.
	 *
	 * @return Value for property 'cod'.
	 */
	public Integer getExtDistrictCode() {
		return extDistrictCode;
	}

	/**
	 * Setter for property 'cod'.
	 *
	 * @param extDistrictCode Value to set for property 'cod'.
	 */
	public void setExtDistrictCode(Integer extDistrictCode) {
		this.extDistrictCode = extDistrictCode;
	}

	/**
	 * Getter for property 'cdrp'.
	 *
	 * @return Value for property 'cdrp'.
	 */
	public Integer getExtOrganizationCode() {
		return extOrganizationCode;
	}

	/**
	 * Setter for property 'cdrp'.
	 *
	 * @param extOrganizationCode Value to set for property 'cdrp'.
	 */
	public void setExtOrganizationCode(Integer extOrganizationCode) {
		this.extOrganizationCode = extOrganizationCode;
	}

	/**
	 * Getter for property 'ncard'.
	 *
	 * @return Value for property 'ncard'.
	 */
	public Integer getDwellingOwnerId() {
		return dwellingOwnerId;
	}

	/**
	 * Setter for property 'ncard'.
	 *
	 * @param dwellingOwnerId Value to set for property 'ncard'.
	 */
	public void setDwellingOwnerId(Integer dwellingOwnerId) {
		this.dwellingOwnerId = dwellingOwnerId;
	}

	/**
	 * Getter for property 'idcode'.
	 *
	 * @return Value for property 'idcode'.
	 */
	public String getDwellingOwnerTaxNumber() {
		return dwellingOwnerTaxNumber;
	}

	/**
	 * Setter for property 'idcode'.
	 *
	 * @param dwellingOwnerTaxNumber Value to set for property 'idcode'.
	 */
	public void setDwellingOwnerTaxNumber(String dwellingOwnerTaxNumber) {
		this.dwellingOwnerTaxNumber = dwellingOwnerTaxNumber;
	}

	/**
	 * Getter for property 'pasp'.
	 *
	 * @return Value for property 'pasp'.
	 */
	public String getDwellingOwnerPasport() {
		return dwellingOwnerPasport;
	}

	/**
	 * Setter for property 'pasp'.
	 *
	 * @param dwellingOwnerPasport Value to set for property 'pasp'.
	 */
	public void setDwellingOwnerPasport(String dwellingOwnerPasport) {
		this.dwellingOwnerPasport = dwellingOwnerPasport;
	}

	/**
	 * Getter for property 'fio'.
	 *
	 * @return Value for property 'fio'.
	 */
	public String getDwellingOwnerName() {
		return dwellingOwnerName;
	}

	/**
	 * Setter for property 'fio'.
	 *
	 * @param dwellingOwnerName Value to set for property 'fio'.
	 */
	public void setDwellingOwnerName(String dwellingOwnerName) {
		this.dwellingOwnerName = dwellingOwnerName;
	}

	/**
	 * Getter for property 'idpil'.
	 *
	 * @return Value for property 'idpil'.
	 */
	public String getDeadheadTaxNumber() {
		return deadheadTaxNumber;
	}

	/**
	 * Setter for property 'idpil'.
	 *
	 * @param deadheadTaxNumber Value to set for property 'idpil'.
	 */
	public void setDeadheadTaxNumber(String deadheadTaxNumber) {
		this.deadheadTaxNumber = deadheadTaxNumber;
	}

	/**
	 * Getter for property 'pasppil'.
	 *
	 * @return Value for property 'pasppil'.
	 */
	public String getDeadheadPassport() {
		return deadheadPassport;
	}

	/**
	 * Setter for property 'pasppil'.
	 *
	 * @param deadheadPassport Value to set for property 'pasppil'.
	 */
	public void setDeadheadPassport(String deadheadPassport) {
		this.deadheadPassport = deadheadPassport;
	}

	/**
	 * Getter for property 'fiopil'.
	 *
	 * @return Value for property 'fiopil'.
	 */
	public String getDeadheadName() {
		return deadheadName;
	}

	/**
	 * Setter for property 'fiopil'.
	 *
	 * @param deadheadName Value to set for property 'fiopil'.
	 */
	public void setDeadheadName(String deadheadName) {
		this.deadheadName = deadheadName;
	}

	/**
	 * Getter for property 'index'.
	 *
	 * @return Value for property 'index'.
	 */
	public Integer getPostalCode() {
		return postalCode;
	}

	/**
	 * Setter for property 'index'.
	 *
	 * @param postalCode Value to set for property 'index'.
	 */
	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * Getter for property 'cdul'.
	 *
	 * @return Value for property 'cdul'.
	 */
	public Integer getExtStreetCode() {
		return extStreetCode;
	}

	/**
	 * Setter for property 'cdul'.
	 *
	 * @param extStreetCode Value to set for property 'cdul'.
	 */
	public void setExtStreetCode(Integer extStreetCode) {
		this.extStreetCode = extStreetCode;
	}

	/**
	 * Getter for property 'house'.
	 *
	 * @return Value for property 'house'.
	 */
	public String getBuildingNumber() {
		return buildingNumber;
	}

	/**
	 * Setter for property 'house'.
	 *
	 * @param buildingNumber Value to set for property 'house'.
	 */
	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	/**
	 * Getter for property 'build'.
	 *
	 * @return Value for property 'build'.
	 */
	public String getBulkNumber() {
		return bulkNumber;
	}

	/**
	 * Setter for property 'build'.
	 *
	 * @param bulkNumber Value to set for property 'build'.
	 */
	public void setBulkNumber(String bulkNumber) {
		this.bulkNumber = bulkNumber;
	}

	/**
	 * Getter for property 'apt'.
	 *
	 * @return Value for property 'apt'.
	 */
	public String getApartmentNumber() {
		return apartmentNumber;
	}

	/**
	 * Setter for property 'apt'.
	 *
	 * @param apartmentNumber Value to set for property 'apt'.
	 */
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	/**
	 * Getter for property 'kat'.
	 *
	 * @return Value for property 'kat'.
	 */
	public Integer getDeadheadCategory() {
		return deadheadCategory;
	}

	/**
	 * Setter for property 'kat'.
	 *
	 * @param deadheadCategory Value to set for property 'kat'.
	 */
	public void setDeadheadCategory(Integer deadheadCategory) {
		this.deadheadCategory = deadheadCategory;
	}

	/**
	 * Getter for property 'lgcode'.
	 *
	 * @return Value for property 'lgcode'.
	 */
	public Integer getPrivilegeCode() {
		return privilegeCode;
	}

	/**
	 * Setter for property 'lgcode'.
	 *
	 * @param privilegeCode Value to set for property 'lgcode'.
	 */
	public void setPrivilegeCode(Integer privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	/**
	 * Getter for property 'yearin'.
	 *
	 * @return Value for property 'yearin'.
	 */
	public Integer getPrivilegeStartYear() {
		return privilegeStartYear;
	}

	/**
	 * Setter for property 'yearin'.
	 *
	 * @param privilegeStartYear Value to set for property 'yearin'.
	 */
	public void setPrivilegeStartYear(Integer privilegeStartYear) {
		this.privilegeStartYear = privilegeStartYear;
	}

	/**
	 * Getter for property 'monthin'.
	 *
	 * @return Value for property 'monthin'.
	 */
	public Integer getPrivilegeStartMonth() {
		return privilegeStartMonth;
	}

	/**
	 * Setter for property 'monthin'.
	 *
	 * @param privilegeStartMonth Value to set for property 'monthin'.
	 */
	public void setPrivilegeStartMonth(Integer privilegeStartMonth) {
		this.privilegeStartMonth = privilegeStartMonth;
	}

	/**
	 * Getter for property 'yearout'.
	 *
	 * @return Value for property 'yearout'.
	 */
	public Integer getPrivilegeEndYear() {
		return privilegeEndYear;
	}

	/**
	 * Setter for property 'yearout'.
	 *
	 * @param privilegeEndYear Value to set for property 'yearout'.
	 */
	public void setPrivilegeEndYear(Integer privilegeEndYear) {
		this.privilegeEndYear = privilegeEndYear;
	}

	/**
	 * Getter for property 'monthout'.
	 *
	 * @return Value for property 'monthout'.
	 */
	public Integer getPrivilegeEndMonth() {
		return privilegeEndMonth;
	}

	/**
	 * Setter for property 'monthout'.
	 *
	 * @param privilegeEndMonth Value to set for property 'monthout'.
	 */
	public void setPrivilegeEndMonth(Integer privilegeEndMonth) {
		this.privilegeEndMonth = privilegeEndMonth;
	}

	/**
	 * Getter for property 'rah'.
	 *
	 * @return Value for property 'rah'.
	 */
	public String getPersonalAccountNumber() {
		return personalAccountNumber;
	}

	/**
	 * Setter for property 'rah'.
	 *
	 * @param personalAccountNumber Value to set for property 'rah'.
	 */
	public void setPersonalAccountNumber(String personalAccountNumber) {
		this.personalAccountNumber = personalAccountNumber;
	}

	/**
	 * Getter for property 'serviceType'.
	 *
	 * @return Value for property 'serviceType'.
	 */
	public Integer getServiceType() {
		return serviceType;
	}

	/**
	 * Setter for property 'serviceType'.
	 *
	 * @param serviceType Value to set for property 'serviceType'.
	 */
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * Getter for property 'tarifCode'.
	 *
	 * @return Value for property 'tarifCode'.
	 */
	public Integer getTarifCode() {
		return tarifCode;
	}

	/**
	 * Setter for property 'tarifCode'.
	 *
	 * @param tarifCode Value to set for property 'tarifCode'.
	 */
	public void setTarifCode(Integer tarifCode) {
		this.tarifCode = tarifCode;
	}

	/**
	 * Getter for property 'deadhead'.
	 *
	 * @return Value for property 'deadhead'.
	 */
	public Person getDeadhead() {
		return deadhead;
	}

	/**
	 * Setter for property 'deadhead'.
	 *
	 * @param deadhead Value to set for property 'deadhead'.
	 */
	public void setDeadhead(Person deadhead) {
		this.deadhead = deadhead;
	}
}
