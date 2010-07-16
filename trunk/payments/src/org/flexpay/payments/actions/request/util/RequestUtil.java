package org.flexpay.payments.actions.request.util;

import org.apache.commons.codec.binary.Hex;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.payments.actions.request.data.request.*;
import org.flexpay.payments.actions.request.data.request.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.flexpay.payments.actions.request.data.request.InfoRequest.*;

public class RequestUtil {

    private final static Logger log = LoggerFactory.getLogger(RequestUtil.class);

    public static boolean authenticate(DebtsRequest request) throws FlexPayException {

        String login = request.getLogin();
        RequestType reqType = request.getRequestType();

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

            if (reqType == RequestType.SEARCH_DEBT_REQUEST || reqType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
                DebtInfo debtInfo = request.getDebtInfo();

                signature.update(debtInfo.getRequestId().getBytes());
                signature.update(debtInfo.getSearchType().toString().getBytes());
                signature.update(debtInfo.getSearchCriteria().getBytes());

            } else if (reqType == RequestType.PAY_DEBT_REQUEST) {
                PayDebt payDebt = request.getPayDebt();

                signature.update(payDebt.getRequestId().getBytes());
                signature.update(payDebt.getTotalPaySum().getBytes());

                for (ServicePayDetails payDetails : payDebt.getServicePayDetails()) {
                    signature.update(payDetails.getServiceId().getBytes());
                    signature.update(payDetails.getServiceProviderAccount().getBytes());
                    signature.update(payDetails.getPaySum().getBytes());
                }

            } else if (reqType == RequestType.REVERSAL_PAY_REQUEST) {
                ReversalPay reversalPay = request.getReversalPay();

                signature.update(reversalPay.getRequestId().getBytes());
                signature.update(reversalPay.getOperationId().toString().getBytes());
                signature.update(reversalPay.getTotalPaySum().getBytes());
            } else if (reqType == RequestType.REGISTRY_COMMENT_REQUEST) {
                RegistryComment registryComment = request.getRegistryComment();

                log.debug("registryComment = {}", registryComment);

                signature.update(registryComment.getRequestId().getBytes());
                signature.update(registryComment.getRegistryId().toString().getBytes());
                signature.update(registryComment.getOrderNumber().getBytes());
                signature.update(registryComment.getOrderDate().getBytes());
                signature.update(registryComment.getOrderComment().getBytes("utf8"));
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
        RequestType reqType = request.getRequestType();

        log.debug("reqType = {}", reqType);

        if (reqType == RequestType.SEARCH_DEBT_REQUEST || reqType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
            int searchType = request.getDebtInfo().getSearchType();
            log.debug("searchType = {}", searchType);

            return searchType == TYPE_ACCOUNT_NUMBER
                    || searchType == TYPE_QUITTANCE_NUMBER
                    || searchType == TYPE_APARTMENT_NUMBER
                    || searchType == TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER
                    || searchType == TYPE_ADDRESS
                    || searchType == TYPE_COMBINED;
        } else if (reqType == RequestType.PAY_DEBT_REQUEST) {
            BigDecimal sum = new BigDecimal("0.00");
            for (ServicePayDetails spd : request.getPayDebt().getServicePayDetails()) {
                sum = sum.add(new BigDecimal(spd.getPaySum()));
            }
            log.debug("sum = {}, totalPaySum = {}", sum.toString(), request.getPayDebt().getTotalPaySum());
            return sum.setScale(2).equals(new BigDecimal(request.getPayDebt().getTotalPaySum()).setScale(2));
        } else if (reqType == RequestType.REVERSAL_PAY_REQUEST) {
            log.debug("operationId = {}, totalPaySum = {}", request.getReversalPay().getOperationId(), request.getReversalPay().getTotalPaySum());
            return true;
        } else if (reqType == RequestType.REGISTRY_COMMENT_REQUEST) {
            //TODO
            //log.debug("operationId = {}, totalPaySum = {}", request.getReversalPay().getOperationId(), request.getReversalPay().getTotalPaySum());
            return true;
        } else {
            log.warn("Unknown type of request");
            return false;
        }
	}

	public static InfoRequest createSearchRequest(DebtsRequest request, Locale locale) {

        DebtInfo debtInfo = request.getDebtInfo();
		String searchCriteria = debtInfo.getSearchCriteria();
		int searchType = debtInfo.getSearchType();
        InfoRequest infoRequest;

		if (TYPE_ACCOUNT_NUMBER == searchType) {
			infoRequest = accountNumberRequest(searchCriteria, request.getRequestType(), locale);
		} else if (TYPE_QUITTANCE_NUMBER == searchType) {
			infoRequest = quittanceNumberRequest(searchCriteria, request.getRequestType(), locale);
		} else if (TYPE_APARTMENT_NUMBER == searchType) {
			infoRequest = apartmentNumberRequest(searchCriteria, request.getRequestType(), locale);
        } else if (TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER == searchType) {
            infoRequest = serviceProviderAccountNumberRequest(searchCriteria, request.getRequestType(), locale);
        } else if (TYPE_ADDRESS == searchType) {
            infoRequest = addressRequest(searchCriteria, request.getRequestType(), locale);
        } else if (TYPE_COMBINED == searchType) {
            infoRequest = combinedRequest(searchCriteria, request.getRequestType(), locale);
        } else {
            log.warn("Unknown search request type - {}", searchType);
            return null;
        }
        infoRequest.setRequestId(debtInfo.getRequestId());

		return infoRequest;
	}

    public static PayRequest createPayRequest(DebtsRequest request, Locale locale) throws FlexPayException {

        PayDebt payDebt = request.getPayDebt();

        PayRequest payRequest = new PayRequest();
        try {
            payRequest.setTotalToPay(new BigDecimal(payDebt.getTotalPaySum()).setScale(2));
        } catch (NumberFormatException e) {
            log.error("Can't parse totalPaySum {} to BigDecimal", payDebt.getTotalPaySum());
            throw new FlexPayException("Can't parse totalPaySum");
        }
        payRequest.setRequestId(payDebt.getRequestId());
        payRequest.setLocale(locale);

        for (ServicePayDetails payDetails : payDebt.getServicePayDetails()) {
            payRequest.addServicePayDetails(payDetails.getServiceId(), payDetails.getServiceProviderAccount(), payDetails.getPaySum());
        }

        return payRequest;
    }

    public static ReversalPayRequest createReversalPayRequest(DebtsRequest request) throws FlexPayException {

        ReversalPay reversalPay = request.getReversalPay();

        ReversalPayRequest reversalPayRequest = new ReversalPayRequest();
        try {
            reversalPayRequest.setTotalPaySum(new BigDecimal(reversalPay.getTotalPaySum()).setScale(2));
        } catch (NumberFormatException e) {
            log.error("Can't parse totalPaySum {} to BigDecimal", reversalPay.getTotalPaySum());
            throw new FlexPayException("Can't parse totalPaySum");
        }
        reversalPayRequest.setRequestId(reversalPay.getRequestId());
        reversalPayRequest.setOperationId(reversalPay.getOperationId());

        return reversalPayRequest;
    }

    public static RegistryCommentRequest createRegistryCommentRequest(DebtsRequest request) throws FlexPayException {

        RegistryComment registryComment = request.getRegistryComment();

        RegistryCommentRequest registryCommentRequest = new RegistryCommentRequest();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            registryCommentRequest.setOrderDate(format.parse(registryComment.getOrderDate()));
        } catch (ParseException e) {
            log.error("Can't parse orderDate {} to Date format", registryComment.getOrderDate());
            throw new FlexPayException("Can't parse orderDate");
        }
        registryCommentRequest.setRequestId(registryComment.getRequestId());
        registryCommentRequest.setRegistryId(registryComment.getRegistryId());
        registryCommentRequest.setOrderNumber(registryComment.getOrderNumber());
        registryCommentRequest.setOrderComment(registryComment.getOrderComment());

        return registryCommentRequest;
    }

}
