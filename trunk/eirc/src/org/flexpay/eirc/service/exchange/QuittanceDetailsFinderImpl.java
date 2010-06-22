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
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.Security;
import org.flexpay.payments.actions.request.data.request.InfoRequest;
import org.flexpay.payments.actions.request.data.request.RequestType;
import org.flexpay.payments.actions.request.data.response.QuittanceDetailsResponse;
import org.flexpay.payments.actions.request.data.response.Status;
import org.flexpay.payments.actions.request.data.response.data.QuittanceInfo;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.actions.request.data.request.InfoRequest.*;

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
	public QuittanceDetailsResponse findQuittance(InfoRequest request) {

		Security.authenticateQuittanceFinder();

		log.debug("Request for quittance details recieved: {}", request);

        String requestStr = request.getRequest();

		QuittanceDetailsResponse response;
        Locale locale = request.getLocale();
        RequestType type = request.getDebtInfoType();
		switch (request.getType()) {
            case TYPE_ACCOUNT_NUMBER:
                response = findByAccountNumber(requestStr, type, locale);
                break;
			case TYPE_QUITTANCE_NUMBER:
				response = findByQuittanceNumber(requestStr, type, locale);
				break;
			case TYPE_APARTMENT_NUMBER:
				response = findByApartment(requestStr, type, locale);
				break;
            case TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER:
                response = findByServiceProviderAccountNumber(requestStr, type, locale);
                break;
            case TYPE_ADDRESS:
                response = findByAddress(requestStr, type, locale);
                break;
            case TYPE_COMBINED:
                response = findByCombined(requestStr, type, locale);
                break;
			default:
				response = getError(Status.UNKNOWN_REQUEST);
		}
		response.setRequestId(request.getRequestId());

		log.debug("Response to return: {}", response);

		return response;
	}

    private QuittanceDetailsResponse findByAccountNumber(@NotNull String request, RequestType requestType, Locale locale) {

        try {

            String[] reqMas = request.split(":");
            EircAccount account = accountService.findAccount(reqMas.length > 1 ? reqMas[1] : reqMas[0]);
            if (account == null) {
                return getError(Status.ACCOUNT_NOT_FOUND);
            }
            List<Quittance> quittances;
            if (reqMas.length > 1) {
                log.debug("Find quittances by account ({}) and serviceType ({})", account, reqMas[0]);
                try {
                    quittances = quittanceService.getLatestAccountQuittances(account, getServiceTypeStub(reqMas[0]));
                } catch (FlexPayException e) {
                    return getError(Status.SERVICE_NOT_FOUND);
                }
            } else {
                log.debug("Find quittances by account ({})", account);
                quittances = quittanceService.getLatestAccountQuittances(account);
            }
            if (quittances.isEmpty()) {
                log.debug("Quittances not found");
                return getError(Status.QUITTANCE_NOT_FOUND);
            }

            if (log.isDebugEnabled()) {
                log.debug("Found {} quittances", quittances.size());
            }
            
            return buildResponse(quittances, requestType, locale);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(Status.INTERNAL_ERROR);
        }

    }

    private QuittanceDetailsResponse findByQuittanceNumber(@NotNull String request, RequestType requestType, Locale locale) {

        if (isBlank(request)) {
            return getError(Status.INVALID_QUITTANCE_NUMBER);
        }

        try {

            String[] reqMas = request.split(":");
            Quittance quittance;
            if (reqMas.length > 1) {
                try {
                    log.debug("Find quittances by quittance number ({}) and serviceType ({})", reqMas[1], reqMas[0]);
                    quittance = quittanceService.findByNumberAndServiceType(reqMas[1], getServiceTypeStub(reqMas[0]));
                } catch (FlexPayException e) {
                    return getError(Status.SERVICE_NOT_FOUND);
                }
            } else {
                log.debug("Find quittances by quittance number ({})", reqMas[0]);
                quittance = quittanceService.findByNumber(reqMas[0]);
            }
            if (quittance == null) {
                log.debug("Quittances not found");
                return getError(Status.QUITTANCE_NOT_FOUND);
            } else {
                log.debug("Quittance found");
            }

            return buildResponse(list(quittance), requestType, locale);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(Status.INTERNAL_ERROR);
        }
    }

	private QuittanceDetailsResponse findByApartment(@NotNull String request, RequestType requestType, Locale locale) {

        String[] reqMas = request.split(":");
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
                return getError(Status.APARTMENT_NOT_FOUND);
            }

            ApartmentFilter filter = new ApartmentFilter(stub.getId());
            List<EircAccount> accounts = accountService.findAccounts(arrayStack(filter), new Page<EircAccount>(1000));
            if (accounts.isEmpty()) {
                return getError(Status.ACCOUNT_NOT_FOUND);
            }

            List<Quittance> quittances;
            if (reqMas.length > 1) {
                log.debug("Find quittances by accounts ({}) and serviceType ({})", accounts, reqMas[0]);
                try {
                    quittances = quittanceService.getLatestAccountsQuittances(accounts, getServiceTypeStub(reqMas[0]));
                } catch (FlexPayException e) {
                    return getError(Status.SERVICE_NOT_FOUND);
                }
            } else {
                log.debug("Find quittances by accounts ({})", accounts);
                quittances = quittanceService.getLatestAccountsQuittances(accounts);
            }
            if (quittances == null || quittances.isEmpty()) {
                log.debug("Quittances not found");
                return getError(Status.QUITTANCE_NOT_FOUND);
            }

            if (log.isDebugEnabled()) {
                log.debug("Found {} quittances", quittances.size());
            }

            return buildResponse(quittances, requestType, locale);

        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(Status.INTERNAL_ERROR);
        }
	}

    private QuittanceDetailsResponse findByServiceProviderAccountNumber(@NotNull String request, RequestType requestType, Locale locale) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request, TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER);
        } catch (FlexPayException e) {
            return getError(Status.SERVICE_NOT_FOUND);
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            return getError(Status.ACCOUNT_NOT_FOUND);
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittances(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            return getError(Status.QUITTANCE_NOT_FOUND);
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        return buildResponse(quittances, requestType, locale);
    }

    private QuittanceDetailsResponse findByAddress(@NotNull String request, RequestType requestType, Locale locale) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request, TYPE_ADDRESS);
        } catch (FlexPayException e) {
            return getError(Status.SERVICE_NOT_FOUND);
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            return getError(Status.ACCOUNT_NOT_FOUND);
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittancesByApartments(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            return getError(Status.QUITTANCE_NOT_FOUND);
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        return buildResponse(quittances, requestType, locale);
    }

    private QuittanceDetailsResponse findByCombined(@NotNull String request, RequestType requestType, Locale locale) {

        List<Consumer> consumers;
        try {
            consumers = getConsumers(request, TYPE_COMBINED);
        } catch (FlexPayException e) {
            return getError(Status.SERVICE_NOT_FOUND);
        }
        if (consumers == null) {
            log.debug("Consumers not found");
            return getError(Status.ACCOUNT_NOT_FOUND);
        }

        log.debug("Find quittances by consumers ({})", consumers);

        List<Quittance> quittances = quittanceService.getQuittancesByApartments(consumers);

        if (quittances.isEmpty()) {
            log.debug("Quittances not found");
            return getError(Status.QUITTANCE_NOT_FOUND);
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }

        return buildResponse(quittances, requestType, locale);
    }

    private List<Consumer> getConsumers(@NotNull String request, int searchType) throws FlexPayException {

        List<Consumer> consumers;

        String[] reqMas = request.split(":");
        if (reqMas.length > 1) {
            Stub<ServiceType> serviceTypeStub = getServiceTypeStub(reqMas[0]);
            String account = reqMas[1];
            if (searchType == TYPE_COMBINED) {
                consumers = consumerService.findConsumersByERCAccountAndServiceType(account, serviceTypeStub);
            } else if (searchType == TYPE_ADDRESS || searchType == TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER) {
                consumers = consumerService.findConsumersByExAccountAndServiceType(account, serviceTypeStub);
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

    private QuittanceDetailsResponse buildResponse(@NotNull List<Quittance> quittances, RequestType requestType, Locale locale) {

        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }
        if (quittances.isEmpty()) {
            return getError(Status.QUITTANCE_NOT_FOUND);
        }

        try {
            QuittanceDetailsResponse response = new QuittanceDetailsResponse();
            List<QuittanceInfo> infos = list();

            for (Quittance quittance : quittances) {
                infos.add(quittanceInfoBuilder.buildInfo(stub(quittance), requestType, locale));
            }
            response.setInfos(infos.toArray(new QuittanceInfo[infos.size()]));
            response.setStatus(Status.SUCCESS);
            return response;
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(Status.INTERNAL_ERROR);
        }

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

	private QuittanceDetailsResponse getError(Status status) {
		QuittanceDetailsResponse response = new QuittanceDetailsResponse();
		response.setStatus(status);
		return response;
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
