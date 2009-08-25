package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

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
	 * Prepare delayed updates for single registry record
	 *
	 * @param context Processing context
	 * @throws Exception if failure occurs
	 */
	@Transactional (readOnly = false)
	public void prepareRecordUpdates(@NotNull ProcessingContext context) throws Exception {

		RegistryRecord record = context.getCurrentRecord();
		if (!recordWorkflowManager.hasSuccessTransition(record)
			|| record.getRecordStatus().isProcessedWithError()) {

			log.debug("Skipping record: {}", record);
			return;
		}

		log.debug("Record to process: {}", record);

		Operation op = serviceOperationsFactory.getOperation(context.getRegistry(), record);
		DelayedUpdate update = op.process(context);
		context.addUpdate(update);
	}

	/**
	 * Do delayed update
	 *
	 * @param context ProcessingContext
	 * @throws Exception if failure occurs
	 */
	@Override
	@Transactional (readOnly = false)
	public void doUpdate(@NotNull ProcessingContext context) throws Exception {

		try {
			context.beforeUpdate();
			context.doUpdate();

			recordWorkflowManager.setNextSuccessStatus(context.getOperationRecords());
			context.nextOperation();
		} catch (Exception ex) {
			log.error("doUpdate failed", ex);
			throw ex;
		}
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
