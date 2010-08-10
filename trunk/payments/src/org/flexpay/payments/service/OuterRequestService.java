package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.outerrequest.request.*;
import org.flexpay.payments.actions.outerrequest.request.response.*;
import org.jetbrains.annotations.NotNull;

public interface OuterRequestService {

    @NotNull
    GetDebtInfoResponse getDeftInfo(GetDebtInfoRequest request);

    @NotNull
    GetQuittanceDebtInfoResponse getQuittanceDeftInfo(GetQuittanceDebtInfoRequest request);

    @NotNull
    PayDebtResponse quittancePay(PayDebtRequest request) throws FlexPayException;

    @NotNull
    ReversalPayResponse reversalPay(ReversalPayRequest request) throws FlexPayException;

    @NotNull
    RegistryCommentResponse addRegistryComment(RegistryCommentRequest request) throws FlexPayException;

    @NotNull
    GetRegistryListResponse getRegistryList(GetRegistryListRequest request) throws FlexPayException;

    @NotNull
    GetServiceListResponse getServiceList(GetServiceListRequest request) throws FlexPayException;

}
