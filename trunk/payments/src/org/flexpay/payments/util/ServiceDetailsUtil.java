package org.flexpay.payments.util;

import org.codehaus.jackson.JsonNode;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;
import org.flexpay.payments.action.outerrequest.request.response.data.ServiceDetails;

import java.util.Iterator;
import java.util.List;

/**
 * @author Pavel Sknar
 */
public abstract class ServiceDetailsUtil {
    public static List<ServiceDetails> getServiceDetailes(Iterator<JsonNode> debInfo) {
        List<ServiceDetails> listServiceDetailses = CollectionUtils.list();
        while (debInfo.hasNext()) {
            JsonNode node = debInfo.next();
            ServiceDetails serviceDetails = new ServiceDetails();

            //service provider account
            final JsonNode serviceCode = node.get("serviceCode");
            if (serviceCode != null) {
                serviceDetails.setServiceCode(serviceCode.getTextValue());
            }
            final JsonNode serviceProviderAccount = node.get("serviceProviderAccount");
            if (serviceProviderAccount != null) {
                serviceDetails.setServiceProviderAccount(serviceProviderAccount.getTextValue());
            }
            final JsonNode eircAccount = node.get("eircAccount");
            if (eircAccount != null) {
                serviceDetails.addServiceAttribute(ConsumerAttributes.ATTR_ERC_ACCOUNT, eircAccount.getTextValue());
            }

            //financial attributes
            final JsonNode incomingBalance = node.get("incomingBalance");
            if (incomingBalance != null) {
                serviceDetails.setIncomingBalance(incomingBalance.getDecimalValue());
            }
            final JsonNode outgoingBalance = node.get("outgoingBalance");
            if (outgoingBalance != null) {
                serviceDetails.setOutgoingBalance(outgoingBalance.getDecimalValue());
            }
            final JsonNode amount = node.get("amount");
            if (amount != null) {
                serviceDetails.setAmount(amount.getDecimalValue());
            }
            final JsonNode expence = node.get("expence");
            if (expence != null) {
                serviceDetails.setExpence(expence.getDecimalValue());
            }
            final JsonNode rate = node.get("rate");
            if (rate != null) {
                serviceDetails.setRate(rate.getDecimalValue());
            }
            final JsonNode recalculation = node.get("recalculation");
            if (recalculation != null) {
                serviceDetails.setRecalculation(recalculation.getDecimalValue());
            }
            final JsonNode benifit = node.get("benifit");
            if (benifit != null) {
                serviceDetails.setBenifit(benifit.getDecimalValue());
            }
            final JsonNode subsidy = node.get("subsidy");
            if (subsidy != null) {
                serviceDetails.setSubsidy(subsidy.getDecimalValue());
            }
            final JsonNode payed = node.get("payed");
            if (payed != null) {
                serviceDetails.setPayment(payed.getDecimalValue());
            }

            //address
            final JsonNode country = node.get("country");
            if (country != null) {
                serviceDetails.setCountry(country.getTextValue());
            }
            final JsonNode region = node.get("region");
            if (region != null) {
                serviceDetails.setRegion(region.getTextValue());
            }
            final JsonNode town = node.get("town");
            if (town != null) {
                serviceDetails.setTownName(town.getTextValue());
            }
            final JsonNode townType = node.get("townType");
            if (townType != null) {
                serviceDetails.setTownType(townType.getTextValue());
            }
            final JsonNode street = node.get("street");
            if (street != null) {
                serviceDetails.setStreetName(street.getTextValue());
            }
            final JsonNode streetType = node.get("streetType");
            if (streetType != null) {
                serviceDetails.setStreetType(streetType.getTextValue());
            }
            final JsonNode buildingNumber = node.get("buildingNumber");
            if (buildingNumber != null) {
                serviceDetails.setBuildingNumber(buildingNumber.getTextValue());
            }
            final JsonNode buildingBulk = node.get("buildingBulk");
            if (buildingBulk != null) {
                serviceDetails.setBuildingBulk(buildingBulk.getTextValue());
            }
            final JsonNode apartment = node.get("apartment");
            if (apartment != null) {
                serviceDetails.setApartmentNumber(apartment.getTextValue());
            }
            final JsonNode room = node.get("room");
            if (room != null) {
                serviceDetails.setRoomNumber(room.getTextValue());
            }

            //person
            final JsonNode firstName = node.get("firstName");
            if (firstName != null) {
                serviceDetails.setPersonFirstName(firstName.getTextValue());
            }
            final JsonNode middleName = node.get("middleName");
            if (middleName != null) {
                serviceDetails.setPersonMiddleName(middleName.getTextValue());
            }
            final JsonNode lastName = node.get("lastName");
            if (lastName != null) {
                serviceDetails.setPersonLastName(lastName.getTextValue());
            }

            listServiceDetailses.add(serviceDetails);
        }
        return listServiceDetailses;
    }
}
