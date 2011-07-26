package org.flexpay.httptester.outerrequest.request.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.flexpay.httptester.outerrequest.request.response.data.ServiceDetails;

import java.util.ArrayList;
import java.util.List;

public class GetQuittanceDebtInfoResponse extends Response {

    private List<QuittanceInfo> quittanceInfos = new ArrayList<QuittanceInfo>();

    public List<QuittanceInfo> getQuittanceInfos() {
        return quittanceInfos;
    }

    public void setQuittanceInfos(List<QuittanceInfo> quittanceInfos) {
        this.quittanceInfos = quittanceInfos;
    }

    public void addQuittanceInfo(QuittanceInfo quittanceInfo) {

        if (quittanceInfos == null) {
            quittanceInfos = new ArrayList<QuittanceInfo>();
        }

        quittanceInfos.add(quittanceInfo);
    }

    public static class QuittanceInfo {

        private String quittanceNumber;
        private String accountNumber;
        private String creationDate;
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

        private String totalPayed;
        private String totalToPay;
        private List<ServiceDetails> serviceDetailses = new ArrayList<ServiceDetails>();

        public String getQuittanceNumber() {
            return quittanceNumber;
        }

        public void setQuittanceNumber(String quittanceNumber) {
            this.quittanceNumber = quittanceNumber;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
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

        public List<ServiceDetails> getServiceDetailses() {
            return serviceDetailses;
        }

        public void setServiceDetailses(List<ServiceDetails> serviceDetailses) {
            this.serviceDetailses = serviceDetailses;
        }

        public void addServiceDetails(ServiceDetails serviceDetails) {
            serviceDetailses.add(serviceDetails);
        }

        public String getTotalPayed() {
            return totalPayed;
        }

        public void setTotalPayed(String totalPayed) {
            this.totalPayed = totalPayed;
        }

        public String getTotalToPay() {
            return totalToPay;
        }

        public void setTotalToPay(String totalToPay) {
            this.totalToPay = totalToPay;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                    append("quittanceNumber", quittanceNumber).
                    append("accountNumber", accountNumber).
                    append("creationDate", creationDate).
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
                    append("totalPayed", totalPayed).
                    append("totalToPay", totalToPay).
                    append("serviceDetailses", serviceDetailses).
                    toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("quittanceInfos", quittanceInfos).
                toString();
    }
}
