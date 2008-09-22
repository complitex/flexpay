package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RegistryRecord extends DomainObject {

	private SpRegistry spRegistry;
	private RegistryRecordStatus recordStatus;
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
	private List<RegistryRecordContainer> containers = Collections.emptyList();

	private AbstractConsumer consumer;
	private ImportError importError;
	private Apartment apartment;
	private Person person;
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
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
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
	public List<RegistryRecordContainer> getContainers() {
		return containers;
	}

	/**
	 * @param containers the containers to set
	 */
	public void setContainers(List<RegistryRecordContainer> containers) {
		this.containers = containers;
	}

	/**
	 * Getter for property 'account'.
	 *
	 * @return Value for property 'account'.
	 */
	public AbstractConsumer getConsumer() {
		return consumer;
	}

	/**
	 * Setter for property 'account'.
	 *
	 * @param consumer Value to set for property 'account'.
	 */
	public void setConsumer(AbstractConsumer consumer) {
		this.consumer = consumer;
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

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@SuppressWarnings ({"UnnecessaryBoxing"})
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("version", version)
				.append("status", recordStatus == null ? "-" : recordStatus.getI18nName())
				.append("status-id", recordStatus == null ? Long.valueOf(0) : recordStatus.getId())
				.append("registry-id", spRegistry == null ? Long.valueOf(0) : spRegistry.getId())
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
				.append("apartment-id", apartment == null ? Long.valueOf(0) : apartment.getId())
				.append("person-id", person == null ? Long.valueOf(0) : person.getId())
				.append("service-id", service == null ? Long.valueOf(0) : service.getId())
				.append("consumer-id", consumer == null ? Long.valueOf(0) : consumer.getId())
				.toString();
	}

	@Nullable
	public Stub<Person> getPersonStub() {
		if (person == null) {
			return null;
		}
		return new Stub<Person>(person);
	}

	@Nullable
	public Stub<Apartment> getApartmentStub() {
		if (apartment == null) {
			return null;
		}
		return new Stub<Apartment>(apartment);
	}
}
