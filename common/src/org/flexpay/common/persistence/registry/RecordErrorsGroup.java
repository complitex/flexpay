package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RecordErrorsGroup {

    private Integer errorType = -1;
    private String townName;
    private String streetType;
    private String streetName;
    private String buildingNumber;
    private String buildingBulk;
    private String apartmentNumber;
    private String lastName;
    private String middleName;
    private String firstName;
    private Long numberOfRecords;

    public Integer getErrorType() {
        return errorType;
    }

    public void setErrorType(Integer errorType) {
        this.errorType = errorType;
    }

    public Long getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(Long numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("errorType", errorType).
                append("townName", townName).
                append("streetType", streetType).
                append("streetName", streetName).
                append("buildingNumber", buildingNumber).
                append("buildingBulk", buildingBulk).
                append("apartmentNumber", apartmentNumber).
                append("lastName", lastName).
                append("middleName", middleName).
                append("firstName", firstName).
                append("numberOfRecords", numberOfRecords).
                toString();
    }
}
