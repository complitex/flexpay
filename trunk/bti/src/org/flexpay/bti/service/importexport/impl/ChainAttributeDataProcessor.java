package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.bti.service.importexport.BuildingAttributeDataProcessor;
import org.flexpay.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Set;

public class ChainAttributeDataProcessor implements BuildingAttributeDataProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Set<BuildingAttributeDataProcessor> processors = CollectionUtils.set();

	/**
	 * ProcessInstance attribute data for specified time interval
	 *
	 * @param begin Interval begin date
	 * @param end   Interval end date
	 * @param data  Attributes data
	 */
	public void processData(Date begin, Date end, BuildingAttributeData data) {

		RuntimeException firstEx = null;

		for (BuildingAttributeDataProcessor processor : processors) {
			try {
			processor.processData(begin, end, data);
			} catch (Exception ex) {
				log.warn("Unexpected exception", ex);
				if (firstEx == null) {
					firstEx = new RuntimeException("Unexpected exception", ex);
				}
			}
		}

		if (firstEx != null) {
			throw firstEx;
		}
	}

	@Required
	public void setProcessors(Set<BuildingAttributeDataProcessor> processors) {
		this.processors = processors;
	}
}
