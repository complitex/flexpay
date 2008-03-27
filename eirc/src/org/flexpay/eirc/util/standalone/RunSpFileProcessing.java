package org.flexpay.eirc.util.standalone;

import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;
import org.flexpay.eirc.persistence.SpFile;

public class RunSpFileProcessing implements StandaloneTask {

	private ServiceProviderFileProcessor fileProcessor;

	/**
	 * Execute task
	 */
	public void execute() {

		fileProcessor.processFile(new SpFile(33L));
	}

	/**
	 * Setter for property 'fileProcessor'.
	 *
	 * @param fileProcessor Value to set for property 'fileProcessor'.
	 */
	public void setFileProcessor(ServiceProviderFileProcessor fileProcessor) {
		this.fileProcessor = fileProcessor;
	}
}
