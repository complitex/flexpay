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
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.quittance.InfoRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.persistence.quittance.QuittanceInfo;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.persistence.quittance.DetailsResponse.*;
import static org.flexpay.payments.persistence.quittance.InfoRequest.*;

public class QuittanceDetailsFinderImpl implements QuittanceDetailsFinder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;
	private EircAccountService accountService;
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
		switch (request.getType()) {
            case TYPE_ACCOUNT_NUMBER:
                response = findByAccountNumber(requestStr, request.getDebtInfoType());
                break;
			case TYPE_QUITTANCE_NUMBER:
				response = findByQuittanceNumber(requestStr, request.getDebtInfoType());
				break;
			case TYPE_APARTMENT_NUMBER:
				response = findByApartment(requestStr, request.getDebtInfoType());
				break;
            case TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER:
                response = findByServiceProviderAccountNumber(requestStr, request.getDebtInfoType());
                break;
            case TYPE_ADDRESS:
                response = findByAddress(requestStr, request.getDebtInfoType());
                break;
			default:
				response = getError(STATUS_UNKNOWN_REQUEST);
		}
		response.setRequestId(request.getRequestId());

		log.debug("Response to return: {}", response);

		return response;
	}

    private QuittanceDetailsResponse findByAccountNumber(@NotNull String request, int requestType) {

        try {

            String[] reqMas = request.split(":");
            EircAccount account = accountService.findAccount(reqMas.length > 1 ? reqMas[1] : reqMas[0]);
            if (account == null) {
                return getError(STATUS_ACCOUNT_NOT_FOUND);
            }
            List<Quittance> quittances;
            if (reqMas.length > 1) {
                try {
                    quittances = quittanceService.getLatestAccountQuittances(account, new Stub<ServiceType>(Long.parseLong(reqMas[0])), new Page<Quittance>(10000));
                } catch (NumberFormatException e) {
                    log.debug("Incorrect number in request");
                    throw new FlexPayException("Incorrect number in request");
                }
            } else {
                quittances = quittanceService.getLatestAccountQuittances(account, new Page<Quittance>(10000));
            }
            if (log.isDebugEnabled()) {
                log.debug("Found {} quittances", quittances.size());
            }
            if (quittances.isEmpty()) {
                return getError(STATUS_QUITTANCE_NOT_FOUND);
            }

            return buildResponse(quittances, requestType);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(STATUS_INTERNAL_ERROR);
        }

    }

    private QuittanceDetailsResponse findByQuittanceNumber(@NotNull String request, int requestType) {

        if (isBlank(request)) {
            return getError(STATUS_INVALID_QUITTANCE_NUMBER);
        }

        try {

            String[] reqMas = request.split(":");
            Quittance quittance;
            if (reqMas.length > 1) {
                try {
                    quittance = quittanceService.findByNumberAndServiceType(reqMas[1], new Stub<ServiceType>(Long.parseLong(reqMas[0])));
                } catch (NumberFormatException e) {
                    log.debug("Incorrect number in request");
                    throw new FlexPayException("Incorrect number in request");
                }
            } else {
                quittance = quittanceService.findByNumber(reqMas[0]);
            }
            if (quittance == null) {
                return getError(STATUS_QUITTANCE_NOT_FOUND);
            }

            return buildResponse(list(quittance), requestType);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(STATUS_INTERNAL_ERROR);
        }
    }

	private QuittanceDetailsResponse findByApartment(@NotNull String request, int requestType) {

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
                return getError(STATUS_APARTMENT_NOT_FOUND);
            }

            ApartmentFilter filter = new ApartmentFilter(stub.getId());
            List<EircAccount> accounts = accountService.findAccounts(arrayStack(filter), new Page<EircAccount>(1000));
            if (accounts.isEmpty()) {
                return getError(STATUS_ACCOUNT_NOT_FOUND);
            }

            List<Quittance> quittances;
            if (reqMas.length > 1) {
                try {
                    quittances = quittanceService.getLatestAccountsQuittances(accounts, new Stub<ServiceType>(Long.parseLong(reqMas[0])), new Page<Quittance>(10000));
                } catch (NumberFormatException e) {
                    log.debug("Incorrect number in request");
                    throw new FlexPayException("Incorrect number in request");
                }
            } else {
                quittances = quittanceService.getLatestAccountsQuittances(accounts, new Page<Quittance>(10000));
            }
            if (log.isDebugEnabled()) {
                log.debug("Found {} quittances", quittances.size());
            }
            if (quittances.isEmpty()) {
                return getError(STATUS_QUITTANCE_NOT_FOUND);
            }

            return buildResponse(quittances, requestType);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(STATUS_INTERNAL_ERROR);
        }
	}

    private QuittanceDetailsResponse findByServiceProviderAccountNumber(@NotNull String request, int requestType) {

        List<Consumer> consumers = getConsumers(request);
        if (consumers == null) {
            return getError(STATUS_ACCOUNT_NOT_FOUND);
        }

        List<Quittance> quittances = quittanceService.getQuittances(consumers);
        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }
        if (quittances.isEmpty()) {
            return getError(STATUS_QUITTANCE_NOT_FOUND);
        }

        return buildResponse(quittances, requestType);
    }

    private QuittanceDetailsResponse findByAddress(@NotNull String request, int requestType) {

        List<Consumer> consumers = getConsumers(request);
        if (consumers == null) {
            return getError(STATUS_ACCOUNT_NOT_FOUND);
        }

        List<Quittance> quittances = quittanceService.getQuittancesByApartments(consumers);
        if (log.isDebugEnabled()) {
            log.debug("Found {} quittances", quittances.size());
        }
        if (quittances.isEmpty()) {
            return getError(STATUS_QUITTANCE_NOT_FOUND);
        }

        return buildResponse(quittances, requestType);
    }

    private List<Consumer> getConsumers(@NotNull String request) {

        List<Consumer> consumers;

        String[] reqMas = request.split(":");
        if (reqMas.length > 1) {
            try {
                consumers = consumerService.findConsumersByExAccountAndServiceType(reqMas[1], new Stub<ServiceType>(Long.parseLong(reqMas[0])));
            } catch (NumberFormatException e) {
                log.debug("Incorrect number in request");
                return null;
            }
        } else {
            consumers = consumerService.findConsumersByExAccount(reqMas[0]);
        }
        if (consumers.isEmpty()) {
            log.debug("Consumers not found");
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} consumers", consumers.size());
        }

        return consumers;

    }

    private QuittanceDetailsResponse buildResponse(@NotNull List<Quittance> quittances, int requestType) {

        QuittanceDetailsResponse response = new QuittanceDetailsResponse();

        try {
            List<QuittanceInfo> infos = list();

            for (Quittance quittance : quittances) {
                infos.add(quittanceInfoBuilder.buildInfo(stub(quittance), requestType));
            }
            response.setInfos(infos.toArray(new QuittanceInfo[infos.size()]));
            response.setStatusCode(STATUS_SUCCESS);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            response = getError(STATUS_INTERNAL_ERROR);
        }

        return response;

    }

	private QuittanceDetailsResponse getError(int code) {
		QuittanceDetailsResponse response = new QuittanceDetailsResponse();
		response.setStatusCode(code);
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
