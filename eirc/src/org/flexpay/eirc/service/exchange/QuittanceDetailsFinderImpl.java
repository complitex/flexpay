package org.flexpay.eirc.service.exchange;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.Security;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest.*;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse.*;

public class QuittanceDetailsFinderImpl implements QuittanceDetailsFinder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;
	private EircAccountService accountService;
	private QuittanceService quittanceService;
	private QuittanceInfoBuilder quittanceInfoBuilder;

	/**
	 * Find quittance details
	 *
	 * @param request Request for quittance details
	 * @return Details response
	 */
	@NotNull
	public QuittanceDetailsResponse findQuittance(QuittanceDetailsRequest request) {

		Security.authenticateQuittanceFinder();

		log.debug("Request for quittance details recieved: {}", request);

		QuittanceDetailsResponse response;
		switch (request.getType()) {
			case TYPE_QUITTANCE_NUMBER:
				response = findByQuittanceNumber(request.getRequest());
				break;
			case TYPE_ACCOUNT_NUMBER:
				response = findByAccountNumber(request.getRequest());
				break;
			case TYPE_APARTMENT_NUMBER:
				response = findByApartmentReference(request.getRequest());
				break;
			default:
				response = getError(CODE_ERROR_UNKNOWN_REQUEST);
		}
		response.setRequestId(request.getRequestId());

		log.debug("Response to return: {}", response);

		return response;
	}

	private QuittanceDetailsResponse findByApartmentReference(String apartmentMasterIndex) {

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
			return getError(CODE_ERROR_APARTMENT_NOT_FOUND);
		}

		ApartmentFilter filter = new ApartmentFilter(stub.getId());
		ArrayStack filters = CollectionUtils.arrayStack(filter);
		Page<EircAccount> pager = new Page<EircAccount>(1000);
		List<EircAccount> accounts = accountService.findAccounts(filters, pager);
		if (accounts.isEmpty()) {
			return getError(CODE_ERROR_ACCOUNT_NOT_FOUND);
		}

		List<QuittanceInfo> infos = CollectionUtils.list();
		for (EircAccount account : accounts) {

			List<Quittance> quittances = quittanceService
					.getLatestAccountQuittances(stub(account), new Page<Quittance>());
			if (quittances.isEmpty()) {
				continue;
			}

			try {
				QuittanceInfo info = getInfo(quittances.get(0));
				infos.add(info);
			} catch (Exception ex) {
				log.error("Failed building quittance info", ex);
			}
		}
		if (infos.isEmpty()) {
			return getError(CODE_ERROR_QUITTANCE_NOT_FOUND);
		}

		QuittanceDetailsResponse response = new QuittanceDetailsResponse();
		response.setInfos(infos.toArray(new QuittanceInfo[infos.size()]));
		response.setErrorCode(CODE_SUCCESS);
		return response;
	}

	private QuittanceDetailsResponse findByAccountNumber(String accountNumber) {

		EircAccount account = accountService.findAccount(accountNumber);
		if (account == null) {
			return getError(CODE_ERROR_ACCOUNT_NOT_FOUND);
		}

		QuittanceDetailsResponse response = new QuittanceDetailsResponse();

		List<Quittance> quittances = quittanceService
				.getLatestAccountQuittances(stub(account), new Page<Quittance>());
		if (quittances.isEmpty()) {
			return getError(CODE_ERROR_QUITTANCE_NOT_FOUND);
		}

		try {
			QuittanceInfo info = getInfo(quittances.get(0));
			QuittanceInfo[] infos = {info};
			response.setInfos(infos);
			response.setErrorCode(CODE_SUCCESS);
			return response;
		} catch (Exception ex) {
			log.error("Failed building quittance info", ex);
			response = getError(CODE_ERROR_INTERNAL_ERROR);
		}

		return response;
	}

	private QuittanceDetailsResponse findByQuittanceNumber(String quittanceNumber) {

		if (StringUtils.isBlank(quittanceNumber)) {
			return getError(CODE_ERROR_INVALID_QUITTANCE_NUMBER);
		}

		Quittance quittance;
		try {
			quittance = quittanceService.findByNumber(quittanceNumber);
			if (quittance == null) {
				return getError(CODE_ERROR_QUITTANCE_NOT_FOUND);
			}

			QuittanceDetailsResponse.QuittanceInfo info = getInfo(quittance);

			QuittanceDetailsResponse response = new QuittanceDetailsResponse();
			response.setErrorCode(CODE_SUCCESS);
			QuittanceInfo[] infos = {info};
			response.setInfos(infos);
			return response;
		} catch (FlexPayException ex) {
			return getError(CODE_ERROR_QUITTANCE_NOT_FOUND);
		} catch (Exception ex) {
			log.error("Failed building quittance info", ex);
			return getError(CODE_ERROR_INTERNAL_ERROR);
		}
	}

	private QuittanceInfo getInfo(Quittance q) throws Exception {

		return quittanceInfoBuilder.buildInfo(stub(q));
	}

	private QuittanceDetailsResponse getError(int code) {

		QuittanceDetailsResponse response = new QuittanceDetailsResponse();
		response.setErrorCode(code);
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

	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	@Required
	public void setQuittanceInfoBuilder(QuittanceInfoBuilder quittanceInfoBuilder) {
		this.quittanceInfoBuilder = quittanceInfoBuilder;
	}
}
