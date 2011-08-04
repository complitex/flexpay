package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.ImportErrorDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.exchange.*;
import org.flexpay.eirc.service.importexport.EircImportConsumerDataTx;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

import static org.flexpay.common.process.ProcessDefinitionManagerImpl.getISODateFormat;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc. <br />
 * Precondition for processing file is complete import operation, i.e. all records should already have assigned
 * PersonalAccount.
 */
public class ServiceProviderFileProcessorTxImpl implements ServiceProviderFileProcessorTx {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceOperationsFactory serviceOperationsFactory;
	private RegistryRecordWorkflowManager recordWorkflowManager;
	private EircImportConsumerDataTx importConsumerDataService;
	private ImportErrorDao errorDao;

	private StopWatch getOperationWatch = new StopWatch();
	private StopWatch processBatchWatch = new StopWatch();

	private static final SimpleDateFormat sdf = getISODateFormat();

	private OperationWatchContext watchContext = new OperationWatchContext();

	{
		getOperationWatch.start();
		getOperationWatch.suspend();

		processBatchWatch.start();
		processBatchWatch.suspend();
	}

	/**
	 * ProcessInstance header
	 *
	 * @param context Processing context
	 * @throws Exception if failure occurs
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
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
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public void prepareRecordUpdates(@NotNull ProcessingContext context) throws Exception {

		RegistryRecord record = context.getCurrentRecord();
		if (!recordWorkflowManager.hasSuccessTransition(record)) {
			context.failedRecord(context.getCurrentRecord());
			log.debug("Skipping record: {}", record);
			return;
		}

		log.debug("Record to process: {}", record);

		RawConsumerData data;
//		processBatchWatch.resume();
		try {
			data = RawConsumersDataUtil.convert(context.getRegistry(), context.getCurrentRecord());

			if (!importConsumerDataService.processBatch(context.getSd(), data, context.getNameStreetMap(),
					context.getCorrectionUpdates())) {
				context.failedRecord(context.getCurrentRecord());
				return;
			}
		} finally {
//			processBatchWatch.suspend();
		}

		record = data.getRegistryRecord();
		if (!recordWorkflowManager.hasSuccessTransition(record)
			|| record.getRecordStatus().isProcessedWithError()) {
			log.debug("Skipping record: {}", record.getId());
			context.setCurrentRecord(null);
			return;
		}

		log.debug("Processing record to operate: {}", record.getId());
//		System.out.println(sdf.format(new Date()) + " Processing record to operate");

//		getOperationWatch.resume();
		Operation op = serviceOperationsFactory.getOperation(context.getRegistry(), record);
//		getOperationWatch.suspend();
//		DelayedUpdate update = op.process(context, watchContext);
//		log.debug("Operation class: {}", op.getClass());
//		System.out.println(sdf.format(new Date()) + " Operation class: " + op.getClass());
		DelayedUpdate update = op.process(context);
		log.debug("Record processed");
//		System.out.println(sdf.format(new Date()) + " Record processed");
		context.addUpdate(update);
		log.debug("Delayed update added to context");
//		System.out.println(sdf.format(new Date()) + " Delayed update added to context");
	}

	/**
	 * Do delayed update
	 *
	 * @param context ProcessingContext
	 * @throws Exception if failure occurs
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void doUpdate(@NotNull ProcessingContext context) throws Exception {

		try {
			log.debug("start context.beforeUpdate()");
			context.beforeUpdate();
			log.debug("end context.beforeUpdate()");

			log.debug("start context.doUpdate()");
			context.doUpdate();
			log.debug("end context.doUpdate()");

			log.debug("start postUpdated(context)");
			postUpdated(context);
			log.debug("end postUpdated(context)");
		} catch (Exception ex) {
			log.error("doUpdate failed", ex);
			throw ex;
		}
	}

	protected void postUpdated(ProcessingContext context) throws FlexPayException {

		for (RegistryRecord record : context.getOperationRecords()) {
			ImportError error = record.getImportError();
			if (error != null && error.isNew()) {
				errorDao.create(error);
			} else if (error != null && error.isNotActive()) {
				errorDao.delete(error);
				record.setImportError(null);
			} else if (error != null) {
				errorDao.update(error);
			}
		}

		context.nextOperation();

		importConsumerDataService.postProcessed();

		printWatch();
	}

	private void printWatch() {
		log.debug("Get operation time: {}, operation time: {}, process batch: {}",
				new Object[]{getOperationWatch, watchContext.getOperationProcessWatch(), processBatchWatch});
	}

	@Required
	public void setServiceOperationsFactory(ServiceOperationsFactory serviceOperationsFactory) {
		this.serviceOperationsFactory = serviceOperationsFactory;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	@Required
	public void setImportConsumerDataService(EircImportConsumerDataTx importConsumerDataService) {
		this.importConsumerDataService = importConsumerDataService;
	}

	@Required
	public void setErrorDao(ImportErrorDao errorDao) {
		this.errorDao = errorDao;
	}
}
