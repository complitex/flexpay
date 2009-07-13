package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperationsChain extends Operation {

	private List<Operation> containers = Collections.emptyList();

	public OperationsChain(List<Operation> containers) {
		this.containers = containers;
	}

	/**
	 * Process all containers in a chain
	 * 
	 * @throws org.flexpay.common.exception.FlexPayException
	 */
	public void process(Registry registry, RegistryRecord record) throws FlexPayException, FlexPayExceptionContainer {
		for (Operation container : containers) {
			container.process(registry, record);
		}
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
