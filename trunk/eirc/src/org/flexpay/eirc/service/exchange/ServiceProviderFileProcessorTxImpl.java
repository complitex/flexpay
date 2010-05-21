package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.exchange.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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

	private StopWatch beforeUpdateWatch = new StopWatch();
	private StopWatch updateWatch = new StopWatch();
	private StopWatch nextStatusWatch = new StopWatch();

	private OperationWatchContext watchContext = new OperationWatchContext();

	{
		beforeUpdateWatch.start();
		beforeUpdateWatch.suspend();

		updateWatch.start();
		updateWatch.suspend();

		nextStatusWatch.start();
		nextStatusWatch.suspend();
	}

	/**
	 * Process header
	 *
	 * @param context Processing context
	 * @throws Exception if failure occurs
	 */
	@Transactional (readOnly = false)
	@Override
	public void processHeader(@NotNull ProcessingContext context) throws Exception {

		Registry registry = context.getRegistry();
		log.debug("Header to process: {}", registry);

		Operation op = serviceOperationsFactory.getContainerOperation(registry);
		DelayedUpdate update = op.process(context);
		context.addUpdate(update);
		doUpdate(context);
	}

	/**
	 * Prepare delayed updates for single registry record
	 *
	 * @param context Processing context
	 * @throws Exception if failure occurs
	 */
	@Transactional (readOnly = false)
	@Override
	public void prepareRecordUpdates(@NotNull ProcessingContext context) throws Exception {

		RegistryRecord record = context.getCurrentRecord();
		if (!recordWorkflowManager.hasSuccessTransition(record)
			|| record.getRecordStatus().isProcessedWithError()) {

			log.debug("Skipping record: {}", record);
			return;
		}

		log.debug("Record to process: {}", record);

		Operation op = serviceOperationsFactory.getOperation(context.getRegistry(), record);
		DelayedUpdate update = op.process(context, watchContext);
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
			beforeUpdateWatch.resume();
			context.beforeUpdate();
			beforeUpdateWatch.suspend();

			updateWatch.resume();
			context.doUpdate();
			updateWatch.suspend();

			nextStatusWatch.resume();
			recordWorkflowManager.setNextSuccessStatus(context.getOperationRecords());
			nextStatusWatch.suspend();
			
			context.nextOperation();

			printWatch();
		} catch (Exception ex) {
			log.error("doUpdate failed", ex);
			throw ex;
		}
	}

	private void printWatch() {
		log.debug("Start time: {}", beforeUpdateWatch.getStartTime());
		log.debug("Operation time: {}, before update time: {}, update time: {}, next status time: {}",
				new Object[]{watchContext.getOperationProcessWatch(), beforeUpdateWatch, updateWatch, nextStatusWatch});
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
