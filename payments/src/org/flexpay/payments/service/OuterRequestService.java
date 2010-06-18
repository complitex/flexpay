package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.request.data.request.InfoRequest;
import org.flexpay.payments.actions.request.data.request.PayRequest;
import org.flexpay.payments.actions.request.data.request.ReversalPayRequest;
import org.flexpay.payments.actions.request.data.response.PayInfoResponse;
import org.flexpay.payments.actions.request.data.response.QuittanceDetailsResponse;
import org.flexpay.payments.actions.request.data.response.SimpleResponse;
import org.jetbrains.annotations.NotNull;

public interface OuterRequestService {

    /**
     * Find quittance details
     *
     * @param request Request for quittance details
     * @return Details response
     */
    @NotNull
    QuittanceDetailsResponse findQuittance(InfoRequest request);

    @NotNull
    PayInfoResponse quittancePay(PayRequest payRequest) throws FlexPayException;

    @NotNull
    SimpleResponse refund(ReversalPayRequest reversalPayRequest) throws FlexPayException;

}
