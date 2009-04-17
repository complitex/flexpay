package org.flexpay.payments.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.impl.stub.QuittanceDetailsFinderStubImpl;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse.*;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.util.config.ApplicationConfig;

public class SearchQuittanceAction extends FPActionSupport {

	private static final String SEARCH_TYPE_EIRC_ACCOUNT = "EIRC_ACCOUNT";
	private static final String SEARCH_TYPE_QUITTANCE_NUMBER = "QUITTANCE_NUMBER";
	private static final String SEARCH_TYPE_ADDRESS = "ADDRESS";

	// form data
	private String searchType;
	private String searchCriteria;
	private QuittanceDetailsResponse.QuittanceInfo[] quittanceInfos;	

	// required services
	private QuittanceDetailsFinder quittanceDetailsFinder;
	private SPService spService;

	@NotNull
	protected String doExecute() throws Exception {

		// TODO get rid of stub implementation
//		quittanceDetailsFinder = new QuittanceDetailsFinderStubImpl();

		QuittanceDetailsRequest request = buildQuittanceRequest();
		QuittanceDetailsResponse response = quittanceDetailsFinder.findQuittance(request);

		if (response.isSuccess()) {
			quittanceInfos = response.getInfos();
		} else {
			addActionError(getErrorMessage(response.getErrorCode()));
		}
		
		return SUCCESS;
	}

	private QuittanceDetailsRequest buildQuittanceRequest() throws FlexPayException {

		if (SEARCH_TYPE_EIRC_ACCOUNT.equals(searchType)) {
			return QuittanceDetailsRequest.accountNumberRequest(searchCriteria);
		} else if (SEARCH_TYPE_QUITTANCE_NUMBER.equals(searchType)) {
			return QuittanceDetailsRequest.quittanceNumberRequest(searchCriteria);
		} else if (SEARCH_TYPE_ADDRESS.equals(searchType)) {
			return QuittanceDetailsRequest.apartmentNumberRequest(searchCriteria);
		} else {
			throw new FlexPayException("Bad search request: type must be one of: " + SEARCH_TYPE_ADDRESS + ", "
									   + SEARCH_TYPE_EIRC_ACCOUNT + ", " + SEARCH_TYPE_QUITTANCE_NUMBER);
		}
	}

	private String getErrorMessage(int errorCode) {
		switch (errorCode) {
			case CODE_ERROR_ACCOUNT_NOT_FOUND:
				return getText("payments.errors.search.account_not_found");
			case CODE_ERROR_APARTMENT_NOT_FOUND:
				return getText("payments.errors.search.apartment_not_found");
			case CODE_ERROR_INTERNAL_ERROR:
				return getText("payments.errors.search.internal_error");
			case CODE_ERROR_INVALID_QUITTANCE_NUMBER:
				return getText("payments.errors.search.invalid_quittance_number");
			case CODE_ERROR_QUITTANCE_NOT_FOUND:
				return getText("payments.errors.search.quittance_not_found");
			case CODE_ERROR_UNKNOWN_REQUEST:
				return getText("payments.errors.search.unknown_request");
			default:
				return getText("payments.errors.search.unknown_error");
		}
	}

	@NotNull
	protected String getErrorResult() {

		return SUCCESS;
	}

	// rendering utility methods
	public boolean resultsAreNotEmpty() {

		return quittanceInfos.length > 0;
	}

	public String getServiceName(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		Service service = spService.read(new Stub<Service>(serviceId));
		return service.getServiceType().getName();
	}

	public String getProviderName(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		Service service = spService.read(new Stub<Service>(serviceId));
		return service.getServiceProvider().getName();		
	}

	public Long getServiceId(String serviceMasterIndex) {
	
		return Long.parseLong(serviceMasterIndex.substring(ApplicationConfig.getInstanceId().length()));
		
	}

	// form data
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public QuittanceInfo[] getQuittanceInfos() {
		return quittanceInfos;
	}

	@Required
	public void setQuittanceDetailsFinder(QuittanceDetailsFinder quittanceDetailsFinder) {
		this.quittanceDetailsFinder = quittanceDetailsFinder;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
