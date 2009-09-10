package org.flexpay.eirc.persistence.exchange;

import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class SimplePaymentOperation extends PaymentOperation {
    public SimplePaymentOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
        super(factory, datum);
    }

    @Override
    public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {
        return validate(context)? super.process(context): DelayedUpdateNope.INSTANCE;
    }

    Long getOperationId(ProcessingContext context) {
        return context.getCurrentRecord().getUniqueOperationNumber();
    }

    BigDecimal getOperationSum(ProcessingContext context) {
        return context.getCurrentRecord().getAmount();
    }

    private boolean validate(ProcessingContext context) {
        if (context.getNumberInstanceId() == null) {
            log.error("Can not create simple payment for organization {} in registry {}. Number instance id is not defined",
                    new Object[]{organizationId, context.getRegistry().getId()});
            return false;
        }
        if (context.getNumberInstanceId().equals(ApplicationConfig.getInstanceId())) {
            log.error("Can not create simple payment on same database. Destination number instance id is {}", context.getNumberInstanceId());
            return false;
        }
        return true;
    }
}
