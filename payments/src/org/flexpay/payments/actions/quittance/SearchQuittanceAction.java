package org.flexpay.payments.actions.quittance;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.actions.OperatorAWPActionSupport;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.quittance.ConsumerAttributes;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.util.ServiceTypesMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse.*;

public class SearchQuittanceAction extends OperatorAWPActionSupport {

	private static final String SEARCH_TYPE_EIRC_ACCOUNT = "EIRC_ACCOUNT";
	private static final String SEARCH_TYPE_QUITTANCE_NUMBER = "QUITTANCE_NUMBER";
	private static final String SEARCH_TYPE_ADDRESS = "ADDRESS";

	// form data
	private String searchType;
	private String searchCriteria;
	private QuittanceDetailsResponse.QuittanceInfo[] quittanceInfos;
	private String actionName;

	// required services
	private AddressService addressService;
	private ApartmentService apartmentService;
	private PersonService personService;
	private QuittanceDetailsFinder quittanceDetailsFinder;
	private SPService spService;
	private ServiceProviderService serviceProviderService;
	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;

	private ServiceTypesMapper serviceTypesMapper;

	@NotNull
    @Override
	protected String doExecute() throws Exception {

		QuittanceDetailsRequest request = buildQuittanceRequest();
		QuittanceDetailsResponse response = quittanceDetailsFinder.findQuittance(request);

		if (response.isSuccess()) {
			quittanceInfos = response.getInfos();
			filterSubservices();
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
			Apartment apartment = apartmentService.readFull(
					new Stub<Apartment>(Long.parseLong(searchCriteria)));
			String indx = masterIndexService.getMasterIndex(apartment);
			if (indx == null) {
				throw new FlexPayException("No master index for apartment #" + searchCriteria);
			}
			return QuittanceDetailsRequest.apartmentNumberRequest(indx);
		} else {
			throw new FlexPayException("Bad search request: type must be one of: " + SEARCH_TYPE_ADDRESS + ", "
									   + SEARCH_TYPE_EIRC_ACCOUNT + ", " + SEARCH_TYPE_QUITTANCE_NUMBER);
		}
	}

	// eliminates services with non-positive outgoing balances
	private void filterNegativeSumms() {

		List<QuittanceInfo> filteredInfos = list();
		for (QuittanceInfo info : quittanceInfos) {
			BigDecimal total = new BigDecimal("0.00");

			List<QuittanceInfo.ServiceDetails> filteredDetails = list();
			for (QuittanceInfo.ServiceDetails sd : info.getDetailses()) {
				if (sd.getOutgoingBalance().compareTo(BigDecimal.ZERO) > 0) {
					filteredDetails.add(sd);
					total = total.add(sd.getOutgoingBalance());
				} else {
					sd.setOutgoingBalance(new BigDecimal("0.00"));
					filteredDetails.add(sd);
				}
			}

			if (!filteredDetails.isEmpty()) {
				info.setDetailses(filteredDetails.toArray(new QuittanceInfo.ServiceDetails[filteredDetails.size()]));
				info.setTotalToPay(total);
				filteredInfos.add(info);
			}
		}

		this.quittanceInfos = filteredInfos.toArray(new QuittanceInfo[filteredInfos.size()]);
	}

	// eliminates subservices information from quittances info
	private void filterSubservices() {

		for (QuittanceInfo quittanceInfo : quittanceInfos) {
			List<QuittanceInfo.ServiceDetails> filtered = list();
			BigDecimal totalToPay = new BigDecimal("0.00");

			for (QuittanceInfo.ServiceDetails details : quittanceInfo.getDetailses()) {
				if (isNotSubservice(details.getServiceMasterIndex())) {
					filtered.add(details);
					totalToPay = totalToPay.add(details.getOutgoingBalance());
				} else {
					log.info("Service '{}' (masterindex) filtered out", details.getServiceMasterIndex());
				}
			}

			quittanceInfo.setDetailses(filtered.toArray(new QuittanceInfo.ServiceDetails[filtered.size()]));
			quittanceInfo.setTotalToPay(totalToPay);
		}

	}

