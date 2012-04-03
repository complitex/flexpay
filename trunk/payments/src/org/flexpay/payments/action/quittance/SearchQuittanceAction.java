package org.flexpay.payments.action.quittance;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.action.OperatorAWPActionSupport;
import org.flexpay.payments.action.outerrequest.request.GetQuittanceDebtInfoRequest;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.GetQuittanceDebtInfoResponse;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.flexpay.payments.action.outerrequest.request.response.Status;
import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;
import org.flexpay.payments.action.outerrequest.request.response.data.QuittanceInfo;
import org.flexpay.payments.action.outerrequest.request.response.data.ServiceDetails;
import org.flexpay.payments.persistence.OperationActionLog;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.OperationActionLogService;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.TradingDay;
import org.flexpay.payments.util.ServiceTypesMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class SearchQuittanceAction extends OperatorAWPActionSupport {

	private static final String SEARCH_TYPE_EIRC_ACCOUNT = "EIRC_ACCOUNT";
	private static final String SEARCH_TYPE_QUITTANCE_NUMBER = "QUITTANCE_NUMBER";
	private static final String SEARCH_TYPE_ADDRESS = "ADDRESS";

	private String searchType;
	private String searchCriteria;
    private String parentSearchCriteria;
    private String apartmentNumber;
	private List<QuittanceInfo> quittanceInfos;
	private String actionName;

    private OperationActionLogService operationActionLogService;
	private AddressService addressService;
	private ApartmentService apartmentService;
	private PersonService personService;
	private QuittanceDetailsFinder quittanceDetailsFinder;
	private SPService spService;
	private ServiceProviderService serviceProviderService;
	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;
	private ServiceTypesMapper serviceTypesMapper;
	private TradingDay<Cashbox> cashboxTradingDayService;

	@NotNull
    @Override
	protected String doExecute() throws Exception {

		GetQuittanceDebtInfoRequest request = buildQuittanceRequest();
		SearchResponse searchResponse = quittanceDetailsFinder.findQuittance(request);

		if (searchResponse.isSuccess()) {
            GetQuittanceDebtInfoResponse response = (GetQuittanceDebtInfoResponse) searchResponse;
			quittanceInfos = response.getInfos();
			filterSubservices();
			filterNegativeSums();

			Long cashboxId = getCashboxId();

			Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
            if (!quittanceInfos.isEmpty()) {
                OperationActionLog actionLog = createActionLog(request, cashbox);
                if (actionLog != null) {
                    operationActionLogService.create(actionLog);
                }
                log.debug("Quittances found. Creating action log {}", actionLog);
            } else {
                log.debug("Quittances not found. Action log not created");
            }
			Long cashboxProcessId = cashbox.getTradingDayProcessInstanceId();

            if (cashboxProcessId == null || cashboxProcessId == 0) {
                log.debug("TradingDaySchedulingJob process id not found for cashbox with id = {}", cashboxId);
                addActionError(getText("payments.quittance.payment.payment_not_alowed_due_closed_trading_day"));
            } else {
                log.debug("Found process id {} for cashbox {}", cashboxProcessId, cashboxId);
                if (!cashboxTradingDayService.isOpened(cashboxProcessId)) {
                    addActionError(getText("payments.quittance.payment.payment_not_alowed_due_closed_trading_day"));
                }
            }

		} else {
			addActionError(getStatusText(searchResponse.getStatus()));
		}

		return SUCCESS;
	}

    private OperationActionLog createActionLog(GetQuittanceDebtInfoRequest request, Cashbox cashbox) throws Exception {
        OperationActionLog actionLog = new OperationActionLog();

        actionLog.setCashbox(cashbox);
        actionLog.setUserName(getUserPreferences().getUsername());

        if (request.getSearchType() == SearchRequest.TYPE_ACCOUNT_NUMBER) {
            actionLog.setAction(OperationActionLog.SEARCH_BY_EIRC_ACCOUNT);
            actionLog.setActionString(request.getSearchCriteria());
        } else if (request.getSearchType() == SearchRequest.TYPE_QUITTANCE_NUMBER) {
            actionLog.setAction(OperationActionLog.SEARCH_BY_QUITTANCE_NUMBER);
            actionLog.setActionString(request.getSearchCriteria());
        } else if (request.getSearchType() == SearchRequest.TYPE_APARTMENT_NUMBER) {
            actionLog.setAction(OperationActionLog.SEARCH_BY_ADDRESS);
            actionLog.setActionString(addressService.getAddress(new Stub<Apartment>(Long.parseLong(searchCriteria)), getLocale()));
        } else {
            log.debug("Unknown operation search type! Can't create log record");
            return null;
        }

        return actionLog;
    }

	private GetQuittanceDebtInfoRequest buildQuittanceRequest() throws FlexPayException {

        GetQuittanceDebtInfoRequest request = new GetQuittanceDebtInfoRequest();

        request.setLocale(getUserPreferences().getLocale());

		if (SEARCH_TYPE_EIRC_ACCOUNT.equals(searchType)) {
            request.setSearchCriteria(searchCriteria);
            request.setSearchType(SearchRequest.TYPE_ACCOUNT_NUMBER);
		} else if (SEARCH_TYPE_QUITTANCE_NUMBER.equals(searchType)) {
            request.setSearchCriteria(searchCriteria);
            request.setSearchType(SearchRequest.TYPE_QUITTANCE_NUMBER);
		} else if (SEARCH_TYPE_ADDRESS.equals(searchType)) {
            Apartment apartment = null;
            try {
                apartment = apartmentService.readFull(
                        new Stub<Apartment>(Long.parseLong(searchCriteria)));
            } catch (Exception ex) {
                log.error("Exception in time apartment reading", ex);
            }
            if (apartment == null && apartmentNumber != null) {
                Long addressId;
                try {
                    addressId = Long.parseLong(parentSearchCriteria);
                } catch (Exception e) {
                    log.warn("Incorrect building address id in filter ({})", parentSearchCriteria);
                    throw new FlexPayException("Incorrect building address id", "ab.error.building_address.incorrect_address_id");
                }
                apartment = apartmentService.findByParent(new Stub<BuildingAddress>(addressId), apartmentNumber);
            }
            if (apartment == null) {
                throw new FlexPayException("Apartment did not find by seartch criteria", "payments.quittance.payment.appartment_did_not_find");
            }
            searchCriteria = apartment.getId().toString();
			String indx = masterIndexService.getMasterIndex(apartment);
			if (indx == null) {
				throw new FlexPayException("No master index for apartment #" + searchCriteria);
			}
            request.setSearchCriteria(searchCriteria);
            request.setSearchType(SearchRequest.TYPE_APARTMENT_NUMBER);
		} else {
			throw new FlexPayException("Bad search request: type must be one of: " + SEARCH_TYPE_ADDRESS + ", "
									   + SEARCH_TYPE_EIRC_ACCOUNT + ", " + SEARCH_TYPE_QUITTANCE_NUMBER);
		}

        return request;
	}

	// eliminates services with non-positive outgoing balances
	private void filterNegativeSums() {

		List<QuittanceInfo> filteredInfos = list();
		for (QuittanceInfo info : quittanceInfos) {
			BigDecimal total = new BigDecimal("0.00");

			List<ServiceDetails> filteredDetails = list();
			for (ServiceDetails sd : info.getServiceDetailses()) {
				if (sd.getOutgoingBalance().compareTo(BigDecimal.ZERO) > 0) {
					filteredDetails.add(sd);
					total = total.add(sd.getOutgoingBalance());
				} else {
					sd.setOutgoingBalance(new BigDecimal("0.00"));
					filteredDetails.add(sd);
				}
			}

			if (!filteredDetails.isEmpty()) {
				info.setServiceDetailses(filteredDetails);
				info.setTotalToPay(total);
				filteredInfos.add(info);
			}
		}

		quittanceInfos = filteredInfos;
	}

	// eliminates subservices information from quittances info
	private void filterSubservices() {

		for (QuittanceInfo quittanceInfo : quittanceInfos) {
			List<ServiceDetails> filtered = list();
			BigDecimal totalToPay = new BigDecimal("0.00");

			for (ServiceDetails details : quittanceInfo.getServiceDetailses()) {
				if (isNotSubservice(details.getServiceMasterIndex())) {
					filtered.add(details);
					totalToPay = totalToPay.add(details.getOutgoingBalance());
				} else {
					log.info("Service '{}' (masterindex) filtered out", details.getServiceMasterIndex());
				}
			}

			quittanceInfo.setServiceDetailses(filtered);
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

	private String getStatusText(Status status) {
        return getText(status.getTextKey());
	}

	@NotNull
    @Override
	protected String getErrorResult() {
		return SUCCESS;
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

	public String getErcAccount(List<ServiceDetails.ServiceAttribute> attributes) {

		for (ServiceDetails.ServiceAttribute attribute : attributes) {
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
			for (ServiceDetails details : info.getServiceDetailses()) {
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

    public void setParentSearchCriteria(String parentSearchCriteria) {
        this.parentSearchCriteria = parentSearchCriteria;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public List<QuittanceInfo> getQuittanceInfos() {
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

	@Required
	public void setCashboxTradingDayService(TradingDay<Cashbox> cashboxTradingDayService) {
		this.cashboxTradingDayService = cashboxTradingDayService;
	}

    @Required
    public void setOperationActionLogService(OperationActionLogService operationActionLogService) {
        this.operationActionLogService = operationActionLogService;
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
