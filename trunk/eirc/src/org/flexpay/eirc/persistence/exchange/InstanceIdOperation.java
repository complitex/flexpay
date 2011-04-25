package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.flexpay.common.util.config.ApplicationConfig.getInstanceId;

public class InstanceIdOperation extends ContainerOperation {

	private static final Logger log = LoggerFactory.getLogger(InstanceIdOperation.class);
	private String instanceId;

	public InstanceIdOperation(List<String> datum) throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));

		if (datum.size() != 2) {
			throw new InvalidContainerException("Expected 2 fields in instance id container: " + datum);
		}

		try {
			instanceId = datum.get(1);
		} catch (Exception e) {
			throw new InvalidContainerException("Invalid instance id container", e);
		}
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @return DelayedUpdate object
	 * @throws FlexPayException if validation fails
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {

		log.debug("Setting source instance id: {}", instanceId);
		context.setSourceInstanceId(instanceId);

		if (getInstanceId().equals(instanceId)
			&& context.getRegistry().getRegistryType().isPayments()) {
			throw new FlexPayException("Same instance: " + instanceId,
					"eirc.error.registry.processing.payments.same_instance");
		}

		return DelayedUpdateNope.INSTANCE;
	}
}
