package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ServiceProviderFileProcessorRecordsTxImpl extends ServiceProviderFileProcessorTxImpl {
	private Logger log = LoggerFactory.getLogger(getClass());

	private RegistryRecordService registryRecordService;

	private StopWatch updateRecordsWatch = new StopWatch();

	{
		updateRecordsWatch.start();
		updateRecordsWatch.suspend();
	}

	@Transactional (readOnly = false)
	protected void postUpdated(ProcessingContext context) throws FlexPayException {
		updateRecordsWatch.resume();
		log.debug("Start update records");
		for (RegistryRecord record : context.getOperationRecords()) {
			registryRecordService.update(record);
		}
		log.debug("Records updated");
		updateRecordsWatch.suspend();

		super.postUpdated(context);
		printWatch();
	}

	private void printWatch() {
		log.debug("Update records time: {}", updateRecordsWatch);
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}
}
