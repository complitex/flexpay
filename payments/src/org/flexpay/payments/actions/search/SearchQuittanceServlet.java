package org.flexpay.payments.actions.search;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.actions.search.data.DebtInfo;
import org.flexpay.payments.actions.search.data.SearchDebtsRequest;
import org.flexpay.payments.persistence.quittance.InfoRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.persistence.quittance.QuittanceInfo;
import org.flexpay.payments.persistence.quittance.ServiceDetails;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;

import static org.flexpay.payments.persistence.quittance.InfoRequest.*;
import static org.flexpay.payments.persistence.quittance.DetailsResponse.*;
import static org.flexpay.payments.actions.search.data.SearchDebtsRequest.*;

public class SearchQuittanceServlet extends HttpServlet {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private KeyStore keyStore;

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

		Writer writer = httpServletResponse.getWriter();

        String requestId = null;
		try {
			SearchDebtsRequest requestDebts = parseRequest(httpServletRequest);
            processRequest(requestDebts, writer);
		} catch (Exception ex) {
            try {
                writer.write(buildResponse(requestId, "9", "Unknown request"));
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
		}

	}

    private SearchDebtsRequest parseRequest(HttpServletRequest httpServletRequest) throws FlexPayException {

        Digester digester = new Digester();

        digester.addObjectCreate("request", SearchDebtsRequest.class);
        digester.addObjectCreate("request/getQuittanceDebtInfo", DebtInfo.class);
        digester.addSetNext("request/getQuittanceDebtInfo", "setQuittanceDebtInfo");
        digester.addObjectCreate("request/getDebtInfo", DebtInfo.class);
        digester.addSetNext("request/getDebtInfo", "setDebtInfo");
        digester.addBeanPropertySetter("*/requestId", "requestId");
        digester.addBeanPropertySetter("*/searchType", "searchType");
        digester.addBeanPropertySetter("*/searchCriteria", "searchCriteria");
        digester.addBeanPropertySetter("request/login", "login");
        digester.addBeanPropertySetter("request/signature", "signature");

        try {
            return (SearchDebtsRequest) digester.parse(httpServletRequest.getInputStream());
        } catch (Exception ex) {
            throw new FlexPayException("Error parsing request", ex);
        }
    }

