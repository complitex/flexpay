package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class OperationsChain extends Operation {

	private List<Operation> containers = Collections.emptyList();

	public OperationsChain(List<Operation> containers) {
		super(0);
		this.containers = containers;
	}

	/**
	 * Process all containers in a chain
	 * 
	 * @throws org.flexpay.common.exception.FlexPayException
	 */
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
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
