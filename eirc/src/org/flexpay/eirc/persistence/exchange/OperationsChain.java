package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdatesContainer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.process.ProcessDefinitionManagerImpl.getISODateFormat;

public class OperationsChain extends Operation {

	private static final Logger log = LoggerFactory.getLogger(OperationsChain.class);
	private static final SimpleDateFormat sdf = getISODateFormat();

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
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {

		DelayedUpdatesContainer container = new DelayedUpdatesContainer();
		//log.debug("Process operations size: {}", containers.size());
		for (Operation operation : containers) {
//			log.debug("Process operation: {}", operation.getClass());
//			System.out.println(sdf.format(new Date()) + " Process operation: " + operation.getClass());
			DelayedUpdate update = operation.process(context);
			container.addUpdate(update);
//			System.out.println(sdf.format(new Date()) + " Processed operation: " + operation.getClass());
//			log.debug("Processed operation: {}", operation.getClass());
		}

		return container;
	}

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
