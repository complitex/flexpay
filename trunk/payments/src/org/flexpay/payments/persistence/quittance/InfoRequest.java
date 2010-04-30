package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.payments.actions.search.data.SearchDebtsRequest;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class InfoRequest implements Serializable {

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

    protected String requestId;
    protected String request;
    protected int type = SearchDebtsRequest.QUITTANCE_DEBT_REQUEST;
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
     * @return Request object
     */
    public static InfoRequest accountNumberRequest(@NotNull String accountNumber, int debtInfoType) {
        return new InfoRequest(accountNumber, TYPE_ACCOUNT_NUMBER, debtInfoType);
    }

    /**
     * Create request from quittance number
     *
     * @param quittanceNumber Quittance number
     * @return Request object
     */
    public static InfoRequest quittanceNumberRequest(@NotNull String quittanceNumber, int debtInfoType) {
        return new InfoRequest(quittanceNumber, TYPE_QUITTANCE_NUMBER, debtInfoType);
    }

    /**
     * Create request from apartment master index
     *
     * @param apartmentNumber Apartment master index
     * @return Request object
     */
    public static InfoRequest apartmentNumberRequest(@NotNull String apartmentNumber, int debtInfoType) {
        return new InfoRequest(apartmentNumber, TYPE_APARTMENT_NUMBER, debtInfoType);
    }

    /**
     * Create request from service provider account number
     *
     * @param seviceProviderAccountNumber service provider account number
     * @return Request object
     */
    public static InfoRequest serviceProviderAccountNumberRequest(@NotNull String seviceProviderAccountNumber, int debtInfoType) {
        return new InfoRequest(seviceProviderAccountNumber, TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER, debtInfoType);
    }

    /**
     * Create request from address calculated by the personal account number of service provider
     *
     * @param address address calculated by the personal account number of service provider
     * @return Request object
     */
    public static InfoRequest addressRequest(@NotNull String address, int debtInfoType) {
        return new InfoRequest(address, TYPE_ADDRESS, debtInfoType);
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
