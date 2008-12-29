package org.flexpay.eirc.service.exchange;

import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc. <br />
 * Precondition for processing file is complete import operation, i.e. all records should already have assigned
 * PersonalAccount.
 */
@Transactional (readOnly = true)
public class ServiceProviderFileProcessorTx {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceOperationsFactory serviceOperationsFactory;

	private RegistryRecordWorkflowManager recordWorkflowManager;

	/**
	 * Run processing on single registry record
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws Exception if failure occurs
	 */
	@Transactional (readOnly = false, rollbackFor = Throwable.class)
	public void processRecord(SpRegistry registry, RegistryRecord record) throws Exception {

		if (!recordWorkflowManager.hasSuccessTransition(record)
			|| record.getRecordStatus().isProcessedWithError()) {

			log.debug("Skipping record: {}", record);
			return;
		}

		log.debug("Record to process: {}", record);

		Operation op = serviceOperationsFactory.getOperation(registry, record);
		op.process(registry, record);
		recordWorkflowManager.setNextSuccessStatus(record);
	}

	public void setServiceOperationsFactory(ServiceOperationsFactory serviceOperationsFactory) {
		this.serviceOperationsFactory = serviceOperationsFactory;
	}

	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}
}
