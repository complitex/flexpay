package org.flexpay.payments.actions.quittance;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
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

public class SearchQuittanceServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(SearchQuittanceServlet.class);

	private KeyStore keyStore;

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

		Writer writer = httpServletResponse.getWriter();
		String requestId = null;
		QuittanceDetailsFinder quittanceDetailsFinder = getFinderFromContext();

		SearchQuittanceExternalRequest request = null;
		try {
			request = parseRequest(httpServletRequest);
			requestId = request.getDebtInfoData().getRequestId();
		} catch (FlexPayException e) {
			try {
				writer.write(buildResponse(requestId, "9", "Unknown request"));
			} catch (FlexPayException e1) {
				log.error("Error building response", e1);
			}
		}
		
		try {
			if (!authenticate(request)) {
				writer.write(buildResponse(requestId, "8", "Bad authentication data"));
				return;
			}
			
			// validating request
			if (!validateExternalRequest(request)) {
				writer.write(buildResponse(requestId, "9", "Unknown request"));
				return;
			}

			QuittanceDetailsRequest quittanceDetailsRequest = convertRequest(request);
			QuittanceDetailsResponse quittanceDetailsResponse = quittanceDetailsFinder.findQuittance(quittanceDetailsRequest);
			writer.write(buildResponse(quittanceDetailsResponse, requestId, "1", "OK"));
		} catch (FlexPayException e) {
			try {
				writer.write(buildResponse(requestId, "14", "Internal search error"));
			} catch (FlexPayException e1) {
				log.error("Error building response", e1);
			}
		}
	}

	private SearchQuittanceExternalRequest parseRequest(HttpServletRequest httpServletRequest)  throws FlexPayException {

		try {
			Digester digester = initParser();
			return (SearchQuittanceExternalRequest) digester.parse(httpServletRequest.getInputStream());
		} catch (Exception e) {
			throw new FlexPayException("Error parsing request", e);	
		}
	}

	private boolean authenticate(SearchQuittanceExternalRequest externalRequest) throws FlexPayException {

		Certificate certificate = null;
		try {
			keyStore = KeyStoreUtil.loadKeyStore();

			if (!keyStore.isCertificateEntry(externalRequest.getLogin())) {
				log.error("Can't load security certificate for user {}", externalRequest.getLogin());
				return false;
			}

			certificate = keyStore.getCertificate(externalRequest.getLogin());

		} catch (Exception e) {
			throw new FlexPayException("Error loading certificate from keystore", e);
		}

		try {
			Signature signature = Signature.getInstance("SHA1withDSA");
			signature.initVerify(certificate);
			signature.update(externalRequest.getDebtInfoData().getRequestId().getBytes());
			signature.update(externalRequest.getDebtInfoData().getSearchType().getBytes());
			signature.update(externalRequest.getDebtInfoData().getSearchCriteria().getBytes());
			if (!signature.verify(Hex.decodeHex(externalRequest.getSignature().toCharArray()))) {
				log.error("Request digital signature is bad");
				return false;
			}
		} catch (Exception e) {
			throw new FlexPayException("Error checking request digital signature", e);
		}

		return true;
	}

	private boolean validateExternalRequest(SearchQuittanceExternalRequest externalRequest) {

		try {
			String searchTypeString = externalRequest.getDebtInfoData().getSearchType();
			int searchType = Integer.parseInt(searchTypeString);

			if (searchType != QuittanceDetailsRequest.TYPE_ACCOUNT_NUMBER &&
				searchType != QuittanceDetailsRequest.TYPE_QUITTANCE_NUMBER &&
				searchType != QuittanceDetailsRequest.TYPE_APARTMENT_NUMBER) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	private QuittanceDetailsRequest convertRequest(SearchQuittanceExternalRequest externalRequest) throws FlexPayException {

		String searchCriteria = externalRequest.getDebtInfoData().getSearchCriteria();
		String searchTypeString = externalRequest.getDebtInfoData().getSearchType();
		int searchType = Integer.parseInt(searchTypeString);

		if (QuittanceDetailsRequest.TYPE_ACCOUNT_NUMBER == searchType) {
			return QuittanceDetailsRequest.accountNumberRequest(searchCriteria);
		} else if (QuittanceDetailsRequest.TYPE_QUITTANCE_NUMBER == searchType) {
			return QuittanceDetailsRequest.quittanceNumberRequest(searchCriteria);
		} else if (QuittanceDetailsRequest.TYPE_APARTMENT_NUMBER == searchType) {
			// TODO deal with this kind of search
			return null;
		}

		return null;
	}

	private String buildResponse(QuittanceDetailsResponse quittanceDetailsResponse, String requestId, String statusCode, String statusMessage) throws FlexPayException {

		Signature signature = initSignature();

		StringBuilder response = new StringBuilder();
		response.append("<response>");
		response.append("<debtInfo>");
		addNonEmptyField(response, "requestId", requestId, signature);

		if (quittanceDetailsResponse != null) {

			addNonEmptyField(response, "statusCode", quittanceDetailsResponse.getErrorCode(), signature);
			addNonEmptyField(response, "statusMessage", getStatusMessage(quittanceDetailsResponse.getErrorCode()), signature);

			if (quittanceDetailsResponse.getInfos() != null) {
				for (QuittanceDetailsResponse.QuittanceInfo quittanceInfo : quittanceDetailsResponse.getInfos()) {
					addQuittanceInfo(response, quittanceInfo, signature);
				}
			}
		} else {
			addNonEmptyField(response, "statusCode", statusCode, signature);
			addNonEmptyField(response, "statusMessage", statusMessage, signature);
		}

		response.append("</debtInfo>");
		addSignature(response, signature);
		response.append("</response>");

		return response.toString();
	}

	private PrivateKey initKey() throws FlexPayException {
		PrivateKey key  = null;
		try {
			keyStore = KeyStoreUtil.loadKeyStore();

			if (!keyStore.isKeyEntry(ApplicationConfig.getSelfKeyAlias())) {
				throw new FlexPayException("Unable to load security key for signing response");
			}

			key = (PrivateKey) keyStore.getKey(ApplicationConfig.getSelfKeyAlias(), ApplicationConfig.getSelfKeyPassword().toCharArray());
			if (key == null) {
				throw new FlexPayException("Unable to load security key for signing response (password is bad)");
			}

		} catch (Exception e) {
			throw new FlexPayException("Error loading certificate from keystore", e);
		}
		return key;
	}

	private Signature initSignature() throws FlexPayException {

		PrivateKey key = initKey();

		Signature signature = null;
		try {
			signature = Signature.getInstance("SHA1withDSA");
			signature.initSign(key);
		} catch (Exception e) {
			throw new FlexPayException("Error initializing signature");
		}
		return signature;
	}

	private void addSignature(StringBuilder response, Signature signature) throws FlexPayException {

		try {
			response.append("<signature>");
			response.append(Hex.encodeHex(signature.sign()));
			response.append("</signature>");
		} catch (SignatureException e) {
			throw new FlexPayException("Error signing response", e);
		}
	}

	private String buildResponse(String requestId, String statusCode, String statusMessage) throws FlexPayException {

		return buildResponse(null, requestId ,statusCode, statusMessage);
	}

	private void addQuittanceInfo(StringBuilder response, QuittanceDetailsResponse.QuittanceInfo quittanceInfo, Signature signature) throws FlexPayException {

		response.append("<quittanceInfo>");
		addNonEmptyField(response, "quittanceNumber", quittanceInfo.getQuittanceNumber(), signature);
		addNonEmptyField(response, "accountNumber", quittanceInfo.getAccountNumber(), signature);
		addNonEmptyField(response, "creationDate", quittanceInfo.getCreationDate(), signature);
		addNonEmptyField(response, "personFirstName", quittanceInfo.getPersonFirstName(), signature);
		addNonEmptyField(response, "personLastName", quittanceInfo.getPersonLastName(), signature);
		addNonEmptyField(response, "personMiddleName", quittanceInfo.getPersonMiddleName(), signature);
		addNonEmptyField(response, "country", quittanceInfo.getCountry(), signature);
		addNonEmptyField(response, "region", quittanceInfo.getRegion(), signature);
		addNonEmptyField(response, "town", quittanceInfo.getTown(), signature);
		addNonEmptyField(response, "streetName", quittanceInfo.getStreetName(), signature);
		addNonEmptyField(response, "streetType", quittanceInfo.getStreetType(), signature);
		addNonEmptyField(response, "buildingNumber", quittanceInfo.getBuildingNumber(), signature);
		addNonEmptyField(response, "buildingBulk", quittanceInfo.getBuildingBulk(), signature);
		addNonEmptyField(response, "apartmentNumber", quittanceInfo.getApartmentNumber(), signature);
		addNonEmptyField(response, "totalToPay", quittanceInfo.getTotalToPay(), signature);
		addNonEmptyField(response, "totalPayed", quittanceInfo.getTotalPayed(), signature);

		for (QuittanceDetailsResponse.QuittanceInfo.ServiceDetails serviceDetails : quittanceInfo.getDetailses()) {
			response.append("<serviceDetails>");

			addNonEmptyField(response, "incomingBalance", serviceDetails.getIncomingBalance(), signature);
			addNonEmptyField(response, "outgoingBalance", serviceDetails.getOutgoingBalance(), signature);
			addNonEmptyField(response, "amount", serviceDetails.getAmount(), signature);
			addNonEmptyField(response, "expence", serviceDetails.getExpence(), signature);
			addNonEmptyField(response, "rate", serviceDetails.getRate(), signature);
			addNonEmptyField(response, "recalculation", serviceDetails.getRecalculation(), signature);
			addNonEmptyField(response, "benefit", serviceDetails.getBenifit(), signature);
			addNonEmptyField(response, "subsidy", serviceDetails.getSubsidy(), signature);
			addNonEmptyField(response, "payment", serviceDetails.getPayment(), signature);
			addNonEmptyField(response, "payed", serviceDetails.getPayed(), signature);
			addNonEmptyField(response, "serviceProviderAccount", serviceDetails.getServiceProviderAccount(), signature);
			addNonEmptyField(response, "personFirstName", quittanceInfo.getPersonFirstName(), signature);
			addNonEmptyField(response, "personLastName", quittanceInfo.getPersonLastName(), signature);
			addNonEmptyField(response, "personMiddleName", quittanceInfo.getPersonMiddleName(), signature);
			addNonEmptyField(response, "country", quittanceInfo.getCountry(), signature);
			addNonEmptyField(response, "region", quittanceInfo.getRegion(), signature);
			addNonEmptyField(response, "town", quittanceInfo.getTown(), signature);
			addNonEmptyField(response, "streetName", quittanceInfo.getStreetName(), signature);
			addNonEmptyField(response, "streetType", quittanceInfo.getStreetType(), signature);
			addNonEmptyField(response, "buildingNumber", quittanceInfo.getBuildingNumber(), signature);
			addNonEmptyField(response, "buildingBulk", quittanceInfo.getBuildingBulk(), signature);
			addNonEmptyField(response, "apartmentNumber", quittanceInfo.getApartmentNumber(), signature);

			if (serviceDetails.getAttributes() != null) {
				for (QuittanceDetailsResponse.ServiceAttribute serviceAttribute : serviceDetails.getAttributes()) {
					response.append("<serviceAttribute>");
					addNonEmptyField(response, "name", serviceAttribute.getName(), signature);
					addNonEmptyField(response, "value", serviceAttribute.getValue(), signature);
					response.append("</serviceAttribute>");
				}
			}

			response.append("</serviceDetails>");
		}
		response.append("</quittanceInfo>");
	}

	private void addNonEmptyField(StringBuilder response, String tagName, Object field, Signature signature) throws FlexPayException {

		if (field != null) {
			String fieldValue = field.toString();
			response.append("<").append(tagName).append(">");
			response.append(fieldValue);
			response.append("</").append(tagName).append(">");

			try {
				signature.update(fieldValue.getBytes());
			} catch (SignatureException e) {
				throw new FlexPayException("Error updating digital signature with value {}", fieldValue);
			}
		}
	}

	private Digester initParser() {

		Digester digester = new Digester();

		digester.addObjectCreate("request", SearchQuittanceExternalRequest.class);
		digester.addObjectCreate("request/getDebtInfo", SearchQuittanceExternalRequest.GetDebtInfo.class);
		digester.addSetNext("request/getDebtInfo", "setDebtInfoData");
		digester.addBeanPropertySetter("request/login", "login");
		digester.addBeanPropertySetter("request/signature", "signature");
		digester.addBeanPropertySetter("request/getDebtInfo/requestId", "requestId");
		digester.addBeanPropertySetter("request/getDebtInfo/searchType", "searchType");
		digester.addBeanPropertySetter("request/getDebtInfo/searchCriteria", "searchCriteria");

		return digester;
	}

	private QuittanceDetailsFinder getFinderFromContext() {

		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		return (QuittanceDetailsFinder) context.getBean("jmsQuittanceDetailsFinder");
	}

	private String getStatusMessage(int errorCode) {

		switch (errorCode) {
			case QuittanceDetailsResponse.CODE_ERROR_ACCOUNT_NOT_FOUND:
				return "Account not found";

			case QuittanceDetailsResponse.CODE_ERROR_APARTMENT_NOT_FOUND:
				return "Apartment not found";

			case QuittanceDetailsResponse.CODE_ERROR_INTERNAL_ERROR:
				return "Internal error";

			case QuittanceDetailsResponse.CODE_ERROR_INVALID_QUITTANCE_NUMBER:
				return "Invalid quittance number";

			case QuittanceDetailsResponse.CODE_ERROR_QUITTANCE_NOT_FOUND:
				return "Quittance not found";

			case QuittanceDetailsResponse.CODE_ERROR_RECIEVE_TIMEOUT:
				return "Receive timeout";

			case QuittanceDetailsResponse.CODE_ERROR_UNKNOWN_REQUEST:
				return "Unknown request";

			default:
				return null;
		}
	}
}
