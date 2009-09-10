package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.exchange.delayed.PaymentOperationDelayedUpdate;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class BankPaymentOperation extends PaymentOperation {

	private Long operationId;
	private BigDecimal summ;

	public BankPaymentOperation(ServiceOperationsFactory factory, List<String> datum)
		throws InvalidContainerException {

		super(factory, datum);

		try {
			operationId = Long.parseLong(datum.get(2));
			summ = new BigDecimal(datum.get(3));
		} catch (Exception e) {
			throw new InvalidContainerException("Invalid bank payment container", e);
		}
	}

    Long getOperationId(ProcessingContext context) {
        return operationId;
    }

    BigDecimal getOperationSum(ProcessingContext context) {
        return summ;
    }
}
