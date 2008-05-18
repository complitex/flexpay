package org.flexpay.eirc.util.standalone;

import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;
import org.flexpay.eirc.persistence.SpFile;
import org.apache.log4j.Logger;

public class RunSpFileProcessing implements StandaloneTask {

	private Logger log = Logger.getLogger(getClass());
	private ServiceProviderFileProcessor fileProcessor;

	/**
	 * Execute task
	 */
	public void execute() {

		try {
			fileProcessor.processFile(new SpFile(33L));
		} catch (Exception e) {
			log.error("Failed processing registry file", e);
		}
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
