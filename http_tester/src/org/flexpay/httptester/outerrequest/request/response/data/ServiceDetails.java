package org.flexpay.httptester.outerrequest.request.response.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetails {

    private String serviceId;
    private String serviceName;
    private String incomingBalance;
    private String outgoingBalance;
    private String amount;
    private String expence;
    private String rate;
    private String recalculation;
    private String benifit;
    private String subsidy;
    private String payment;
    private String payed;
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

    private List<ServiceAttribute> attributes = new ArrayList<ServiceAttribute>();

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIncomingBalance() {
        return incomingBalance;
    }

    public void setIncomingBalance(String incomingBalance) {
        this.incomingBalance = incomingBalance;
    }

    public String getOutgoingBalance() {
        return outgoingBalance;
    }

    public void setOutgoingBalance(String outgoingBalance) {
        this.outgoingBalance = outgoingBalance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpence() {
        return expence;
    }

    public void setExpence(String expence) {
        this.expence = expence;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRecalculation() {
        return recalculation;
    }

    public void setRecalculation(String recalculation) {
        this.recalculation = recalculation;
    }

    public String getBenifit() {
        return benifit;
    }

    public void setBenifit(String benifit) {
        this.benifit = benifit;
    }

    public String getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
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

    public void addAttribute(ServiceAttribute attribute) {
        attributes.add(attribute);
    }

    public static class ServiceAttribute {

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
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                    append("name", name).
                    append("value", value).
                    toString();
        }
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
}
