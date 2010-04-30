package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class QuittanceInfo implements Serializable {

    private String quittanceNumber;
    private String accountNumber;
    private Date creationDate;
    private String serviceOrganizationMasterIndex;
    private Integer orderNumber;
    private Date dateFrom;
    private Date dateTill;
    private String personFirstName;
    private String personMiddleName;
    private String personLastName;
    private String personMasterIndex;
    private String apartmentMasterIndex;
    private String country;
    private String region;
    private String townName;
    private String townType;
    private String streetName;
    private String streetType;
    private String buildingNumber;
    private String buildingBulk;
    private String apartmentNumber;

    private BigDecimal totalPayed;
    private BigDecimal totalToPay;
    private ServiceDetails[] detailses;

    public String getPersonFio() {
        StringBuilder fioBuilder = new StringBuilder();

        if (StringUtils.isNotEmpty(personLastName)) {
            fioBuilder.append(personLastName);
        }

        if (StringUtils.isNotEmpty(personFirstName)) {
            fioBuilder.append(" ").append(personFirstName.charAt(0)).append(".");
        }

        if (StringUtils.isNotEmpty(personMiddleName)) {
            fioBuilder.append(" ").append(personMiddleName.charAt(0)).append(".");
        }

        return fioBuilder.toString();
    }

    public String getAddress() {

        StringBuilder addressBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(streetType)) {
            addressBuilder.append(streetType).append(". ");
        }

        if (StringUtils.isNotEmpty(streetName)) {
            addressBuilder.append(streetName).append(", ");
        }

        if (StringUtils.isNotEmpty(buildingNumber)) {
            addressBuilder.append(buildingNumber);
        }

        if (StringUtils.isNotEmpty(buildingBulk)) {
            addressBuilder.append("/").append(buildingBulk);
        }

        if (StringUtils.isNotEmpty(apartmentNumber)) {
            addressBuilder.append(" ").append(apartmentNumber);
        }

        return addressBuilder.toString();
    }

    public String getQuittanceNumber() {
        return quittanceNumber;
    }

    public void setQuittanceNumber(String quittanceNumber) {
        this.quittanceNumber = quittanceNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getServiceOrganizationMasterIndex() {
        return serviceOrganizationMasterIndex;
    }

    public void setServiceOrganizationMasterIndex(String serviceOrganizationMasterIndex) {
        this.serviceOrganizationMasterIndex = serviceOrganizationMasterIndex;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTill() {
        return dateTill;
    }

    public void setDateTill(Date dateTill) {
        this.dateTill = dateTill;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonMiddleName() {
        return personMiddleName;
    }

    public void setPersonMiddleName(String personMiddleName) {
        this.personMiddleName = personMiddleName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getPersonMasterIndex() {
        return personMasterIndex;
    }

    public void setPersonMasterIndex(String personMasterIndex) {
        this.personMasterIndex = personMasterIndex;
    }

    public String getApartmentMasterIndex() {
        return apartmentMasterIndex;
    }

    public void setApartmentMasterIndex(String apartmentMasterIndex) {
        this.apartmentMasterIndex = apartmentMasterIndex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType) {
        this.streetType = streetType;
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

    public ServiceDetails[] getDetailses() {
        return detailses;
    }

    public void setDetailses(ServiceDetails[] detailses) {
        this.detailses = detailses;
    }

    public BigDecimal getTotalPayed() {
        return totalPayed;
    }

    public void setTotalPayed(BigDecimal totalPayed) {
        this.totalPayed = totalPayed;
    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("quittanceNumber", quittanceNumber).
                append("accountNumber", accountNumber).
                append("creationDate", creationDate).
                append("serviceOrganizationMasterIndex", serviceOrganizationMasterIndex).
                append("orderNumber", orderNumber).
                append("dateFrom", dateFrom).
                append("dateTill", dateTill).
                append("personFirstName", personFirstName).
                append("personMiddleName", personMiddleName).
                append("personLastName", personLastName).
                append("personMasterIndex", personMasterIndex).
                append("apartmentMasterIndex", apartmentMasterIndex).
                append("country", country).
                append("region", region).
                append("townName", townName).
                append("townType", townType).
                append("streetName", streetName).
                append("streetType", streetType).
                append("buildingNumber", buildingNumber).
                append("buildingBulk", buildingBulk).
                append("apartmentNumber", apartmentNumber).
                append("totalPayed", totalPayed).
                append("totalToPay", totalToPay).
                append("detailses", detailses).
                toString();
    }
}
