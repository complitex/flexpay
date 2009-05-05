package org.flexpay.payments.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse.*;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.util.config.ApplicationConfig;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;

public class SearchQuittanceAction extends FPActionSupport {

	private static final String SEARCH_TYPE_EIRC_ACCOUNT = "EIRC_ACCOUNT";
	private static final String SEARCH_TYPE_QUITTANCE_NUMBER = "QUITTANCE_NUMBER";
	private static final String SEARCH_TYPE_ADDRESS = "ADDRESS";

	// form data
	private String searchType;
	private String searchCriteria;
	private QuittanceDetailsResponse.QuittanceInfo[] quittanceInfos;
	private ApartmentService apartmentService;
	private PersonService personService;

	private String actionName;

	// required services
	private QuittanceDetailsFinder quittanceDetailsFinder;
	private SPService spService;
	private ServiceProviderService serviceProviderService;

	@NotNull
	protected String doExecute() throws Exception {

		QuittanceDetailsRequest request = buildQuittanceRequest();
		QuittanceDetailsResponse response = quittanceDetailsFinder.findQuittance(request);

		if (response.isSuccess()) {
			quittanceInfos = response.getInfos();
			filterNegativeSumms();
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

	public String getRedirectActionName() {
		return actionName;
	}

	// rendering utility methods
	public boolean resultsAreNotEmpty() {

		return quittanceInfos.length > 0;
	}

	private void filterNegativeSumms() {

		for (QuittanceInfo info : quittanceInfos) {

			for (QuittanceInfo.ServiceDetails sd : info.getDetailses()) {
				if (sd.getOutgoingBalance().compareTo(BigDecimal.ZERO) < 0) {
					sd.setOutgoingBalance(new BigDecimal("0.00"));
				}
			}
		}
	}

	public String getServiceName(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		Service service = spService.read(new Stub<Service>(serviceId));
		return service.getServiceType().getName();
	}

	public String getProviderName(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		Service service = spService.read(new Stub<Service>(serviceId));
		ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(service.getServiceProvider().getId()));
		return serviceProvider.getName();
	}

	public Long getServiceId(String serviceMasterIndex) {

		return getLocalId(serviceMasterIndex);
	}

	public String getPersonFio(QuittanceInfo quittanceInfo) {

		String personMasterIndex = quittanceInfo.getPersonMasterIndex();
		if (personMasterIndex != null) {
			Long personId = getLocalId(personMasterIndex);
			Person person = personService.read(new Stub<Person>(personId));
			return person.getFIO();
		} else {
			return quittanceInfo.getPersonFio();
		}
	}

	public String getApartmentAddress(QuittanceInfo quittanceInfo) throws FlexPayException {

		String apartmentMasterIndex = quittanceInfo.getApartmentMasterIndex();
		if (apartmentMasterIndex != null) {
			Long apartmentId = getLocalId(apartmentMasterIndex);
			return apartmentService.getAddress(new Stub<Apartment>(apartmentId));
		} else {
			return quittanceInfo.getAddress();
		}
	}

	private Long getLocalId(String masterIndex) {

		// TODO how to properly get service id by index? current implementation is hack
		return Long.parseLong(masterIndex.substring(ApplicationConfig.getInstanceId().length() + 1)); // +1 is for '-' delimeter
	}

	public boolean isNotSubservice(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		Service service = spService.read(new Stub<Service>(serviceId));
		return service.isNotSubservice();
	}

	public BigDecimal getTotalPayable() {

		BigDecimal total = new BigDecimal("0.00");

		if (quittanceInfos.length > 0) {
			for (QuittanceInfo.ServiceDetails serviceDetails : quittanceInfos[0].getDetailses()) {
				total = total.add(serviceDetails.getOutgoingBalance());
			}
		}



//		for (QuittanceDetails qd : quittance.getQuittanceDetails()) {
//			if (!qd.getConsumer().getService().isSubService()) {
//				total = total.add(qd.getOutgoingBalance());
//			}
//		}

		return total;
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

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Required
	public void setQuittanceDetailsFinder(QuittanceDetailsFinder quittanceDetailsFinder) {
		this.quittanceDetailsFinder = quittanceDetailsFinder;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
