package org.flexpay.payments.action.outerrequest.request.response.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.util.CollectionUtils.list;

public class QuittanceInfo implements Serializable {

    private String quittanceNumber;
    private String accountNumber;
    private Date creationDate;
    private String personFirstName;
    private String personMiddleName;
    private String personLastName;
    private String country;
    private String region;
    private String townName;
    private String townType;
    private String streetName;
    private String streetType;
    private String buildingNumber;
    private String buildingBulk;
    private String apartmentNumber;
    private String roomNumber;
    private BigDecimal totalPayed;
    private BigDecimal totalToPay;

    private String serviceOrganizationMasterIndex;
    private Integer orderNumber;
    private Date dateFrom;
    private Date dateTill;
    private String personMasterIndex;
    private String apartmentMasterIndex;

    private List<ServiceDetails> serviceDetailses = list();

    public String getPersonFio() {
        StringBuilder fioBuilder = new StringBuilder();

        if (isNotEmpty(personLastName)) {
            fioBuilder.append(personLastName);
        }

        if (isNotEmpty(personFirstName)) {
            fioBuilder.append(" ").append(personFirstName.charAt(0)).append(".");
        }

        if (isNotEmpty(personMiddleName)) {
            fioBuilder.append(" ").append(personMiddleName.charAt(0)).append(".");
        }

        return fioBuilder.toString();
    }

    public String getAddress() {

        StringBuilder addressBuilder = new StringBuilder();
        if (isNotEmpty(streetType)) {
            addressBuilder.append(streetType).append(". ");
        }

        if (isNotEmpty(streetName)) {
            addressBuilder.append(streetName).append(", ");
        }

        if (isNotEmpty(buildingNumber)) {
            addressBuilder.append(buildingNumber);
        }

        if (isNotEmpty(buildingBulk)) {
            addressBuilder.append("/").append(buildingBulk);
        }

        if (isNotEmpty(apartmentNumber)) {
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<ServiceDetails> getServiceDetailses() {
        return serviceDetailses;
    }

    public void setServiceDetailses(List<ServiceDetails> serviceDetailses) {
        this.serviceDetailses = serviceDetailses;
    }

    public void addServiceDetails(ServiceDetails serviceDetails) {
        if (serviceDetailses == null) {
            serviceDetailses = list();
        }

        serviceDetailses.add(serviceDetails);
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
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
                append("serviceDetailses", serviceDetailses).
                toString();
    }
}
