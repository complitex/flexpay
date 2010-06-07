package org.flexpay.payments.actions.request.util;

import org.apache.commons.codec.binary.Hex;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.quittance.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import static org.flexpay.payments.actions.request.data.DebtsRequest.*;

public class ResponseUtil {

    private final static Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    public static String buildResponse(String requestId, int requestType, String statusCode, String statusMessage) throws FlexPayException {
        return requestType == PAY_DEBT_REQUEST ? buildPayResponse(requestId, statusCode, statusMessage) : buildSearchResponse(requestId, statusCode, requestType, statusMessage);
    }

    public static String buildPayResponse(String requestId, String statusCode, String statusMessage) throws FlexPayException {
        return buildPayResponse(null, requestId, statusCode, statusMessage);
    }

    public static String buildPayResponse(PayInfoResponse payInfoResponse, String requestId, String statusCode, String statusMessage) throws FlexPayException {

        log.debug("Building pay response: requestId = {}, statusCode = {}, statusMessage = {}", new Object[] {requestId, statusCode, statusMessage});

        Signature signature = initSignature();

        StringBuilder response = new StringBuilder();
        response.append("<response>").
                append("<payInfo>");

        addNonEmptyField(response, "requestId", requestId, signature);

        if (payInfoResponse != null) {

            addNonEmptyField(response, "operationId", payInfoResponse.getOperationId(), signature);
            addNonEmptyField(response, "statusCode", payInfoResponse.getStatusCode(), signature);
            addNonEmptyField(response, "statusMessage", getPayStatusMessage(payInfoResponse.getStatusCode()), signature);

            if (payInfoResponse.getServicePayInfos() != null) {
                for (ServicePayInfo servicePayInfo : payInfoResponse.getServicePayInfos()) {
                    addServicePayInfo(response, servicePayInfo, signature);
                }
            }
        } else {
            addNonEmptyField(response, "statusCode", statusCode, signature);
            addNonEmptyField(response, "statusMessage", statusMessage, signature);
        }

        response.append("</payInfo>");
        addSignature(response, signature);
        response.append("</response>");

        log.debug("Pay response = {}", response.toString());

        return response.toString();
    }

    public static String buildSearchResponse(String requestId, String statusCode, Integer requestType, String statusMessage) throws FlexPayException {
        return buildSearchResponse(null, requestId, requestType, statusCode, statusMessage);
    }

	public static String buildSearchResponse(QuittanceDetailsResponse quittanceDetailsResponse, String requestId, Integer requestType, String statusCode, String statusMessage) throws FlexPayException {

        log.debug("Building search response: requestId = {}, requestType = {}, statusCode = {}, statusMessage = {}", new Object[] {requestId, requestType, statusCode, statusMessage});

		Signature signature = initSignature();

		StringBuilder response = new StringBuilder();
		response.append("<response>");
        if (requestType == SEARCH_QUITTANCE_DEBT_REQUEST) {
            response.append("<quittanceDebtInfo>");
        } else if (requestType == SEARCH_DEBT_REQUEST) {
            response.append("<debtInfo>");
        }

		addNonEmptyField(response, "requestId", requestId, signature);

		if (quittanceDetailsResponse != null) {

			addNonEmptyField(response, "statusCode", quittanceDetailsResponse.getStatusCode(), signature);
			addNonEmptyField(response, "statusMessage", getSearchStatusMessage(quittanceDetailsResponse.getStatusCode()), signature);

            if (quittanceDetailsResponse.getInfos() != null) {
                for (QuittanceInfo quittanceInfo : quittanceDetailsResponse.getInfos()) {
                    addQuittanceInfo(response, quittanceInfo, requestType, signature);
                }
            }
		} else {
			addNonEmptyField(response, "statusCode", statusCode, signature);
			addNonEmptyField(response, "statusMessage", statusMessage, signature);
		}

        if (requestType == SEARCH_QUITTANCE_DEBT_REQUEST) {
            response.append("</quittanceDebtInfo>");
        } else if (requestType == SEARCH_DEBT_REQUEST) {
            response.append("</debtInfo>");
        }
		addSignature(response, signature);
		response.append("</response>");

        log.debug("Search debt response = {}", response.toString());

		return response.toString();
	}

    private static void addServicePayInfo(StringBuilder response, ServicePayInfo servicePayInfo, Signature signature) throws FlexPayException {
        response.append("<servicePayInfo>");
        addNonEmptyField(response, "serviceId", servicePayInfo.getServiceId(), signature);
        addNonEmptyField(response, "documentId", servicePayInfo.getDocumentId(), signature);
        addNonEmptyField(response, "serviceStatusCode", servicePayInfo.getServiceStatusCode(), signature);
        addNonEmptyField(response, "serviceStatusMessage", getServicePayStatusMessage(servicePayInfo.getServiceStatusCode()), signature);
        response.append("</servicePayInfo>");
    }

