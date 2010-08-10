package org.flexpay.payments.actions.outerrequest.request.response.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ServiceDetails implements Serializable {

    private Long serviceId;
    private String serviceName;
    private BigDecimal incomingBalance;
    private BigDecimal outgoingBalance;
    private BigDecimal amount;
    private BigDecimal expence;
    private BigDecimal rate;
    private BigDecimal recalculation;
    private BigDecimal benifit;
    private BigDecimal subsidy;
    private BigDecimal payment;
    private BigDecimal payed;
    private String serviceMasterIndex;
    private String serviceProviderAccount;

    // service provider internal data
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

    private List<ServiceAttribute> attributes = list();

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getIncomingBalance() {
        return incomingBalance;
    }

    public void setIncomingBalance(BigDecimal incomingBalance) {
        this.incomingBalance = incomingBalance;
    }

    public BigDecimal getOutgoingBalance() {
        return outgoingBalance;
    }

    public void setOutgoingBalance(BigDecimal outgoingBalance) {
        this.outgoingBalance = outgoingBalance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExpence() {
        return expence;
    }

    public void setExpence(BigDecimal expence) {
        this.expence = expence;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRecalculation() {
        return recalculation;
    }

    public void setRecalculation(BigDecimal recalculation) {
        this.recalculation = recalculation;
    }

    public BigDecimal getBenifit() {
        return benifit;
    }

    public void setBenifit(BigDecimal benifit) {
        this.benifit = benifit;
    }

    public BigDecimal getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(BigDecimal subsidy) {
        this.subsidy = subsidy;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getPayed() {
        return payed;
    }

    public void setPayed(BigDecimal payed) {
        this.payed = payed;
    }

    public String getServiceMasterIndex() {
        return serviceMasterIndex;
    }

    public void setServiceMasterIndex(String serviceMasterIndex) {
        this.serviceMasterIndex = serviceMasterIndex;
    }

    public String getServiceProviderAccount() {
        return serviceProviderAccount;
    }

    public void setServiceProviderAccount(String serviceProviderAccount) {
        this.serviceProviderAccount = serviceProviderAccount;
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

    public List<ServiceAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ServiceAttribute> attributes) {
        this.attributes = attributes;
    }

    public void addServiceAttribute(ServiceAttribute serviceAttribute) {
        if (attributes == null) {
            attributes = list();
        }
        attributes.add(serviceAttribute);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("serviceId", serviceId).
                append("serviceName", serviceName).
                append("incomingBalance", incomingBalance).
                append("outgoingBalance", outgoingBalance).
                append("amount", amount).
                append("expence", expence).
                append("rate", rate).
                append("recalculation", recalculation).
                append("benifit", benifit).
                append("subsidy", subsidy).
                append("payment", payment).
                append("payed", payed).
                append("serviceMasterIndex", serviceMasterIndex).
                append("serviceProviderAccount", serviceProviderAccount).
                append("personFirstName", personFirstName).
                append("personMiddleName", personMiddleName).
                append("personLastName", personLastName).
                append("country", country).
                append("region", region).
                append("townName", townName).
                append("townType", townType).
                append("streetName", streetName).
                append("streetType", streetType).
                append("buildingNumber", buildingNumber).
                append("buildingBulk", buildingBulk).
                append("apartmentNumber", apartmentNumber).
                append("roomNumber", roomNumber).
                append("attributes", attributes).
                toString();
    }

    public static class ServiceAttribute implements Serializable {

        private String name;
        private String value;

        public ServiceAttribute() {
        }

        public ServiceAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).
                    append("name", name).
                    append("value", value).
                    toString();
        }
    }

}
