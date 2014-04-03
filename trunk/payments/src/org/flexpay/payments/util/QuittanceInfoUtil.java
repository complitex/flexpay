package org.flexpay.payments.util;

import org.codehaus.jackson.JsonNode;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.action.outerrequest.request.response.data.QuittanceInfo;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel Sknar
 */
public abstract class QuittanceInfoUtil {
    private static Logger logger = LoggerFactory.getLogger(QuittanceInfoUtil.class);

    public static final DateTimeFormatter CREATIOM_DATE_FORMAT = DateTimeFormat.forPattern("ddMMyyyyHHmmss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("ddMMyyyy");

    public static List<QuittanceInfo> getQuttanceInfos(Iterator<JsonNode> quittanceInfo) {
        List<QuittanceInfo> quittanceInfos = CollectionUtils.list();
        while (quittanceInfo.hasNext()) {
            logger.info("has next");
            JsonNode node = quittanceInfo.next();
            QuittanceInfo info = new QuittanceInfo();

            logger.info("node: {}", node.getTextValue());
            Iterator<Map.Entry<String, JsonNode>> fields = node.getFields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                logger.info("{}: {}", field.getKey(), field.getValue().getTextValue());
            }

            //service provider account
            final JsonNode quittanceNumber = node.get("quittanceNumber");
            if (quittanceNumber != null) {
                info.setQuittanceNumber(quittanceNumber.getTextValue());
            }
            final JsonNode eircAccount = node.get("eircAccount");
            if (eircAccount != null) {
                info.setAccountNumber(eircAccount.getTextValue());
            }
            final JsonNode orderNumber = node.get("orderNumber");
            if (orderNumber != null) {
                info.setOrderNumber(orderNumber.getIntValue());
            }
            final JsonNode creationDate = node.get("creationDate");
            if (creationDate != null) {
                info.setCreationDate(CREATIOM_DATE_FORMAT.parseDateTime(creationDate.getTextValue()).toDate());
            }
            final JsonNode beginDate = node.get("beginDate");
            if (beginDate != null) {
                info.setDateFrom(DATE_FORMAT.parseDateTime(beginDate.getTextValue()).toDate());
            }
            final JsonNode endDate = node.get("endDate");
            if (endDate != null) {
                info.setDateTill(DateUtil.getEndOfThisDay(DATE_FORMAT.parseDateTime(endDate.getTextValue()).toDate()));
            }

            //financial attributes
            final JsonNode totalPayed = node.get("totalPayed");
            if (totalPayed != null) {
                info.setTotalPayed(totalPayed.getDecimalValue());
            }
            final JsonNode totalToPay = node.get("totalToPay");
            if (totalToPay != null) {
                info.setTotalToPay(totalToPay.getDecimalValue());
            }

            //address
            final JsonNode country = node.get("country");
            if (country != null) {
                info.setCountry(country.getTextValue());
            }
            final JsonNode region = node.get("region");
            if (region != null) {
                info.setRegion(region.getTextValue());
            }
            final JsonNode town = node.get("town");
            if (town != null) {
                info.setTownName(town.getTextValue());
            }
            final JsonNode townType = node.get("townType");
            if (townType != null) {
                info.setTownType(townType.getTextValue());
            }
            final JsonNode street = node.get("street");
            if (street != null) {
                info.setStreetName(street.getTextValue());
            }
            final JsonNode streetType = node.get("streetType");
            if (streetType != null) {
                info.setStreetType(streetType.getTextValue());
            }
            final JsonNode buildingNumber = node.get("buildingNumber");
            if (buildingNumber != null) {
                info.setBuildingNumber(buildingNumber.getTextValue());
            }
            final JsonNode buildingBulk = node.get("buildingBulk");
            if (buildingBulk != null) {
                info.setBuildingBulk(buildingBulk.getTextValue());
            }
            final JsonNode apartment = node.get("apartment");
            if (apartment != null) {
                info.setApartmentNumber(apartment.getTextValue());
            }
            final JsonNode room = node.get("room");
            if (room != null) {
                info.setRoomNumber(room.getTextValue());
            }

            //person
            final JsonNode firstName = node.get("firstName");
            if (firstName != null) {
                info.setPersonFirstName(firstName.getTextValue());
            }
            final JsonNode middleName = node.get("middleName");
            if (middleName != null) {
                info.setPersonMiddleName(middleName.getTextValue());
            }
            final JsonNode lastName = node.get("lastName");
            if (lastName != null) {
                info.setPersonLastName(lastName.getTextValue());
            }

            //service`s details
            JsonNode debInfo = node.findValue("serviceDetails");
            if (debInfo != null && debInfo.size() > 0) {
                info.setServiceDetailses(ServiceDetailsUtil.getServiceDetailes(debInfo.getElements()));
            }

            quittanceInfos.add(info);
        }
        return quittanceInfos;
    }
}
