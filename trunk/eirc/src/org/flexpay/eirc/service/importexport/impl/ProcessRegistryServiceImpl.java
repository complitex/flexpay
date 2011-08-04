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
import org.flexpay.eirc.service.ProcessRegistryVariableInstanceService;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.eirc.service.importexport.ProcessRecordsRangeService;
import org.flexpay.eirc.service.importexport.ProcessRegistryService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
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

	private JpaTransactionManager transactionManager;

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
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean processRegistryRecordRange(@NotNull FetchRange range, @NotNull ProcessRegistryVariableInstance variable, @NotNull ProcessingContext context) {

		context.setRegistry(registryService.read(Stub.stub(context.getRegistry())));
		log.debug("Processing registry: {}", context.getRegistry());

		List<RegistryRecord> records = registryFileService.getRecordsForProcessing(Stub.stub(context.getRegistry()), range);

		return processRegistryRecordRange(range, context, records) && updateProcessState(variable, records);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean processRestRegistryRecordRange(@NotNull FetchRange range, @NotNull ProcessingContext context) {

		if (!registryFileService.hasLoadedAndFixedRecords(Stub.stub(context.getRegistry()))) {
			return false;
		}

		context.setRegistry(registryService.read(Stub.stub(context.getRegistry())));
		log.debug("Processing registry: {}", context.getRegistry());

		List<RegistryRecord> records = registryFileService.getLoadedAndFixedRecords(Stub.stub(context.getRegistry()), range);

		return processRegistryRecordRange(range, context, records);
	}

	private boolean processRegistryRecordRange(@NotNull FetchRange range, @NotNull ProcessingContext context,
															@NotNull List<RegistryRecord> records) {

/*		EntityManager entityManager = transactionManager.getEntityManagerFactory().createEntityManager();

		log.debug("Current session flush mode: {}", entityManager.getFlushMode().toString());
		entityManager.setFlushMode(FlushModeType.COMMIT);
		log.debug("New session flush mode: {}", entityManager.getFlushMode().toString());
*/
		for (RegistryRecord record : records) {
			record.setRegistry(context.getRegistry());
		}
//		getRecordsWatch.suspend();

//		processRecordsWatch.resume();
		log.debug("start processRecordsRangeService.processRecords()");
		try {
			if (!processRecordsRangeService.processRecords(records, context)) {
				log.debug("failed processRecordsRangeService.processRecords()");
				return false;
			}
		} finally {
//			processRecordsWatch.suspend();
		}
		log.debug("end processRecordsRangeService.processRecords() and start doUpdate()");

//		updateWatch.resume();
		if (!doUpdate(context)) {
			log.debug("failed doUpdate()");
			return false;
		}
		log.debug("end doUpdate()");
//		updateWatch.suspend();
		return true;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean processRegistryRecordEnumeration(@NotNull List<Long> recordIds, @NotNull ProcessRegistryVariableInstance variable,
												 @NotNull ProcessingContext context) {

		EntityManager entityManager = transactionManager.getEntityManagerFactory().createEntityManager();

		log.debug("Current session flush mode: {}", entityManager.getFlushMode().toString());
		entityManager.setFlushMode(FlushModeType.COMMIT);
		log.debug("New session flush mode: {}", entityManager.getFlushMode().toString());

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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProcessRegistryVariableInstance prepare(@NotNull ProcessingContext context, @NotNull ProcessRegistryVariableInstance variable)
			throws Exception {

		processor.startRegistryProcessing(context);
		processor.processHeader(context);

		return processRegistryVariableInstanceService.create(variable);
	}

	private boolean doUpdate(final ProcessRegistryVariableInstance variable, ProcessingContext context, List<RegistryRecord> records) {
		return doUpdate(context) && updateProcessState(variable, records);
	}

	private boolean doUpdate(ProcessingContext context) {
		return processRecordsRangeService.doUpdate(context);
	}

	@SuppressWarnings({"null", "unchecked"})
	private boolean updateProcessState(ProcessRegistryVariableInstance variable, List<RegistryRecord> records) {
		if (records.size() > 0) {
			synchronized (variable) {
				ProcessRegistryVariableInstance currentVariable =
						processRegistryVariableInstanceService.update(new Stub<ProcessRegistryVariableInstance>(variable),
								records.get(records.size() - 1).getId(), records.size());
				if (currentVariable == null) {
					return false;
				}
				variable.setProcessedCountRecords(currentVariable.getProcessedCountRecords());
				variable.setLastProcessedRegistryRecord(currentVariable.getLastProcessedRegistryRecord());
			}
			return true;
		}
		return false;
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
	public void setTransactionManager(JpaTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