    private static void addQuittanceInfo(StringBuilder response, QuittanceInfo quittanceInfo, int requestType, Signature signature) throws FlexPayException {

        if (requestType == SEARCH_QUITTANCE_DEBT_REQUEST) {
            response.append("<quittanceInfo>");
            addNonEmptyField(response, "quittanceNumber", quittanceInfo.getQuittanceNumber(), signature);
            addNonEmptyField(response, "accountNumber", quittanceInfo.getAccountNumber(), signature);
            addNonEmptyField(response, "creationDate", quittanceInfo.getCreationDate(), signature);
            addNonEmptyField(response, "personFirstName", quittanceInfo.getPersonFirstName(), signature);
            addNonEmptyField(response, "personMiddleName", quittanceInfo.getPersonMiddleName(), signature);
            addNonEmptyField(response, "personLastName", quittanceInfo.getPersonLastName(), signature);
            addNonEmptyField(response, "country", quittanceInfo.getCountry(), signature);
            addNonEmptyField(response, "region", quittanceInfo.getRegion(), signature);
            addNonEmptyField(response, "townName", quittanceInfo.getTownName(), signature);
            //TODO: Сейчас в информаци о плательщике нет поля townType, поэтому оно всегда пустое
            addNonEmptyField(response, "townType", quittanceInfo.getTownType(), signature);
            addNonEmptyField(response, "streetName", quittanceInfo.getStreetName(), signature);
            addNonEmptyField(response, "streetType", quittanceInfo.getStreetType(), signature);
            addNonEmptyField(response, "buildingNumber", quittanceInfo.getBuildingNumber(), signature);
            addNonEmptyField(response, "buildingBulk", quittanceInfo.getBuildingBulk(), signature);
            addNonEmptyField(response, "apartmentNumber", quittanceInfo.getApartmentNumber(), signature);
            addNonEmptyField(response, "totalToPay", quittanceInfo.getTotalToPay(), signature);
            addNonEmptyField(response, "totalPayed", quittanceInfo.getTotalPayed(), signature);
        }

        for (ServiceDetails serviceDetails : quittanceInfo.getDetailses()) {
            response.append("<serviceDetails>");

            addNonEmptyField(response, "serviceId", serviceDetails.getServiceId(), signature);
            addNonEmptyField(response, "serviceName", serviceDetails.getServiceName(), signature);
            addNonEmptyField(response, "incomingBalance", serviceDetails.getIncomingBalance(), signature);
            addNonEmptyField(response, "outgoingBalance", serviceDetails.getOutgoingBalance(), signature);
            addNonEmptyField(response, "amount", serviceDetails.getAmount(), signature);
            if (requestType == SEARCH_QUITTANCE_DEBT_REQUEST) {
                addNonEmptyField(response, "expence", serviceDetails.getExpence(), signature);
                addNonEmptyField(response, "rate", serviceDetails.getRate(), signature);
                addNonEmptyField(response, "recalculation", serviceDetails.getRecalculation(), signature);
                addNonEmptyField(response, "benifit", serviceDetails.getBenifit(), signature);
                addNonEmptyField(response, "subsidy", serviceDetails.getSubsidy(), signature);
                addNonEmptyField(response, "payment", serviceDetails.getPayment(), signature);
                addNonEmptyField(response, "payed", serviceDetails.getPayed(), signature);
            }
            addNonEmptyField(response, "serviceProviderAccount", serviceDetails.getServiceProviderAccount(), signature);
            addNonEmptyField(response, "personFirstName", serviceDetails.getFirstName(), signature);
            addNonEmptyField(response, "personMiddleName", serviceDetails.getMiddleName(), signature);
            addNonEmptyField(response, "personLastName", serviceDetails.getLastName(), signature);
            if (requestType == SEARCH_QUITTANCE_DEBT_REQUEST) {
                addNonEmptyField(response, "country", serviceDetails.getCountry(), signature);
                addNonEmptyField(response, "region", serviceDetails.getRegion(), signature);
            }
            //TODO: Сейчас в информаци о плательщике нет поля townType, поэтому оно всегда пустое
            addNonEmptyField(response, "townName", serviceDetails.getTownName(), signature);
            addNonEmptyField(response, "townType", serviceDetails.getTownType(), signature);
            addNonEmptyField(response, "streetName", serviceDetails.getStreetName(), signature);
            addNonEmptyField(response, "streetType", serviceDetails.getStreetType(), signature);
            addNonEmptyField(response, "buildingNumber", serviceDetails.getBuildingNumber(), signature);
            addNonEmptyField(response, "buildingBulk", serviceDetails.getBuildingBulk(), signature);
            addNonEmptyField(response, "apartmentNumber", serviceDetails.getApartmentNumber(), signature);
            addNonEmptyField(response, "roomNumber", serviceDetails.getRoomNumber(), signature);

            if (requestType == SEARCH_QUITTANCE_DEBT_REQUEST) {
                if (serviceDetails.getAttributes() != null) {
                    for (ServiceDetails.ServiceAttribute serviceAttribute : serviceDetails.getAttributes()) {
                        response.append("<serviceAttribute>");
                        addNonEmptyField(response, "name", serviceAttribute.getName(), signature);
                        addNonEmptyField(response, "value", serviceAttribute.getValue(), signature);
                        response.append("</serviceAttribute>");
                    }
                }
            }

            response.append("</serviceDetails>");
        }
        if (requestType == SEARCH_QUITTANCE_DEBT_REQUEST) {
            response.append("</quittanceInfo>");
        }
    }

