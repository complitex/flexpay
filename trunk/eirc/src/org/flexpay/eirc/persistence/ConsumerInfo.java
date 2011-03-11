package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

/**
 * Consumer info is a general consumer info given by provider
 */
public class ConsumerInfo extends DomainObjectWithStatus {

    private String firstName;
    private String middleName;
    private String lastName;

    // address information
    private String townType;
    private String townName;
    private String streetTypeName;
    private String streetName;
    private String buildingNumber;
    private String buildingBulk;
    private String apartmentNumber;

    private Set<Consumer> consumers = Collections.emptySet();

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

    public String getFIO() {
        StringBuilder sb = new StringBuilder();
        sb.append(lastName);

        if (isNotEmpty(firstName)) {
            sb.append(" ");
            sb.append(firstName.charAt(0));
            sb.append(".");

            if (isNotEmpty(middleName)) {
                sb.append(" ");
                sb.append(middleName.charAt(0));
                sb.append(".");
            }
        }

        return sb.toString();
    }

    public String getAddress() {
        StringBuilder sb = new StringBuilder();

        sb.append(streetTypeName);
        sb.append(". ");
        sb.append(streetName);
        sb.append(" ");
        sb.append(buildingNumber);

        if (isNotEmpty(buildingBulk)) {
            sb.append("/");
            sb.append(buildingBulk);
        }

        sb.append(", ");
        sb.append(apartmentNumber);

        return sb.toString();
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("firstName", firstName).
				append("middleName", middleName).
				append("lastName", lastName).
                append("townType", townType).
				append("townName", townName).
				append("streetTypeName", streetTypeName).
				append("streetName", streetName).
				append("buildingNumber", buildingNumber).
				append("buildingBulk", buildingBulk).
				append("apartmentNumber", apartmentNumber).
				toString();
	}

}
