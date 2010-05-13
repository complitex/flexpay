package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.quittance.PayInfoResponse;
import org.flexpay.payments.persistence.quittance.PayRequest;

public interface QuittancePayer {

    PayInfoResponse quittancePay(PayRequest payRequest) throws FlexPayException;

}
