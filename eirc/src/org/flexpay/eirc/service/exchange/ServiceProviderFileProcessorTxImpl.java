package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc. <br />
 * Precondition for processing file is complete import operation, i.e. all records should already have assigned
 * PersonalAccount.
 */
@Transactional (readOnly = true)
public class ServiceProviderFileProcessorTxImpl implements ServiceProviderFileProcessorTx {

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
	public void processRecord(Registry registry, RegistryRecord record) throws Exception {

		if (!recordWorkflowManager.hasSuccessTransition(record)
			|| record.getRecordStatus().isProcessedWithError()) {

			log.debug("Skipping record: {}", record);
			return;
		}

		log.debug("Record to process: {}", record);

		Operation op = serviceOperationsFactory.getOperation(registry, record);
		DelayedUpdate update = op.process(registry, record);
		update.doUpdate();
		recordWorkflowManager.setNextSuccessStatus(record);
	}

	@Required
	public void setServiceOperationsFactory(ServiceOperationsFactory serviceOperationsFactory) {
		this.serviceOperationsFactory = serviceOperationsFactory;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

}
