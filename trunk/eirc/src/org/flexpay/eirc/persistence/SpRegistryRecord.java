package org.flexpay.eirc.persistence;

import java.util.Date;

import org.flexpay.common.persistence.DomainObject;

public class SpRegistryRecord extends DomainObject {
	private SpRegistry spRegistry;
	private Long serviceCode;
	private String personalAccount;
	private String city;
	private String streetType;
	private String streetName;
	private String buildingNum;
	private String buildingPartNum;
	private String apartamentNum;
	private String firstName;
	private String lastName;
	private Date date;
	private Long uno;
	private Long sum;
	private String containers;
	
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
	public String getPersonalAccount() {
		return personalAccount;
	}
	/**
	 * @param personalAccount the personalAccount to set
	 */
	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
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
	 * @return the buildingPartNum
	 */
	public String getBuildingPartNum() {
		return buildingPartNum;
	}
	/**
	 * @param buildingPartNum the buildingPartNum to set
	 */
	public void setBuildingPartNum(String buildingPartNum) {
		this.buildingPartNum = buildingPartNum;
	}
	/**
	 * @return the apartamentNum
	 */
	public String getApartamentNum() {
		return apartamentNum;
	}
	/**
	 * @param apartamentNum the apartamentNum to set
	 */
	public void setApartamentNum(String apartamentNum) {
		this.apartamentNum = apartamentNum;
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
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the uno
	 */
	public Long getUno() {
		return uno;
	}
	/**
	 * @param uno the uno to set
	 */
	public void setUno(Long uno) {
		this.uno = uno;
	}
	/**
	 * @return the sum
	 */
	public Long getSum() {
		return sum;
	}
	/**
	 * @param sum the sum to set
	 */
	public void setSum(Long sum) {
		this.sum = sum;
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

}
