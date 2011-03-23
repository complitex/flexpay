package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.flexpay.common.util.config.ApplicationConfig.getInstanceId;

public class SimplePaymentOperation extends PaymentOperation {

	private static final Logger log = LoggerFactory.getLogger(SimplePaymentOperation.class);

	public SimplePaymentOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(factory, datum);
	}

	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {
		return validate(context) ? super.process(context) : DelayedUpdateNope.INSTANCE;
	}

	Long getOperationId(ProcessingContext context) {
		return context.getCurrentRecord().getUniqueOperationNumber();
	}

	BigDecimal getOperationSum(ProcessingContext context) {
		return context.getCurrentRecord().getAmount();
	}

	private boolean validate(ProcessingContext context) {

		if (getInstanceId().equals(context.getSourceInstanceId())) {
			log.error("Can not create simple payment on same database. Source instance id is {}",
					context.getSourceInstanceId());
			return false;
		}
		return true;
	}
}
