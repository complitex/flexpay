package org.flexpay.payments.actions.request.util;

import org.apache.commons.codec.binary.Hex;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.payments.actions.request.data.DebtInfo;
import org.flexpay.payments.actions.request.data.DebtsRequest;
import org.flexpay.payments.actions.request.data.PayDebt;
import org.flexpay.payments.actions.request.data.ServicePayDetails;
import org.flexpay.payments.persistence.quittance.InfoRequest;
import org.flexpay.payments.persistence.quittance.PayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.Certificate;

import static org.flexpay.payments.actions.request.data.DebtsRequest.*;
import static org.flexpay.payments.persistence.quittance.InfoRequest.*;

public class RequestUtil {

    private final static Logger log = LoggerFactory.getLogger(RequestUtil.class);

    public static boolean authenticate(DebtsRequest request) throws FlexPayException {

        String login = request.getLogin();
        int reqType = request.getDebtRequestType();

        log.debug("login = {}, reqType = {}", login, reqType);

        Certificate certificate;
        try {
            KeyStore keyStore = KeyStoreUtil.loadKeyStore();

            if (!keyStore.isCertificateEntry(login)) {
                log.error("Can't load security certificate for user {}", login);
                return false;
            }

            certificate = keyStore.getCertificate(login);

        } catch (Exception e) {
            log.error("Error loading certificate from keystore", e);
            throw new FlexPayException("Error loading certificate from keystore", e);
        }

        try {
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initVerify(certificate);

            if (reqType == SEARCH_DEBT_REQUEST || reqType == SEARCH_QUITTANCE_DEBT_REQUEST) {
                DebtInfo debtInfo = request.getDebtInfo();

                signature.update(debtInfo.getRequestId().getBytes());
                signature.update(debtInfo.getSearchType().toString().getBytes());
                signature.update(debtInfo.getSearchCriteria().getBytes());

            } else if (reqType == PAY_DEBT_REQUEST) {
                PayDebt payDebt = request.getPayDebt();

                signature.update(payDebt.getRequestId().getBytes());
                signature.update(payDebt.getTotalPaySum().getBytes());

                for (ServicePayDetails payDetails : payDebt.getServicePayDetails()) {
                    signature.update(payDetails.getServiceId().getBytes());
                    signature.update(payDetails.getServiceProviderAccount().getBytes());
                    signature.update(payDetails.getPaySum().getBytes());
                }

            } else {
                log.warn("Unknown request type");
                throw new FlexPayException("Unknown request");
            }

            if (!signature.verify(Hex.decodeHex(request.getSignature().toCharArray()))) {
                log.error("Request digital signature is bad");
                return false;
            }
        } catch (Exception e) {
            log.error("Error checking request digital signature", e);
            throw new FlexPayException("Error checking request digital signature", e);
        }

        return true;

    }

	public static boolean validate(DebtsRequest request) {
        int reqType = request.getDebtRequestType();

        log.debug("reqType = {}", reqType);

        if (reqType == SEARCH_DEBT_REQUEST || reqType == SEARCH_QUITTANCE_DEBT_REQUEST) {
            int searchType = request.getDebtInfo().getSearchType();
            log.debug("searchType = {}", searchType);

            return searchType == TYPE_ACCOUNT_NUMBER
                    || searchType == TYPE_QUITTANCE_NUMBER
                    || searchType == TYPE_APARTMENT_NUMBER
                    || searchType == TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER
                    || searchType == TYPE_ADDRESS;
        } else if (reqType == PAY_DEBT_REQUEST) {
            BigDecimal sum = new BigDecimal(0);
            for (ServicePayDetails spd : request.getPayDebt().getServicePayDetails()) {
                sum = sum.add(new BigDecimal(spd.getPaySum()));
            }
            log.debug("sum = {}, totalPaySum = {}", sum.toString(), request.getPayDebt().getTotalPaySum());
            return sum.equals(new BigDecimal(request.getPayDebt().getTotalPaySum()));
        } else {
            log.warn("Unknown type of request");
            return false;
        }
	}

	public static InfoRequest convertRequest(DebtsRequest request) {

		String searchCriteria = request.getDebtInfo().getSearchCriteria();
		int searchType = request.getDebtInfo().getSearchType();

		if (TYPE_ACCOUNT_NUMBER == searchType) {
			return accountNumberRequest(searchCriteria, request.getDebtRequestType());
		} else if (TYPE_QUITTANCE_NUMBER == searchType) {
			return quittanceNumberRequest(searchCriteria, request.getDebtRequestType());
		} else if (TYPE_APARTMENT_NUMBER == searchType) {
			return apartmentNumberRequest(searchCriteria, request.getDebtRequestType());
        } else if (TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER == searchType) {
            return serviceProviderAccountNumberRequest(searchCriteria, request.getDebtRequestType());
        } else if (TYPE_ADDRESS == searchType) {
            return addressRequest(searchCriteria, request.getDebtRequestType());
        }

		return null;
	}

    public static PayRequest createPayRequest(DebtsRequest request) throws FlexPayException {

        PayDebt payDebt = request.getPayDebt();

        PayRequest payRequest = new PayRequest();
        try {
            payRequest.setTotalToPay(new BigDecimal(payDebt.getTotalPaySum()));
        } catch (NumberFormatException e) {
            log.error("Can't parse totalPaySum {} to BigDecimal", payDebt.getTotalPaySum());
            throw new FlexPayException("Can't parse totalPaySum");
        }

        for (ServicePayDetails payDetails : payDebt.getServicePayDetails()) {
            payRequest.addServicePayDetails(payDetails.getServiceId(), payDetails.getServiceProviderAccount(), payDetails.getPaySum());
        }

        return payRequest;
    }

}
