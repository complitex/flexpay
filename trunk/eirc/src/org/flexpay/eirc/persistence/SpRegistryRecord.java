package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.math.BigDecimal;
import java.util.Date;

public class SpRegistryRecord extends DomainObject {

	private SpRegistry spRegistry;
	private Long serviceCode;
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
	private String containers;

	private PersonalAccount account;
	private Service service;

	/**
	 * @return the spRegistry
	 */
	public SpRegistry getSpRegistry() {
		return spRegistry;
	}

	/**
	 * @param spRegistry the spRegistry to set
	 */
	public void setSpRegistry(SpRegistry spRegistry) {
		this.spRegistry = spRegistry;
	}

	/**
	 * @return the serviceCode
	 */
	public Long getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(Long serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the personalAccount
	 */
	public String getPersonalAccountExt() {
		return personalAccountExt;
	}

	/**
	 * @param personalAccountExt the personalAccount to set
	 */
	public void setPersonalAccountExt(String personalAccountExt) {
		this.personalAccountExt = personalAccountExt;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the streetType
	 */
	public String getStreetType() {
		return streetType;
	}

	/**
	 * @param streetType the streetType to set
	 */
	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	/**
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}

	/**
	 * @param streetName the streetName to set
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	/**
	 * @return the buildingNum
	 */
	public String getBuildingNum() {
		return buildingNum;
	}

	/**
	 * @param buildingNum the buildingNum to set
	 */
	public void setBuildingNum(String buildingNum) {
		this.buildingNum = buildingNum;
	}

	/**
	 * @return the buildingBulkNum
	 */
	public String getBuildingBulkNum() {
		return buildingBulkNum;
	}

	/**
	 * @param buildingBulkNum the buildingBulkNum to set
	 */
	public void setBuildingBulkNum(String buildingBulkNum) {
		this.buildingBulkNum = buildingBulkNum;
	}

	/**
	 * @return the apartmentNum
	 */
	public String getApartmentNum() {
		return apartmentNum;
	}

	/**
	 * @param apartmentNum the apartmentNum to set
	 */
	public void setApartmentNum(String apartmentNum) {
		this.apartmentNum = apartmentNum;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter for property 'middleName'.
	 *
	 * @return Value for property 'middleName'.
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Setter for property 'middleName'.
	 *
	 * @param middleName Value to set for property 'middleName'.
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the date
	 */
	public Date getOperationDate() {
		return operationDate;
	}

	/**
	 * @param operationDate the date to set
	 */
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	/**
	 * @return the uniqueOperationNumber
	 */
	public Long getUniqueOperationNumber() {
		return uniqueOperationNumber;
	}

	/**
	 * @param uniqueOperationNumber the uniqueOperationNumber to set
	 */
	public void setUniqueOperationNumber(Long uniqueOperationNumber) {
		this.uniqueOperationNumber = uniqueOperationNumber;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the containers
	 */
	public String getContainers() {
		return containers;
	}

	/**
	 * @param containers the containers to set
	 */
	public void setContainers(String containers) {
		this.containers = containers;
	}

	/**
	 * Getter for property 'account'.
	 *
	 * @return Value for property 'account'.
	 */
	public PersonalAccount getAccount() {
		return account;
	}

	/**
	 * Setter for property 'account'.
	 *
	 * @param account Value to set for property 'account'.
	 */
	public void setAccount(PersonalAccount account) {
		this.account = account;
	}

	/**
	 * Getter for property 'service'.
	 *
	 * @return Value for property 'service'.
	 */
	public Service getService() {
		return service;
	}

	/**
	 * Setter for property 'service'.
	 *
	 * @param service Value to set for property 'service'.
	 */
	public void setService(Service service) {
		this.service = service;
	}
}
