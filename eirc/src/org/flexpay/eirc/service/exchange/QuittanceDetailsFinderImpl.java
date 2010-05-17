package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.StringUtils;
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
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.quittance.InfoRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.persistence.quittance.QuittanceInfo;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

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
			case TYPE_QUITTANCE_NUMBER:
				response = findByQuittanceNumber(requestStr, request.getDebtInfoType());
				break;
			case TYPE_ACCOUNT_NUMBER:
				response = findByAccountNumber(requestStr, request.getDebtInfoType());
				break;
			case TYPE_APARTMENT_NUMBER:
				response = findByApartmentReference(requestStr, request.getDebtInfoType());
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

    private QuittanceDetailsResponse findByQuittanceNumber(String quittanceNumber, int requestType) {

        if (StringUtils.isBlank(quittanceNumber)) {
            return getError(STATUS_INVALID_QUITTANCE_NUMBER);
        }

        try {
            Quittance quittance = quittanceService.findByNumber(quittanceNumber);
            if (quittance == null) {
                return getError(STATUS_QUITTANCE_NOT_FOUND);
            }

            QuittanceInfo info = getInfo(quittance, requestType);

            QuittanceDetailsResponse response = new QuittanceDetailsResponse();
            response.setStatusCode(STATUS_SUCCESS);
            QuittanceInfo[] infos = {info};
            response.setInfos(infos);
            return response;
        } catch (FlexPayException ex) {
            return getError(STATUS_QUITTANCE_NOT_FOUND);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            return getError(STATUS_INTERNAL_ERROR);
        }
    }

	private QuittanceDetailsResponse findByApartmentReference(String apartmentMasterIndex, int requestType) {

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

		List<QuittanceInfo> infos = list();
		for (EircAccount account : accounts) {

			List<Quittance> quittances = quittanceService.getLatestAccountQuittances(stub(account), new Page<Quittance>());
			if (quittances.isEmpty()) {
				continue;
			}

			try {
				QuittanceInfo info = getInfo(quittances.get(0), requestType);
				infos.add(info);
			} catch (Exception ex) {
				log.error("Failed building quittance info", ex);
			}
		}
		if (infos.isEmpty()) {
			return getError(STATUS_QUITTANCE_NOT_FOUND);
		}

		QuittanceDetailsResponse response = new QuittanceDetailsResponse();
		response.setInfos(infos.toArray(new QuittanceInfo[infos.size()]));
		response.setStatusCode(STATUS_SUCCESS);
		return response;
	}

	private QuittanceDetailsResponse findByAccountNumber(String accountNumber, int requestType) {

		EircAccount account = accountService.findAccount(accountNumber);
		if (account == null) {
			return getError(STATUS_ACCOUNT_NOT_FOUND);
		}

		QuittanceDetailsResponse response = new QuittanceDetailsResponse();

		List<Quittance> quittances = quittanceService.getLatestAccountQuittances(stub(account), new Page<Quittance>());
		if (quittances.isEmpty()) {
			return getError(STATUS_QUITTANCE_NOT_FOUND);
		}

		try {
			QuittanceInfo info = getInfo(quittances.get(0), requestType);
			response.setInfos(new QuittanceInfo[] {info});
			response.setStatusCode(STATUS_SUCCESS);
			return response;
		} catch (Exception ex) {
			log.error("Failed building quittance info", ex);
			response = getError(STATUS_INTERNAL_ERROR);
		}

		return response;
	}

    private QuittanceDetailsResponse findByServiceProviderAccountNumber(@NotNull String request, int requestType) {

        List<Consumer> consumers = getConsumers(request);
        if (consumers == null) {
            return getError(STATUS_ACCOUNT_NOT_FOUND);
        }

        List<Quittance> quittances = quittanceService.getQuittancesByEircAccounts(consumers);
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

        List<Consumer> consumers = list();

        String[] reqMas = request.split(":");
        if (reqMas.length > 1) {
            try {
                Consumer consumer = consumerService.findConsumerByERCAccountAndService(reqMas[1], new Stub<Service>(Long.parseLong(reqMas[0])));
                if (consumer == null) {
                    log.debug("Consumers not found");
                    return null;
                }
                consumers.add(consumer);
            } catch (NumberFormatException e) {
                log.debug("Incorrect numebr in request");
                return null;
            }
        } else {
            consumers = consumerService.findConsumersByERCAccount(reqMas[0]);
            if (consumers.isEmpty()) {
                log.debug("Consumers not found");
                return null;
            }
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
                infos.add(getInfo(quittance, requestType));
            }
            response.setInfos(infos.toArray(new QuittanceInfo[infos.size()]));
            response.setStatusCode(STATUS_SUCCESS);
        } catch (Exception ex) {
            log.error("Failed building quittance info", ex);
            response = getError(STATUS_INTERNAL_ERROR);
        }

        return response;

    }

	private QuittanceInfo getInfo(Quittance q, int requestType) throws Exception {
    	return quittanceInfoBuilder.buildInfo(stub(q), requestType);
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
