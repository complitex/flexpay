package org.flexpay.payments.actions.quittance;

import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class SearchQuittanceServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(SearchQuittanceServlet.class);

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

		Writer writer = httpServletResponse.getWriter();
		String requestId = null;
		QuittanceDetailsFinder quittanceDetailsFinder = getFinderFromContext();

		try {
			SearchQuittanceExternalRequest request = parseRequest(httpServletRequest);
			requestId = request.getDebtInfoData().getRequestId();

			// validating request
			if (!validateExternalRequest(request)) {
				writer.write(buildResponse(requestId, "9", "Unknown request"));
				return;
			}

			QuittanceDetailsRequest quittanceDetailsRequest = convertRequest(request);
			QuittanceDetailsResponse quittanceDetailsResponse = quittanceDetailsFinder.findQuittance(quittanceDetailsRequest);
			writer.write(buildResponse(quittanceDetailsResponse, requestId, "1", "OK"));
		} catch (SAXException e) {
			writer.write(buildResponse(requestId, "9", "Unknown request"));
		} catch (FlexPayException e) {
			writer.write(buildResponse(requestId, "14", "Internal search error"));
		}
	}

	private SearchQuittanceExternalRequest parseRequest(HttpServletRequest httpServletRequest)  throws IOException, SAXException {

		Digester digester = initParser();
		return (SearchQuittanceExternalRequest) digester.parse(httpServletRequest.getInputStream());
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

		// TODO validate login and signature

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

	private String buildResponse(QuittanceDetailsResponse quittanceDetailsResponse, String requestId, String statusCode, String statusMessage) {

		StringBuilder response = new StringBuilder();

		response.append("<response>");
		response.append("<debtInfo>");

		response.append("<requestId>");
		response.append(requestId);
		response.append("</requestId>");

		if (quittanceDetailsResponse != null) {

			response.append("<statusCode>");
			response.append(quittanceDetailsResponse.getErrorCode());
			response.append("</statusCode>");

			response.append("<statusMessage>");
			response.append(getStatusMessage(quittanceDetailsResponse.getErrorCode()));
			response.append("</statusMessage>");

			if (quittanceDetailsResponse.getInfos() != null) {
				for (QuittanceDetailsResponse.QuittanceInfo quittanceInfo : quittanceDetailsResponse.getInfos()) {
					addQuittanceInfo(response, quittanceInfo);
				}
			}

		} else {
			response.append("<statusCode>");
			response.append(statusCode);
			response.append("</statusCode>");

			response.append("<statusMessage>");
			response.append(statusMessage);
			response.append("</statusMessage>");
		}

		response.append("</debtInfo>");
		response.append("<signature>");
		// TODO add signature for response
		response.append("</signature>");
		response.append("</response>");

		return response.toString();
	}

	private String buildResponse(String requestId, String statusCode, String statusMessage) {

		return buildResponse(null, requestId ,statusCode, statusMessage);
	}

	private void addQuittanceInfo(StringBuilder response, QuittanceDetailsResponse.QuittanceInfo quittanceInfo) {

		response.append("<quittanceInfo>");
		addNonEmptyField(response, "quittanceNumber", quittanceInfo.getQuittanceNumber());
		addNonEmptyField(response, "accountNumber", quittanceInfo.getAccountNumber());
		addNonEmptyField(response, "creationDate", quittanceInfo.getCreationDate());
		addNonEmptyField(response, "personFirstName", quittanceInfo.getPersonFirstName());
		addNonEmptyField(response, "personLastName", quittanceInfo.getPersonLastName());
		addNonEmptyField(response, "personMiddleName", quittanceInfo.getPersonMiddleName());
		addNonEmptyField(response, "country", quittanceInfo.getCountry());
		addNonEmptyField(response, "region", quittanceInfo.getRegion());
		addNonEmptyField(response, "town", quittanceInfo.getTown());
		addNonEmptyField(response, "streetName", quittanceInfo.getStreetName());
		addNonEmptyField(response, "streetType", quittanceInfo.getStreetType());
		addNonEmptyField(response, "buildingNumber", quittanceInfo.getBuildingNumber());
		addNonEmptyField(response, "buildingBulk", quittanceInfo.getBuildingBulk());
		addNonEmptyField(response, "apartmentNumber", quittanceInfo.getApartmentNumber());
		addNonEmptyField(response, "totalToPay", quittanceInfo.getTotalToPay());
		addNonEmptyField(response, "totalPayed", quittanceInfo.getTotalPayed());

		for (QuittanceDetailsResponse.QuittanceInfo.ServiceDetails serviceDetails : quittanceInfo.getDetailses()) {
			response.append("<serviceDetails>");

			addNonEmptyField(response, "incomingBalance", serviceDetails.getIncomingBalance());
			addNonEmptyField(response, "outgoingBalance", serviceDetails.getOutgoingBalance());
			addNonEmptyField(response, "amount", serviceDetails.getAmount());
			addNonEmptyField(response, "expence", serviceDetails.getExpence());
			addNonEmptyField(response, "rate", serviceDetails.getRate());
			addNonEmptyField(response, "recalculation", serviceDetails.getRecalculation());
			addNonEmptyField(response, "benefit", serviceDetails.getBenifit());
			addNonEmptyField(response, "subsidy", serviceDetails.getSubsidy());
			addNonEmptyField(response, "payment", serviceDetails.getPayment());
			addNonEmptyField(response, "payed", serviceDetails.getPayed());
			addNonEmptyField(response, "serviceProviderAccount", serviceDetails.getServiceProviderAccount());
			addNonEmptyField(response, "personFirstName", quittanceInfo.getPersonFirstName());
			addNonEmptyField(response, "personLastName", quittanceInfo.getPersonLastName());
			addNonEmptyField(response, "personMiddleName", quittanceInfo.getPersonMiddleName());
			addNonEmptyField(response, "country", quittanceInfo.getCountry());
			addNonEmptyField(response, "region", quittanceInfo.getRegion());
			addNonEmptyField(response, "town", quittanceInfo.getTown());
			addNonEmptyField(response, "streetName", quittanceInfo.getStreetName());
			addNonEmptyField(response, "streetType", quittanceInfo.getStreetType());
			addNonEmptyField(response, "buildingNumber", quittanceInfo.getBuildingNumber());
			addNonEmptyField(response, "buildingBulk", quittanceInfo.getBuildingBulk());
			addNonEmptyField(response, "apartmentNumber", quittanceInfo.getApartmentNumber());

			if (serviceDetails.getAttributes() != null) {
				for (QuittanceDetailsResponse.ServiceAttribute serviceAttribute : serviceDetails.getAttributes()) {
					response.append("<serviceAttribute>");
					addNonEmptyField(response, "name", serviceAttribute.getName());
					addNonEmptyField(response, "value", serviceAttribute.getValue());
					response.append("</serviceAttribute>");
				}
			}

			response.append("</serviceDetails>");
		}
		response.append("</quittanceInfo>");
	}

	private void addNonEmptyField(StringBuilder response, String tagName, Object field) {

		if (field != null) {
			response.append("<").append(tagName).append(">");
			response.append(field);
			response.append("</").append(tagName).append(">");
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
