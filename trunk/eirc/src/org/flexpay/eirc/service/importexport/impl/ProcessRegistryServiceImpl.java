package org.flexpay.eirc.service.importexport.impl;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.exchange.ContainerProcessHelper;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.persistence.exchange.SetExternalOrganizationAccountOperation;
import org.flexpay.eirc.persistence.exchange.SetResponsiblePersonOperation;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.eirc.service.ProcessRegistryVariableInstanceService;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.eirc.service.importexport.ProcessRecordsRangeService;
import org.flexpay.eirc.service.importexport.ProcessRegistryService;
import org.hibernate.FlushMode;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class ProcessRegistryServiceImpl implements ProcessRegistryService {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private RegistryProcessor processor;
	private RegistryService registryService;
	private RegistryFileService registryFileService;
	private RegistryWorkflowManager registryWorkflowManager;
	private ProcessRecordsRangeService processRecordsRangeService;
	private ProcessRegistryVariableInstanceService processRegistryVariableInstanceService;

	private HibernateTransactionManager transactionManager;

	private StopWatch updateWatch = new StopWatch();
	private StopWatch getRecordsWatch = new StopWatch();
	private StopWatch processRecordsWatch = new StopWatch();

	{
		updateWatch.start();
		updateWatch.suspend();

		getRecordsWatch.start();
		getRecordsWatch.suspend();

		processRecordsWatch.start();
		processRecordsWatch.suspend();
	}

	@Override
	public ProcessingContext prepareContext(@NotNull Registry registry) throws FlexPayException {
		return processRecordsRangeService.prepareContext(registry);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean processRegistryRecordRange(@NotNull FetchRange range, @NotNull ProcessRegistryVariableInstance variable, @NotNull ProcessingContext context) {

		List<RegistryRecord> records = processRegistryRecordRange(range, context);
		return records != null && updateProcessState(variable, records);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private List<RegistryRecord> processRegistryRecordRange(@NotNull FetchRange range, @NotNull ProcessingContext context) {

		log.debug("Current session flush mode: {}", transactionManager.getSessionFactory().getCurrentSession().getFlushMode().toString());
		transactionManager.getSessionFactory().getCurrentSession().setFlushMode(FlushMode.COMMIT);
		log.debug("New session flush mode: {}", transactionManager.getSessionFactory().getCurrentSession().getFlushMode().toString());

		List<RegistryRecord> records;
//		getRecordsWatch.resume();
		records = registryFileService.getRecordsForProcessing(Stub.stub(context.getRegistry()), range);
		for (RegistryRecord record : records) {
			record.setRegistry(context.getRegistry());
		}
//		getRecordsWatch.suspend();

//		processRecordsWatch.resume();
		try {
			if (!processRecordsRangeService.processRecords(records, context)) {
				return null;
			}
		} finally {
//			processRecordsWatch.suspend();
		}

//		updateWatch.resume();
		if (!doUpdate(context)) {
			return null;
		}
//		updateWatch.suspend();
		return records;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean processRegistryRecordEnumeration(@NotNull List<Long> recordIds, @NotNull ProcessRegistryVariableInstance variable,
												 @NotNull ProcessingContext context) {

		log.debug("Current session flush mode: {}", transactionManager.getSessionFactory().getCurrentSession().getFlushMode().toString());
		transactionManager.getSessionFactory().getCurrentSession().setFlushMode(FlushMode.COMMIT);
		log.debug("New session flush mode: {}", transactionManager.getSessionFactory().getCurrentSession().getFlushMode().toString());

		List<RegistryRecord> records;
		getRecordsWatch.resume();
		records = registryFileService.getRecordsForProcessing(CollectionUtils.<Long>set(recordIds));
		for (RegistryRecord record : records) {
			record.setRegistry(context.getRegistry());
		}
		getRecordsWatch.suspend();

		processRecordsWatch.resume();
		try {
			if (!processRecordsRangeService.processRecords(records, context)) {
				return false;
			}
		} finally {
			processRecordsWatch.suspend();
		}

		updateWatch.resume();
		if (!doUpdate(variable, context, records)) {
			return false;
		}
		updateWatch.suspend();
		return true;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ProcessRegistryVariableInstance prepare(@NotNull ProcessingContext context, @NotNull ProcessRegistryVariableInstance variable)
			throws Exception {

		processor.startRegistryProcessing(context);
		processor.processHeader(context);

		return processRegistryVariableInstanceService.create(variable);
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	private boolean doUpdate(final ProcessRegistryVariableInstance variable, ProcessingContext context, List<RegistryRecord> records) {
		return !doUpdate(context) && updateProcessState(variable, records);
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	private boolean doUpdate(ProcessingContext context) {
		return !processRecordsRangeService.doUpdate(context);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private boolean updateProcessState(ProcessRegistryVariableInstance variable, List<RegistryRecord> records) {
		if (records.size() > 0) {
			synchronized (variable) {
				ProcessRegistryVariableInstance currentVariable = processRegistryVariableInstanceService.readFull(Stub.stub(variable));

				Long lastProcessedRegistryRecord = records.get(records.size() - 1).getId();
				Integer processedCountRecords = currentVariable.getProcessedCountRecords() == null? 0: currentVariable.getProcessedCountRecords();
				processedCountRecords += records.size();

				currentVariable.setProcessedCountRecords(processedCountRecords);
				variable.setProcessedCountRecords(processedCountRecords);

				if (currentVariable.getLastProcessedRegistryRecord() != null
						&& lastProcessedRegistryRecord > currentVariable.getLastProcessedRegistryRecord()) {

					currentVariable.setLastProcessedRegistryRecord(lastProcessedRegistryRecord);
					variable.setLastProcessedRegistryRecord(lastProcessedRegistryRecord);

				}
				processRegistryVariableInstanceService.update(currentVariable);
			}
		}
		return true;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public boolean endProcessing(@NotNull ProcessingContext context) {
		try {
			processor.endRegistryProcessing(context);

			return true;
		} catch (TransitionNotAllowed transitionNotAllowed) {
			log.error("Inner error", transitionNotAllowed);
		}
		return false;
	}

	@Override
	public void printWatch() {
		log.debug("Get records time: {}, full operations time: {}, update time: {}",
				new Object[]{getRecordsWatch, processRecordsWatch, updateWatch});
		SetResponsiblePersonOperation.printWatch();
		SetExternalOrganizationAccountOperation.printWatch();
		ContainerProcessHelper.printWatch();
	}

	@Required
	public void setProcessor(RegistryProcessor processor) {
		this.processor = processor;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	@Required
	public void setRegistryFileService(RegistryFileService registryFileService) {
		this.registryFileService = registryFileService;
	}

	@Required
	public void setProcessRecordsRangeService(ProcessRecordsRangeService processRecordsRangeService) {
		this.processRecordsRangeService = processRecordsRangeService;
	}

	@Required
	public void setProcessRegistryVariableInstanceService(ProcessRegistryVariableInstanceService processRegistryVariableInstanceService) {
		this.processRegistryVariableInstanceService = processRegistryVariableInstanceService;
	}

	@Required
	public void setTransactionManager(HibernateTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
