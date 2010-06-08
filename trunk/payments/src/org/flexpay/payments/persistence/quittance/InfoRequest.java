package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.payments.actions.request.data.DebtsRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

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

    protected String requestId;
    protected String request;
    protected int type = DebtsRequest.SEARCH_QUITTANCE_DEBT_REQUEST;
    private int debtInfoType;

    private InfoRequest(String request, int type, int debtInfoType) {
        this.request = request;
        this.type = type;
        this.debtInfoType = debtInfoType;
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

    public Integer getDebtInfoType() {
        return debtInfoType;
    }

    public void setDebtInfoType(Integer debtInfoType) {
        this.debtInfoType = debtInfoType;
    }

    /**
     * Create request from account number
     *
     * @param accountNumber Account number
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest accountNumberRequest(@NotNull String accountNumber, int debtInfoType) {
        return new InfoRequest(accountNumber, TYPE_ACCOUNT_NUMBER, debtInfoType);
    }

    /**
     * Create request from quittance number
     *
     * @param quittanceNumber Quittance number
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest quittanceNumberRequest(@NotNull String quittanceNumber, int debtInfoType) {
        return new InfoRequest(quittanceNumber, TYPE_QUITTANCE_NUMBER, debtInfoType);
    }

    /**
     * Create request from apartment master index
     *
     * @param apartmentNumber Apartment master index
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest apartmentNumberRequest(@NotNull String apartmentNumber, int debtInfoType) {
        return new InfoRequest(apartmentNumber, TYPE_APARTMENT_NUMBER, debtInfoType);
    }

    /**
     * Create request from service provider account number
     *
     * @param seviceProviderAccountNumber service provider account number
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest serviceProviderAccountNumberRequest(@NotNull String seviceProviderAccountNumber, int debtInfoType) {
        return new InfoRequest(seviceProviderAccountNumber, TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER, debtInfoType);
    }

    /**
     * Create request from address calculated by the personal account number of service provider
     *
     * @param address address calculated by the personal account number of service provider
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest addressRequest(@NotNull String address, int debtInfoType) {
        return new InfoRequest(address, TYPE_ADDRESS, debtInfoType);
    }

    /**
     * Create request from combined request
     *
     * @param request request string
     * @param debtInfoType debtInfoType
     * @return Request object
     */
    public static InfoRequest combinedRequest(@NotNull String request, int debtInfoType) {
        String[] req = request.split(":");
        String num = req.length > 1 ? req[1] : req[0];
        if (num.startsWith("10") && num.length() == 9) {
            log.debug("!new combined request (5)");
            return new InfoRequest(request, TYPE_COMBINED, debtInfoType);
        }
        log.debug("!new service provider request (4)");
        return new InfoRequest(request, TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER, debtInfoType);
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
