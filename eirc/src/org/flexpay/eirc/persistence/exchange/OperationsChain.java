package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdatesContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperationsChain extends Operation {

	private List<Operation> containers = Collections.emptyList();

	public OperationsChain(List<Operation> containers) {
		this.containers = containers;
	}

	/**
	 * ProcessInstance all containers in a chain
	 * 
	 * @throws org.flexpay.common.exception.FlexPayException
	 * @param context processing context
	 */
	@Transactional (readOnly = true)
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {

		DelayedUpdatesContainer container = new DelayedUpdatesContainer();
		for (Operation operation : containers) {
			DelayedUpdate update = operation.process(context);
			container.addUpdate(update);
		}

		return container;
	}

	@Transactional (readOnly = true)
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context, @NotNull OperationWatchContext watchContext) throws FlexPayException, FlexPayExceptionContainer {
		DelayedUpdatesContainer container = new DelayedUpdatesContainer();
		for (Operation operation : containers) {
			DelayedUpdate update = operation.process(context, watchContext);
			container.addUpdate(update);
		}

		return container;
	}

	/**
	 * Get container string representation
	 *
	 * @return container string representation
	 */
	public String getStringFormat() {
		List<String> formats = new ArrayList<String>(containers.size());
		for (Operation container : containers) {
			formats.add(container.toString());
		}
		return StringUtils.join(formats, CONTAINER_DELIMITER);
	}
}
