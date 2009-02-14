package org.flexpay.bti.service.importexport.impl;

public class ChainAttributeDataProcessorFactory {

	private ChainAttributeDataProcessor processor;

	public ChainAttributeDataProcessor getInstance() {
		if (processor == null) {
			processor = new ChainAttributeDataProcessor();
		}

		return processor;
	}
}
