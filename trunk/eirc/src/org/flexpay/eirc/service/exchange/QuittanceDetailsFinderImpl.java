package org.flexpay.eirc.service.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.persistence.account.QuittanceDetailsQuittance;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.Security;
import org.flexpay.payments.action.outerrequest.request.GetDebtInfoRequest;
import org.flexpay.payments.action.outerrequest.request.GetQuittanceDebtInfoRequest;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.flexpay.payments.action.outerrequest.request.response.Status;
import org.flexpay.payments.action.outerrequest.request.response.data.QuittanceInfo;
import org.flexpay.payments.action.outerrequest.request.response.data.ServiceDetails;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.action.outerrequest.request.SearchRequest.*;

public class QuittanceDetailsFinderImpl implements QuittanceDetailsFinder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;
	private EircAccountService accountService;
    private ServiceTypeService serviceTypeService;
    private ConsumerService consumerService;
	private QuittanceService quittanceService;
	private QuittanceInfoBuilder quittanceInfoBuilder;

	/**
	 * Find quittance details
	 *
	 * @param request Request for quittance details
	 * @return Details response
	 */
	@NotNull
    @Override
	public SearchResponse findQuittance(SearchRequest<?> request) {

		Security.authenticateQuittanceFinder();

		log.debug("Request for quittance details recieved: {}", request);

		switch (request.getSearchType()) {
            case TYPE_ACCOUNT_NUMBER:
                findByAccountNumber(request);
                break;
			case TYPE_QUITTANCE_NUMBER:
				findByQuittanceNumber(request);
				break;
			case TYPE_APARTMENT_NUMBER:
				findByApartment(request);
				break;
            case TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER:
                findByServiceProviderAccountNumber(request);
                break;
            case TYPE_ADDRESS:
                findByAddress(request);
                break;
            case TYPE_COMBINED:
                findByCombined(request);
                break;
            case TYPE_ERC_KVP_NUMBER:
                findByERCAccountNumber(request);
                break;
            case TYPE_ERC_KVP_ADDRESS:
                findByERCAccountAddress(request);
                break;
			default:
                request.getResponse().setStatus(Status.UNKNOWN_REQUEST);
		}

        request.getResponse().setJmsRequestId(request.getJmsRequestId());

		log.debug("Response to return: {}", request.getResponse());

		return request.getResponse();
	}

    private void findByAccountNumber(SearchRequest<?> request) {

        try {

            String[] reqMas = request.getSearchCriteria().split(":");
            EircAccount account = accountService.findAccount(reqMas.length > 1 ? reqMas[1] : reqMas[0]);
            if (account == null) {
                request.getResponse().setStatus(Status.ACCOUNT_NOT_FOUND);
                return;
            }
            List<Quittance> quittances;
            if (reqMas.length > 1) {
                log.debug("Find quittances by account ({}) and serviceType ({})", account, reqMas[0]);
                try {
                    quittances = quittanceService.getLatestAccountQuittances(account, getServiceTypeStub(reqMas[0]));
                } catch (FlexPayException e) {
                    request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
                    return;
                }
            } else {
                log.debug("Find quittances by account ({})", account);
                quittances = quittanceService.getLatestAccountQuittances(account);
            }
            if (quittances.isEmpty()) {
                log.debug("Quittances not found");
                request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("Found {} quittances", quittances.size());
            }
            
            buildResponse(quittances, request);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            request.getResponse().setStatus(Status.INTERNAL_ERROR);
        }

    }

    private void findByQuittanceNumber(SearchRequest<?> request) {

        if (isBlank(request.getSearchCriteria())) {
            request.getResponse().setStatus(Status.INVALID_QUITTANCE_NUMBER);
            return;
        }

        try {

            String[] reqMas = request.getSearchCriteria().split(":");
            Quittance quittance;
            if (reqMas.length > 1) {
                try {
                    log.debug("Find quittances by quittance number ({}) and serviceType ({})", reqMas[1], reqMas[0]);
                    quittance = quittanceService.findByNumberAndServiceType(reqMas[1], getServiceTypeStub(reqMas[0]));
                } catch (FlexPayException e) {
                    request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
                    return;
                }
            } else {
                log.debug("Find quittances by quittance number ({})", reqMas[0]);
                quittance = quittanceService.findByNumber(reqMas[0]);
            }
            if (quittance == null) {
                log.debug("Quittances not found");
                request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
                return;
            } else {
                log.debug("Quittance found");
            }

            buildResponse(list(quittance), request);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            request.getResponse().setStatus(Status.INTERNAL_ERROR);
        }
    }

	private void findByApartment(SearchRequest<?> request) {

        String[] reqMas = request.getSearchCriteria().split(":");
        String apartmentMasterIndex = reqMas.length > 1 ? reqMas[1] : reqMas[0];
        try {
            Stub<Apartment> stub = correctionsService.findCorrection(
                    apartmentMasterIndex, Apartment.class, masterIndexService.getMasterSourceDescriptionStub());
            if (stub == null) {
                // todo remove this hack
                try {
                    Long stubId = Long.parseLong(apartmentMasterIndex, 10);
                    if (apartmentMasterIndex.equals(stubId.toString()) && stubId > 0) {
                        stub = new Stub<Apartment>(stubId);
                    }
                } catch (NumberFormatException ex) {
                    log.debug("Master index is looking like a real master index, but not found");
                }
            }
            if (stub == null) {
                log.debug("No apartment found by master index {}", apartmentMasterIndex);
                request.getResponse().setStatus(Status.APARTMENT_NOT_FOUND);
                return;
            }

            ApartmentFilter filter = new ApartmentFilter(stub.getId());
            List<EircAccount> accounts = accountService.findAccounts(arrayStack(filter), new Page<EircAccount>(1000));
            if (accounts.isEmpty()) {
                log.debug("Account not found for apartment with id {}", stub.getId());
                request.getResponse().setStatus(Status.ACCOUNT_NOT_FOUND);
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("Found accounts: {}", accounts.size());
            }

            List<Quittance> quittances;
            if (reqMas.length > 1) {
                log.debug("Find quittances by accounts ({}) and serviceType ({})", accounts, reqMas[0]);
                try {
                    quittances = quittanceService.getLatestAccountsQuittances(accounts, getServiceTypeStub(reqMas[0]));
                } catch (FlexPayException e) {
                    request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
                    return;
                }
            } else {
                log.debug("Find quittances by accounts ({})", accounts);
                quittances = quittanceService.getLatestAccountsQuittances(accounts);
            }
            if (quittances == null || quittances.isEmpty()) {
                log.debug("Quittances not found");
                request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("Found {} quittances", quittances.size());
            }

            buildResponse(quittances, request);

        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            request.getResponse().setStatus(Status.INTERNAL_ERROR);
        }
	}

    private void findByServiceProviderAccountNumber(SearchRequest<?> request) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request);
        } catch (FlexPayException e) {
            request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
            return;
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            request.getResponse().setStatus(Status.ACCOUNT_NOT_FOUND);
            return;
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittances(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        buildResponse(quittances, request);
    }

    private void findByAddress(SearchRequest<?> request) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request);
        } catch (FlexPayException e) {
            request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
            return;
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            request.getResponse().setStatus(Status.ACCOUNT_NOT_FOUND);
            return;
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittancesByApartments(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        buildResponse(quittances, request);
    }

    private void findByCombined(SearchRequest<?> request) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request);
        } catch (FlexPayException e) {
            request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
            return;
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            request.getResponse().setStatus(Status.ACCOUNT_NOT_FOUND);
            return;
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittancesByApartments(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        buildResponse(quittances, request);
    }

    private void findByERCAccountNumber(SearchRequest<?> request) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request);
        } catch (FlexPayException e) {
            request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
            return;
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            request.getResponse().setStatus(Status.ACCOUNT_NOT_FOUND);
            return;
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittances(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        buildFilteredByServiceResponse(quittances, request, 1);
    }

    private void findByERCAccountAddress(SearchRequest<?> request) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request);
        } catch (FlexPayException e) {
            request.getResponse().setStatus(Status.SERVICE_NOT_FOUND);
            return;
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            request.getResponse().setStatus(Status.ACCOUNT_NOT_FOUND);
            return;
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittancesByApartments(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        buildResponse(quittances, request);
    }

    private List<Consumer> getConsumers(SearchRequest<?> request) throws FlexPayException {

        List<Consumer> consumers;

        String[] reqMas = request.getSearchCriteria().split(":");
        Integer searchType = request.getSearchType();
        if (reqMas.length > 1) {
            Stub<ServiceType> serviceTypeStub = getServiceTypeStub(reqMas[0]);
            String account = reqMas[1];
            if (searchType == TYPE_COMBINED) {
                consumers = consumerService.findConsumersByERCAccountAndService(account, serviceTypeStub);
            } else if (searchType == TYPE_ADDRESS || searchType == TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER) {
                consumers = consumerService.findConsumersByExAccountAndService(account, serviceTypeStub);
            } else {
                log.debug("Incorrect searchType");
                return null;
            }
        } else {
            String account = reqMas[0];
            if (searchType == TYPE_COMBINED) {
                consumers = consumerService.findConsumersByERCAccount(account);
            } else if (searchType == TYPE_ADDRESS || searchType == TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER) {
                consumers = consumerService.findConsumersByExAccount(account);
            } else if (searchType == TYPE_ERC_KVP_NUMBER || searchType == TYPE_ERC_KVP_ADDRESS) {
                // TODO There is hardcoded the service with id=1 by the client request
                Stub<ServiceType> serviceTypeStub = getServiceTypeStub("1");
                consumers = consumerService.findConsumersByERCAccountAndService(account, serviceTypeStub);
            } else {
                log.debug("Incorrect searchType");
                return null;
            }
        }
        if (consumers == null || consumers.isEmpty()) {
            log.debug("Consumers not found");
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} consumers", consumers.size());
        }

        return consumers;

    }

    private void buildResponse(@NotNull List<Quittance> quittances, SearchRequest<?> request) {

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }
        if (quittances.isEmpty()) {
            request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
            return;
        }

        try {

            if (request instanceof GetQuittanceDebtInfoRequest) {
                GetQuittanceDebtInfoRequest getQuittanceDebtInfoRequest = (GetQuittanceDebtInfoRequest) request;
                for (Quittance quittance : quittances) {
                    getQuittanceDebtInfoRequest.getResponse().addQuiitanceInfo(quittanceInfoBuilder.buildInfo(stub(quittance), getQuittanceDebtInfoRequest));
                }
            } else {
                GetDebtInfoRequest getDebtInfoRequest = (GetDebtInfoRequest) request;
                for (Quittance quittance : quittances) {
                    getDebtInfoRequest.getResponse().addAllServiceDetails(quittanceInfoBuilder.buildServiceDetails(stub(quittance), getDebtInfoRequest));
                }
            }

        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            request.getResponse().setStatus(Status.INTERNAL_ERROR);
        }

    }

    private void buildFilteredByServiceResponse(@NotNull List<Quittance> quittances, SearchRequest<?> request, long serviceId) {

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }
        if (quittances.isEmpty()) {
            request.getResponse().setStatus(Status.QUITTANCE_NOT_FOUND);
            return;
        }

        try {

            if (request instanceof GetQuittanceDebtInfoRequest) {
                GetQuittanceDebtInfoRequest getQuittanceDebtInfoRequest = (GetQuittanceDebtInfoRequest) request;
                for (Quittance quittance : quittances) {
                    QuittanceInfo qInfo = filterInfo(quittanceInfoBuilder.buildInfo(stub(quittance), getQuittanceDebtInfoRequest), serviceId);
                    if (qInfo != null) {
                        getQuittanceDebtInfoRequest.getResponse().addQuiitanceInfo( qInfo );
                    }
                }
            } else {
                GetDebtInfoRequest getDebtInfoRequest = (GetDebtInfoRequest) request;
                for (Quittance quittance : quittances) {
                    List<ServiceDetails> sdList = filterDetails(quittanceInfoBuilder.buildServiceDetails(stub(quittance), getDebtInfoRequest), serviceId);
                    if (sdList != null && sdList.size() > 0) {
                        getDebtInfoRequest.getResponse().addAllServiceDetails(sdList);
                    }
                }
            }

        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            request.getResponse().setStatus(Status.INTERNAL_ERROR);
        }

    }

    private QuittanceInfo filterInfo(QuittanceInfo quittanceInfo, long serviceId) {
        List<ServiceDetails> filteredDetails = new ArrayList<ServiceDetails>(quittanceInfo.getServiceDetailses().size()) ;
        for( ServiceDetails sDetail : quittanceInfo.getServiceDetailses()) {
            if( sDetail.getServiceId() == serviceId ) {
                filteredDetails.add(sDetail);
            }
        }
        if( filteredDetails.size() > 0) {
            quittanceInfo.setServiceDetailses( filteredDetails );
            return quittanceInfo;
        }
        else {
            return null;
        }

    }

    private List<ServiceDetails> filterDetails(List<ServiceDetails> sdList, long serviceId) {
        List<ServiceDetails> filteredDetails = new ArrayList<ServiceDetails>(sdList.size()) ;
        for( ServiceDetails sDetail : sdList) {
            if( sDetail.getServiceId() == serviceId ) {
                filteredDetails.add(sDetail);
            }
        }

        return filteredDetails;
    }

    private Stub<ServiceType> getServiceTypeStub(String serviceTypeIdStr) throws FlexPayException {

        Stub<ServiceType> serviceTypeStub = new Stub<ServiceType>(Long.parseLong(serviceTypeIdStr));
        ServiceType serviceType = serviceTypeService.read(serviceTypeStub);
        if (serviceType == null) {
            log.warn("Can't get service type with id {} from DB", serviceTypeStub.getId());
            throw new FlexPayException("Can't get service type with id " + serviceTypeStub.getId() + " from DB");
        }
        return serviceTypeStub;
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
	public void setAccountService(EircAccountService accountService) {
		this.accountService = accountService;
	}

    @Required
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	@Required
	public void setQuittanceInfoBuilder(QuittanceInfoBuilder quittanceInfoBuilder) {
		this.quittanceInfoBuilder = quittanceInfoBuilder;
	}

    @Required
    public void setConsumerService(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }
}
