package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NumberInstanceIdOperation extends ContainerOperation {
    private ServiceOperationsFactory factory;

    private String numberInstanceId;

    public NumberInstanceIdOperation(ServiceOperationsFactory factory, List<String> datum)
		throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));
		this.factory = factory;

		try {
			numberInstanceId = datum.get(1);
		} catch (Exception e) {
			throw new InvalidContainerException("Invalid bank payment container", e);
		}
	}

    /**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @return DelayedUpdate object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
    @Override
    public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {
        context.setNumberInstanceId(numberInstanceId);
        return DelayedUpdateNope.INSTANCE;
    }
}