    private void processRequest(SearchDebtsRequest request, Writer writer) throws IOException {

        String requestId = request.getDebtInfo().getRequestId();

        try {

            if (!authenticate(request)) {
                writer.write(buildResponse(requestId, "8", "Bad authentication data"));
                return;
            }
            // validating request
            if (!validate(request)) {
                writer.write(buildResponse(requestId, "9", "Unknown request"));
                return;
            }

            QuittanceDetailsFinder quittanceDetailsFinder = getFinderFromContext();
            InfoRequest infoRequest = convertRequest(request);
            QuittanceDetailsResponse quittanceDetailsResponse = quittanceDetailsFinder.findQuittance(infoRequest);
            writer.write(buildResponse(quittanceDetailsResponse, requestId, request.getDebtInfoType(), "1", "OK"));
        } catch (FlexPayException e) {
            try {
                writer.write(buildResponse(requestId, "14", "Internal search error"));
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
        }
    }

    private boolean authenticate(SearchDebtsRequest request) throws FlexPayException {

        String login = request.getLogin();
        DebtInfo debtInfo = request.getDebtInfo();

        Certificate certificate;
        try {
            keyStore = KeyStoreUtil.loadKeyStore();

            if (!keyStore.isCertificateEntry(login)) {
                log.error("Can't load security certificate for user {}", login);
                return false;
            }

            certificate = keyStore.getCertificate(login);

        } catch (Exception ex) {
            throw new FlexPayException("Error loading certificate from keystore", ex);
        }

        try {
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initVerify(certificate);
            signature.update(debtInfo.getRequestId().getBytes());
            signature.update(debtInfo.getSearchType().toString().getBytes());
            signature.update(debtInfo.getSearchCriteria().getBytes());
            if (!signature.verify(Hex.decodeHex(request.getSignature().toCharArray()))) {
                log.error("Request digital signature is bad");
                return false;
            }
        } catch (Exception ex) {
            throw new FlexPayException("Error checking request digital signature", ex);
        }

        return true;

    }

	private boolean validate(SearchDebtsRequest request) {
        int searchType = request.getDebtInfo().getSearchType();

        return searchType == TYPE_ACCOUNT_NUMBER
                || searchType == TYPE_QUITTANCE_NUMBER
                || searchType == TYPE_APARTMENT_NUMBER
                || searchType == TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER
                || searchType == TYPE_ADDRESS;
	}

	private InfoRequest convertRequest(SearchDebtsRequest request) throws FlexPayException {

		String searchCriteria = request.getDebtInfo().getSearchCriteria();
		int searchType = request.getDebtInfo().getSearchType();

		if (TYPE_ACCOUNT_NUMBER == searchType) {
			return accountNumberRequest(searchCriteria, request.getDebtInfoType());
		} else if (TYPE_QUITTANCE_NUMBER == searchType) {
			return quittanceNumberRequest(searchCriteria, request.getDebtInfoType());
		} else if (TYPE_APARTMENT_NUMBER == searchType) {
			return apartmentNumberRequest(searchCriteria, request.getDebtInfoType());
        } else if (TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER == searchType) {
            return serviceProviderAccountNumberRequest(searchCriteria, request.getDebtInfoType());
        } else if (TYPE_ADDRESS == searchType) {
            return addressRequest(searchCriteria, request.getDebtInfoType());
        }

		return null;
	}

    private String buildResponse(String requestId, String statusCode, String statusMessage) throws FlexPayException {
        return buildResponse(requestId, statusCode, QUITTANCE_DEBT_REQUEST, statusMessage);
    }

    private String buildResponse(String requestId, String statusCode, Integer requestType, String statusMessage) throws FlexPayException {
        return buildResponse(null, requestId, requestType, statusCode, statusMessage);
    }

	private String buildResponse(QuittanceDetailsResponse quittanceDetailsResponse, String requestId, Integer requestType, String statusCode, String statusMessage) throws FlexPayException {

		Signature signature = initSignature();

		StringBuilder response = new StringBuilder();
		response.append("<response>");
        if (requestType == QUITTANCE_DEBT_REQUEST) {
            response.append("<quittanceDebtInfo>");
        } else if (requestType == DEBT_REQUEST) {
            response.append("<debtInfo>");
        }
		addNonEmptyField(response, "requestId", requestId, signature);

		if (quittanceDetailsResponse != null) {

			addNonEmptyField(response, "statusCode", quittanceDetailsResponse.getStatusCode(), signature);
			addNonEmptyField(response, "statusMessage", getStatusMessage(quittanceDetailsResponse.getStatusCode()), signature);

            if (quittanceDetailsResponse.getInfos() != null) {
                for (QuittanceInfo quittanceInfo : quittanceDetailsResponse.getInfos()) {
                    addQuittanceInfo(response, quittanceInfo, requestType, signature);
                }
            }
		} else {
			addNonEmptyField(response, "statusCode", statusCode, signature);
			addNonEmptyField(response, "statusMessage", statusMessage, signature);
		}

        if (requestType == QUITTANCE_DEBT_REQUEST) {
            response.append("</quittanceDebtInfo>");
        } else {
            response.append("</debtInfo>");
        }
		addSignature(response, signature);
		response.append("</response>");

		return response.toString();
	}

	private PrivateKey initKey() throws FlexPayException {
		try {
			keyStore = KeyStoreUtil.loadKeyStore();
			if (!keyStore.isKeyEntry(ApplicationConfig.getSelfKeyAlias())) {
				throw new FlexPayException("Unable to load security key for signing response");
			}

			PrivateKey key = (PrivateKey) keyStore.getKey(ApplicationConfig.getSelfKeyAlias(), ApplicationConfig.getSelfKeyPassword().toCharArray());
			if (key == null) {
				throw new FlexPayException("Unable to load security key for signing response (password is bad)");
			}

            return key;

		} catch (Exception e) {
			throw new FlexPayException("Error loading certificate from keystore", e);
		}
	}

	private Signature initSignature() throws FlexPayException {

		PrivateKey key = initKey();

		try {

            Signature signature = Signature.getInstance("SHA1withDSA");
			signature.initSign(key);

            return signature;

		} catch (Exception e) {
			throw new FlexPayException("Error initializing signature");
		}
	}

	private void addSignature(StringBuilder response, Signature signature) throws FlexPayException {

		try {
			response.append("<signature>").append(Hex.encodeHex(signature.sign())).append("</signature>");
		} catch (SignatureException e) {
			throw new FlexPayException("Error signing response", e);
		}
	}

	private void addQuittanceInfo(StringBuilder response, QuittanceInfo quittanceInfo, int requestType, Signature signature) throws FlexPayException {

        if (requestType == QUITTANCE_DEBT_REQUEST) {
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
            if (requestType == QUITTANCE_DEBT_REQUEST) {
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
            if (requestType == QUITTANCE_DEBT_REQUEST) {
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

            if (requestType == QUITTANCE_DEBT_REQUEST) {
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
        if (requestType == QUITTANCE_DEBT_REQUEST) {
		    response.append("</quittanceInfo>");
        }
	}

	private void addNonEmptyField(StringBuilder response, String tagName, Object field, Signature signature) throws FlexPayException {

        if (field == null) {
            return;
        }

        String fieldValue = field.toString();
        response.append("<").append(tagName).append(">").
                append(fieldValue).
                append("</").append(tagName).append(">");

        try {
            signature.update(fieldValue.getBytes());
        } catch (SignatureException e) {
            throw new FlexPayException("Error updating digital signature with value {}", fieldValue);
        }
	}

	private QuittanceDetailsFinder getFinderFromContext() {
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		return (QuittanceDetailsFinder) context.getBean("jmsQuittanceDetailsFinder");
	}

	private String getStatusMessage(int status) {
    	switch (status) {
			case STATUS_ACCOUNT_NOT_FOUND:
				return "Account not found";
			case STATUS_APARTMENT_NOT_FOUND:
				return "Apartment not found";
			case STATUS_INTERNAL_ERROR:
				return "Internal error";
			case STATUS_INVALID_QUITTANCE_NUMBER:
				return "Invalid quittance number";
			case STATUS_QUITTANCE_NOT_FOUND:
				return "Quittance not found";
			case STATUS_RECIEVE_TIMEOUT:
				return "Receive timeout";
			case STATUS_UNKNOWN_REQUEST:
				return "Unknown request";
			default:
				return null;
		}
	}
}
