package org.flexpay.payments.action.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public enum Status {

    SUCCESS(1, "payments.outer_request.status.success"),
    INCORRECT_AUTHENTICATION_DATA(8, "payments.outer_request.status.incorrect_authentication_data"),
    UNKNOWN_REQUEST(9, "payments.outer_request.status.unknown_request"),
    QUITTANCE_NOT_FOUND(10, "payments.outer_request.status.quittance_not_found"),
    ACCOUNT_NOT_FOUND(11, "payments.outer_request.status.account_not_found"),
    APARTMENT_NOT_FOUND(12, "payments.outer_request.status.apartment_not_found"),
    INVALID_QUITTANCE_NUMBER(13, "payments.outer_request.status.invalid_quittance_number"),
    INTERNAL_ERROR(14, "payments.outer_request.status.internal_error"),
    RECIEVE_TIMEOUT(15, "payments.outer_request.status.recieve_timeout"),
    SERVICE_NOT_FOUND(16, "payments.outer_request.status.service_not_found"),
    INCORRECT_PAY_SUM(17, "payments.outer_request.status.incorrect_pay_sum"),
    REQUEST_IS_NOT_PROCESSED(18, "payments.outer_request.status.request_is_not_processed"),
    INCORRECT_OPERATION_ID(19, "payments.outer_request.status.incorrect_operation_id"),
    REVERSE_IS_NOT_POSSIBLE(20, "payments.outer_request.status.reverse_is_not_possible"),
    REGISTRY_NOT_FOUND(21, "payments.outer_request.status.registry_not_found"),
    REQUEST_IS_NOT_PROCESSED_TRADING_DAY_WAS_CLOSED(22, "payments.outer_request.status.request_is_not_processed_trading_day_closed"),
    INCORRECT_MASTER_INDEX_ADDRESS_INPUT_DATA(23, "payments.outer_request.status.incorrect_master_index_address_input_data"),
    PARENT_NOT_FOUND(24, "payments.outer_request.status.parent_not_found");

    private int code;
    private String textKey;

    Status(int code, String textKey) {
        this.code = code;
        this.textKey = textKey;
    }

    public int getCode() {
        return code;
    }

    public String getTextKey() {
        return textKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("code", code).
                append("textKey", getTextKey()).
                toString();
    }

    public static Status getStaus(int code) {
        for (Status status : Status.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