	public boolean isNotSubservice(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
        if (serviceId == null) {
            log.error("Error! serviceId is null");
            return false;
        }

		Service service = spService.readFull(new Stub<Service>(serviceId));
		assert service != null;
		return service.isNotSubservice();
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
				return getText("payments.errors.search.debts_not_found");
			case CODE_ERROR_UNKNOWN_REQUEST:
				return getText("payments.errors.search.unknown_request");
			default:
				return getText("payments.errors.search.unknown_error");
		}
	}

	@NotNull
    @Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	// rendering utility methods
	public boolean resultsAreNotEmpty() {
		return quittanceInfos.length > 0;
	}

	public String getServiceName(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		Service service = spService.readFull(new Stub<Service>(serviceId));
		assert service != null;
		return service.getServiceType().getName();
	}

	public String getProviderName(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		Service service = spService.readFull(new Stub<Service>(serviceId));
		assert service != null;
		ServiceProvider serviceProvider = serviceProviderService.read(service.providerStub());
		assert serviceProvider != null;
		return serviceProvider.getName();
	}

	public String getErcAccount(ServiceAttribute[] attributes) {

		for (ServiceAttribute attribute : attributes) {
			if (attribute.getName().equals(ConsumerAttributes.ATTR_ERC_ACCOUNT)) {
				return attribute.getValue();
			}
		}

		return null;
	}

	public Long getServiceId(String serviceMasterIndex) {
		Stub<Service> stub = correctionsService.findCorrection(serviceMasterIndex,
				Service.class, masterIndexService.getMasterSourceDescriptionStub());
		return stub != null ? stub.getId() : null;
	}

	public String getPersonFio(QuittanceInfo quittanceInfo) {

		String personMasterIndex = quittanceInfo.getPersonMasterIndex();
		if (personMasterIndex != null) {
			Stub<Person> stub = correctionsService.findCorrection(personMasterIndex,
					Person.class, masterIndexService.getMasterSourceDescriptionStub());
			Person person = personService.readFull(stub);
			return person.getFIO();
		} else {
			return quittanceInfo.getPersonFio();
		}
	}

	public String getEircAccount(QuittanceInfo quittanceInfo) {
		return quittanceInfo.getAccountNumber();
	}

	public String getApartmentAddress(QuittanceInfo quittanceInfo) throws Exception {

		String apartmentMasterIndex = quittanceInfo.getApartmentMasterIndex();
		if (apartmentMasterIndex != null) {
			Stub<Apartment> stub = correctionsService.findCorrection(apartmentMasterIndex,
					Apartment.class, masterIndexService.getMasterSourceDescriptionStub());
			return addressService.getAddress(stub, getLocale());
		} else {
			return quittanceInfo.getAddress();
		}
	}

	public BigDecimal getTotalToPay() {

		BigDecimal total = new BigDecimal("0.00");
		for (QuittanceInfo info : quittanceInfos) {
			for (QuittanceInfo.ServiceDetails details : info.getDetailses()) {
				total = total.add(details.getOutgoingBalance());
			}
		}

		return total;
	}

	public String getServiceFullIndex(String quittanceId, String serviceId) {
		return ServiceFullIndexUtil.getServiceFullIndex(quittanceId, serviceId);
	}

	public String getApartmentId() {

		if (SEARCH_TYPE_ADDRESS.equals(searchType)) {
			return searchCriteria;
		}

		return null;
	}

	public String getMBServiceCode(String serviceMasterIndex) {

		Long serviceId = getServiceId(serviceMasterIndex);
		if (serviceId == null) {
			log.warn("Cannot find local service id for master index {}", serviceMasterIndex);
			return null;
		}

		Service service = spService.readFull(new Stub<Service>(serviceId));
		if (service == null) {
			log.warn("Cannot find service with id {} (master index {})", serviceId, serviceMasterIndex);
			return null;
		}

		return serviceTypesMapper.getMegabankCode(service.serviceTypeStub());
	}

	// form data
	public String getRedirectActionName() {
		return actionName;
	}

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
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setServiceTypesMapper(ServiceTypesMapper serviceTypesMapper) {
		this.serviceTypesMapper = serviceTypesMapper;
	}

	public static class ServiceFullIndexUtil {

		private static final String DELIMITER = "z";

		public static String getServiceIdFromIndex(String serviceFullIndex) {
			return serviceFullIndex.substring(serviceFullIndex.indexOf(DELIMITER) + 1);
		}

		public static String getServiceFullIndex(String quittanceId, String serviceId) {
			return quittanceId + DELIMITER + serviceId;
		}
	}
}