    private static void addNonEmptyField(StringBuilder response, String tagName, Object field, Signature signature) throws FlexPayException {

        if (field == null) {
            return;
        }

        String fieldValue = field.toString();
        response.append("<").append(tagName).append(">").
                append(fieldValue).
                append("</").append(tagName).append(">");

        try {
            signature.update(fieldValue.getBytes("utf8"));
        } catch (SignatureException e) {
            throw new FlexPayException("Error updating digital signature with value {}", fieldValue);
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding for value {}", fieldValue);
            throw new FlexPayException("Unsupported encoding for value {}", fieldValue);
        }
    }

    private static void addSignature(StringBuilder response, Signature signature) throws FlexPayException {

        try {
            response.append("<signature>").append(Hex.encodeHex(signature.sign())).append("</signature>");
        } catch (SignatureException e) {
            log.error("Error signing response", e);
            throw new FlexPayException("Error signing response", e);
        }
    }

    private static Signature initSignature() throws FlexPayException {

        PrivateKey key = initKey();

        try {

            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(key);

            return signature;

        } catch (Exception e) {
            log.error("Error initializing signature", e);
            throw new FlexPayException("Error initializing signature");
        }
    }

    private static PrivateKey initKey() throws FlexPayException {
        try {
            KeyStore keyStore = KeyStoreUtil.loadKeyStore();
            if (!keyStore.isKeyEntry(ApplicationConfig.getSelfKeyAlias())) {
                log.error("Unable to load security key for signing response");
                throw new FlexPayException("Unable to load security key for signing response");
            }

            PrivateKey key = (PrivateKey) keyStore.getKey(ApplicationConfig.getSelfKeyAlias(), ApplicationConfig.getSelfKeyPassword().toCharArray());
            if (key == null) {
                log.error("Unable to load security key for signing response (password is bad)");
                throw new FlexPayException("Unable to load security key for signing response (password is bad)");
            }

            return key;

        } catch (Exception e) {
            log.error("Error loading certificate from keystore", e);
            throw new FlexPayException("Error loading certificate from keystore", e);
        }
    }

    private static String getSearchStatusMessage(int status) {
        switch (status) {
            case DetailsResponse.STATUS_SUCCESS:
                return "OK";
            case DetailsResponse.STATUS_ACCOUNT_NOT_FOUND:
                return "Account not found";
            case DetailsResponse.STATUS_APARTMENT_NOT_FOUND:
                return "Apartment not found";
            case DetailsResponse.STATUS_INTERNAL_ERROR:
                return "Internal error";
            case DetailsResponse.STATUS_INVALID_QUITTANCE_NUMBER:
                return "Invalid quittance number";
            case DetailsResponse.STATUS_QUITTANCE_NOT_FOUND:
                return "Quittance not found";
            case DetailsResponse.STATUS_RECIEVE_TIMEOUT:
                return "Receive timeout";
            case DetailsResponse.STATUS_UNKNOWN_REQUEST:
                return "Unknown request";
            default:
                return null;
        }
    }

    private static String getPayStatusMessage(int status) {
        switch (status) {
            case PayInfoResponse.STATUS_SUCCESS:
                return "OK";
            case PayInfoResponse.STATUS_INCORRECT_AUTHENTICATION_DATA:
                return "Incorrect authentication data";
            case PayInfoResponse.STATUS_UNKNOWN_REQUEST:
                return "Unknown request";
            case PayInfoResponse.STATUS_INTERNAL_ERROR:
                return "Internal error";
            case PayInfoResponse.STATUS_RECIEVE_TIMEOUT:
                return "Receive timeout";
            case PayInfoResponse.STATUS_REQUEST_IS_NOT_PROCESSED:
                return "Request is not processed, details of errors found in the structure \"servicePayInfo\"";
            default:
                return null;
        }
    }

    private static String getServicePayStatusMessage(int status) {
        switch (status) {
            case ServicePayInfo.STATUS_SUCCESS:
                return "OK";
            case ServicePayInfo.STATUS_ACCOUNT_NOT_FOUND:
                return "Account not found";
            case ServicePayInfo.STATUS_SERVICE_NOT_FOUND:
                return "Service not found";
            case ServicePayInfo.STATUS_INCORRECT_PAY_SUM:
                return "Incorrect pay sum";
            default:
                return null;
        }
    }

}
