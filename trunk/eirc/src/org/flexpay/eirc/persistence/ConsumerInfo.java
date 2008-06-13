package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Set;
import java.util.Collections;

/**
 * Consumer info is a general consumer info given by provider
 */
public class ConsumerInfo extends DomainObjectWithStatus {

	private String firstName;
	private String middleName;
	private String lastName;

	// address information
	private String cityName;
	private String streetTypeName;
	private String streetName;
	private String buildingNumber;
	private String buildingBulk;
	private String apartmentNumber;

	private Set<Consumer> consumers = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public ConsumerInfo() {
	}

	public ConsumerInfo(Long id) {
		super(id);
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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStreetTypeName() {
		return streetTypeName;
	}

	public void setStreetTypeName(String streetTypeName) {
		this.streetTypeName = streetTypeName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getBuildingBulk() {
		return buildingBulk;
	}

	public void setBuildingBulk(String buildingBulk) {
		this.buildingBulk = buildingBulk;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public Set<Consumer> getConsumers() {
		return consumers;
	}

	public void setConsumers(Set<Consumer> consumers) {
		this.consumers = consumers;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("firstName", getFirstName())
				.append("middleName", getMiddleName())
				.append("lastName", getLastName())
				.append("city", getCityName())
				.append("street", getStreetName() )
				.append("streetType", getStreetTypeName())
				.append("building", getBuildingNumber())
				.append("bulk", getBuildingBulk())
				.append("apartment", getApartmentNumber())
				.toString();
	}
}
