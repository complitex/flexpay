package org.flexpay.eirc.process.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.persistence.exchange.ContainerProcessHelper;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.persistence.exchange.SetExternalOrganizationAccountOperation;
import org.flexpay.eirc.persistence.exchange.SetResponsiblePersonOperation;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.eirc.service.ProcessRegistryVariableInstanceService;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessorTx;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;

public class ProcessRegistryJob extends Job {

	private RegistryProcessor processor;
	private RegistryService registryService;
	private RegistryFileService registryFileService;
	private HandleError handleError;
	private RegistryWorkflowManager registryWorkflowManager;
	private ProcessRecordsRangeActionHandler processRecordsRange;
	private ProcessRegistryVariableInstanceService processRegistryVariableInstanceService;
	private ServiceProviderFileProcessorTx processorTx;

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
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		log.debug("start action");

		Logger processLog = ProcessLogger.getLogger(getClass());

		Long registryId = (Long)parameters.get(StartRegistryProcessingActionHandler.REGISTRY_ID);
		if (registryId == null) {
			log.error("Can not find '{}' in process parameters", StartRegistryProcessingActionHandler.REGISTRY_ID);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}
		Registry registry = registryService.readWithContainers(new Stub<Registry>(registryId));
		if (registry == null) {
			log.error("Can not find registry '{}'", registryId);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}

		FetchRange range = (FetchRange) parameters.get(HasMoreRecordActionHandler.RANGE);
		ProcessRegistryVariableInstance variable = processRegistryVariableInstanceService.findVariable(getProcessId());
		if (variable != null && variable.getLastProcessedRegistryRecord() != null) {
			range = registryFileService.getFetchRangeForProcessing(Stub.stub(registry), range.getPageSize(), variable.getLastProcessedRegistryRecord());
		}

		ProcessingContext context = processRecordsRange.prepareContext(registry);

		if (processing(range, variable, context)) return RESULT_NEXT;

		return RESULT_ERROR;
	}

	@Transactional(readOnly = true)
	private boolean processing(FetchRange range, ProcessRegistryVariableInstance variable, ProcessingContext context) {
		try {
			if (variable == null) {
				variable = prepare(context);
			}

			List<RegistryRecord> records;
			Integer processedCountRecords = variable.getProcessedCountRecords() == null? 0: variable.getProcessedCountRecords();
			do {
				getRecordsWatch.resume();
				records = registryFileService.getRecordsForProcessing(stub(context.getRegistry()), range);
				getRecordsWatch.suspend();

				processRecordsWatch.resume();
				if (!processRecordsRange.processRecords(records, context)) {
					return false;
				}
				processRecordsWatch.suspend();

				updateWatch.resume();
				if ((processedCountRecords = doUpdate(variable, context, records, processedCountRecords)) == null) {
					return false;
				}
				updateWatch.suspend();
				range.nextPage();
				context.nextOperation();

				log.debug("Processed count records: {}", variable.getProcessedCountRecords());
				printWatch();
			} while (range.hasMore());

			if (endProcessing(Stub.stub(context.getRegistry()))) return true;
		} catch (Throwable t) {
			setError(context, t);
		}
		return false;
	}

	@Transactional(readOnly = false)
	private ProcessRegistryVariableInstance prepare(ProcessingContext context) throws Exception {
		ProcessRegistryVariableInstance variable;

		registryWorkflowManager.startProcessing(context.getRegistry());
		processor.processHeader(context);

		variable = new ProcessRegistryVariableInstance();
		variable.setRegistry(context.getRegistry());
		variable.setProcessId(getProcessId());
		processRegistryVariableInstanceService.create(variable);
		return variable;
	}

	@Transactional(readOnly = false)
	private void setError(ProcessingContext context, Throwable t) {
		try {
			handleError.handleError(t, context);
		} catch (Exception e) {
			log.error("Inner error", e);
		}
	}

	@Transactional(readOnly = false)
	private Integer doUpdate(ProcessRegistryVariableInstance variable, ProcessingContext context, List<RegistryRecord> records, Integer processedCountRecords) {
		try {
			processorTx.doUpdate(context);
		} catch (Exception e) {
			log.error("Inner error", e);
			return null;
		}
		if (records.size() > 0) {
			processedCountRecords += records.size();
			variable.setProcessedCountRecords(processedCountRecords);
			variable.setLastProcessedRegistryRecord(records.get(records.size() - 1).getId());
			processRegistryVariableInstanceService.update(variable);
		}
		return processedCountRecords;
	}

	@Transactional(readOnly = false)
	private boolean endProcessing(@NotNull Stub<Registry> stub) {
		try {
			Registry registry = registryService.read(stub);

			registryWorkflowManager.setNextSuccessStatus(registry);
			registryWorkflowManager.endProcessing(registry);

			return true;
		} catch (TransitionNotAllowed transitionNotAllowed) {
			log.error("Inner error", transitionNotAllowed);
		}
		return false;
	}

	private void printWatch() {
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
	public void setHandleError(HandleError handleError) {
		this.handleError = handleError;
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
	public void setProcessRecordsRange(ProcessRecordsRangeActionHandler processRecordsRange) {
		this.processRecordsRange = processRecordsRange;
	}

	@Required
	public void setProcessRegistryVariableInstanceService(ProcessRegistryVariableInstanceService processRegistryVariableInstanceService) {
		this.processRegistryVariableInstanceService = processRegistryVariableInstanceService;
	}

	@Required
	public void setProcessorTx(ServiceProviderFileProcessorTx processorTx) {
		this.processorTx = processorTx;
	}
}
