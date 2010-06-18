package org.flexpay.payments.actions.request.data.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Locale;

public class InfoRequest implements Serializable {

    private final static Logger log = LoggerFactory.getLogger(InfoRequest.class);

    /**
     * Quittance details should be searched by account number
     */
    public static final int TYPE_ACCOUNT_NUMBER = 1;
    /**
     * Quittance details should be searched by quittance number
     */
    public static final int TYPE_QUITTANCE_NUMBER = 2;
    /**
     * Quittance details should be searched by master index of apartment
     */
    public static final int TYPE_APARTMENT_NUMBER = 3;
    /**
     * Quittance details should be searched by service provider account number
     */
    public static final int TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER = 4;
    /**
     * Quittance details should be searched by address calculated by the personal account number of service provider
     */
    public static final int TYPE_ADDRESS = 5;
    /**
     * Quittance details should be search with number 4 or search with number 5
     */
    public static final int TYPE_COMBINED = 6;

    private String requestId;
    private String request;
    private Locale locale = Locale.ENGLISH;
    private int type;
    private RequestType debtInfoType = RequestType.SEARCH_QUITTANCE_DEBT_REQUEST;

    private InfoRequest(String request, int type, RequestType debtInfoType, Locale locale) {
        this.request = request;
        this.type = type;
        this.debtInfoType = debtInfoType;
        this.locale = locale;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RequestType getDebtInfoType() {
        return debtInfoType;
    }

    public void setDebtInfoType(RequestType debtInfoType) {
        this.debtInfoType = debtInfoType;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Create request from account number
     *
     * @param accountNumber Account number
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest accountNumberRequest(@NotNull String accountNumber, RequestType debtInfoType, Locale locale) {
        return new InfoRequest(accountNumber, TYPE_ACCOUNT_NUMBER, debtInfoType, locale);
    }

    /**
     * Create request from quittance number
     *
     * @param quittanceNumber Quittance number
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest quittanceNumberRequest(@NotNull String quittanceNumber, RequestType debtInfoType, Locale locale) {
        return new InfoRequest(quittanceNumber, TYPE_QUITTANCE_NUMBER, debtInfoType, locale);
    }

    /**
     * Create request from apartment master index
     *
     * @param apartmentNumber Apartment master index
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest apartmentNumberRequest(@NotNull String apartmentNumber, RequestType debtInfoType, Locale locale) {
        return new InfoRequest(apartmentNumber, TYPE_APARTMENT_NUMBER, debtInfoType, locale);
    }

    /**
     * Create request from service provider account number
     *
     * @param seviceProviderAccountNumber service provider account number
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest serviceProviderAccountNumberRequest(@NotNull String seviceProviderAccountNumber, RequestType debtInfoType, Locale locale) {
        return new InfoRequest(seviceProviderAccountNumber, TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER, debtInfoType, locale);
    }

    /**
     * Create request from address calculated by the personal account number of service provider
     *
     * @param address address calculated by the personal account number of service provider
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest addressRequest(@NotNull String address, RequestType debtInfoType, Locale locale) {
        return new InfoRequest(address, TYPE_ADDRESS, debtInfoType, locale);
    }

    /**
     * Create request from combined request
     *
     * @param request request string
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest combinedRequest(@NotNull String request, RequestType debtInfoType, Locale locale) {
        String[] req = request.split(":");
        String num = req.length > 1 ? req[1] : req[0];
        if (num.startsWith("10") && num.length() == 9) {
            log.debug("!new combined request (5)");
            return new InfoRequest(request, TYPE_COMBINED, debtInfoType, locale);
        }
        log.debug("!new service provider request (4)");
        return new InfoRequest(request, TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER, debtInfoType, locale);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("requestId", requestId).
                append("request", request).
                append("type", type).
                toString();
    }

}
