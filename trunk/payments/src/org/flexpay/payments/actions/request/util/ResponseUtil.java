package org.flexpay.payments.actions.request.util;

import org.apache.commons.codec.binary.Hex;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.actions.request.data.request.RequestType;
import org.flexpay.payments.actions.request.data.response.PayInfoResponse;
import org.flexpay.payments.actions.request.data.response.QuittanceDetailsResponse;
import org.flexpay.payments.actions.request.data.response.SimpleResponse;
import org.flexpay.payments.actions.request.data.response.Status;
import org.flexpay.payments.actions.request.data.response.data.QuittanceInfo;
import org.flexpay.payments.actions.request.data.response.data.ServiceDetails;
import org.flexpay.payments.actions.request.data.response.data.ServicePayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResponseUtil {

    private final static Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    public static String buildResponse(String requestId, RequestType requestType, Status status, Locale locale) throws FlexPayException {
        if (requestType == RequestType.PAY_DEBT_REQUEST) {
            return buildPayResponse(null, requestId, status, locale);
        } else if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST || requestType == RequestType.SEARCH_DEBT_REQUEST) {
            return buildSearchResponse(null, requestId, status, requestType, locale);
        } else if (requestType == RequestType.REVERSAL_PAY_REQUEST) {
            return buildReversalPayResponse(null, requestId, status, locale);
        } else {
            log.warn("Incorrect request type parameter on buildResponse method");
            throw new FlexPayException("Incorrect request type parameter on buildResponse method");
        }
    }

    public static String buildReversalPayResponse(SimpleResponse reversalInfoResponse, String requestId, Status status, Locale locale) throws FlexPayException {

        log.debug("Building reversal info response: requestId = {}, status = {}", requestId, status);

        Signature signature = initSignature();

        StringBuilder response = new StringBuilder();
        response.append("<response>").
                append("<reversalInfo>");

        addNonEmptyField(response, "requestId", requestId, signature);

        if (reversalInfoResponse != null) {
            addNonEmptyField(response, "statusCode", reversalInfoResponse.getStatus().getCode(), signature);
            addNonEmptyField(response, "statusMessage", getStatusText(reversalInfoResponse.getStatus(), locale), signature);
        } else {
            addNonEmptyField(response, "statusCode", status.getCode(), signature);
            addNonEmptyField(response, "statusMessage", getStatusText(status, locale), signature);
        }

        response.append("</reversalInfo>");
        addSignature(response, signature);
        response.append("</response>");

        log.debug("Reversal info response = {}", response.toString());

        return response.toString();
    }

    public static String buildPayResponse(PayInfoResponse payInfoResponse, String requestId, Status status, Locale locale) throws FlexPayException {

        log.debug("Building pay response: requestId = {}, status = {}", requestId, status);

        Signature signature = initSignature();

        StringBuilder response = new StringBuilder();
        response.append("<response>").
                append("<payInfo>");

        addNonEmptyField(response, "requestId", requestId, signature);

        if (payInfoResponse != null) {

            addNonEmptyField(response, "operationId", payInfoResponse.getOperationId(), signature);
            addNonEmptyField(response, "statusCode", payInfoResponse.getStatus().getCode(), signature);
            addNonEmptyField(response, "statusMessage", getStatusText(payInfoResponse.getStatus(), locale), signature);

            if (payInfoResponse.getServicePayInfos() != null) {
                for (ServicePayInfo servicePayInfo : payInfoResponse.getServicePayInfos()) {
                    addServicePayInfo(response, servicePayInfo, signature, locale);
                }
            }
        } else {
            addNonEmptyField(response, "statusCode", status.getCode(), signature);
            addNonEmptyField(response, "statusMessage", getStatusText(status, locale), signature);
        }

        response.append("</payInfo>");
        addSignature(response, signature);
        response.append("</response>");

        log.debug("Pay response = {}", response.toString());

        return response.toString();
    }

	public static String buildSearchResponse(QuittanceDetailsResponse quittanceDetailsResponse, String requestId, Status status, RequestType requestType, Locale locale) throws FlexPayException {

        log.debug("Building search response: requestId = {}, requestType = {}, status = {}", new Object[] {requestId, requestType, status});

		Signature signature = initSignature();

		StringBuilder response = new StringBuilder();
		response.append("<response>");
        if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
            response.append("<quittanceDebtInfo>");
        } else if (requestType == RequestType.SEARCH_DEBT_REQUEST) {
            response.append("<debtInfo>");
        }

		addNonEmptyField(response, "requestId", requestId, signature);

		if (quittanceDetailsResponse != null) {

			addNonEmptyField(response, "statusCode", quittanceDetailsResponse.getStatus().getCode(), signature);
			addNonEmptyField(response, "statusMessage", getStatusText(quittanceDetailsResponse.getStatus(), locale), signature);

            if (quittanceDetailsResponse.getInfos() != null) {
                for (QuittanceInfo quittanceInfo : quittanceDetailsResponse.getInfos()) {
                    addQuittanceInfo(response, quittanceInfo, requestType, signature);
                }
            }
		} else {
			addNonEmptyField(response, "statusCode", status.getCode(), signature);
			addNonEmptyField(response, "statusMessage", getStatusText(status, locale), signature);
		}

        if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
            response.append("</quittanceDebtInfo>");
        } else if (requestType == RequestType.SEARCH_DEBT_REQUEST) {
            response.append("</debtInfo>");
        }
		addSignature(response, signature);
		response.append("</response>");

        log.debug("Search debt response = {}", response.toString());

		return response.toString();
	}

    private static void addServicePayInfo(StringBuilder response, ServicePayInfo servicePayInfo, Signature signature, Locale locale) throws FlexPayException {
        response.append("<servicePayInfo>");
        addNonEmptyField(response, "serviceId", servicePayInfo.getServiceId(), signature);
        addNonEmptyField(response, "documentId", servicePayInfo.getDocumentId(), signature);
        addNonEmptyField(response, "serviceStatusCode", servicePayInfo.getServiceStatus().getCode(), signature);
        addNonEmptyField(response, "serviceStatusMessage", getStatusText(servicePayInfo.getServiceStatus(), locale), signature);
        response.append("</servicePayInfo>");
    }

    private static void addQuittanceInfo(StringBuilder response, QuittanceInfo quittanceInfo, RequestType requestType, Signature signature) throws FlexPayException {

        if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
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
            if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
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
            if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
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

            if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
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
        if (requestType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
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

    private static String getStatusText(Status status, Locale locale) {
        return ResourceBundle.getBundle("org/flexpay/payments/i18n/responseStatuses", locale).getString(status.getTextKey());
    }

}
