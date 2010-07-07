package org.flexpay.eirc.process.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.eirc.service.ProcessRegistryVariableInstanceService;
import org.flexpay.eirc.service.importexport.ProcessRegistryService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProcessRegistryJob extends Job {

	private RegistryService registryService;
	private RegistryFileService registryFileService;
	private ProcessRegistryVariableInstanceService processRegistryVariableInstanceService;
	private ProcessRegistryService processRegistryService;
	private HandleError handleError;

	private StopWatch processRegistryRecordRangeWatch = new StopWatch();

	{
		processRegistryRecordRangeWatch.start();
		processRegistryRecordRangeWatch.suspend();
	}

	@SuppressWarnings ({"unchecked"})
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

		List<Long> recordIds = (List<Long>) parameters.get(StartRegistryProcessingActionHandler.RECORD_IDS);

		FetchRange range = (FetchRange) parameters.get(HasMoreRecordActionHandler.RANGE);
		ProcessRegistryVariableInstance variable = processRegistryVariableInstanceService.findVariable(getProcessId());

		if (variable != null && variable.getLastProcessedRegistryRecord() != null && recordIds == null) {

			range = registryFileService.getFetchRangeForProcessing(Stub.stub(registry), range.getPageSize(), variable.getLastProcessedRegistryRecord());

		} if (variable != null && variable.getLastProcessedRegistryRecord() != null && recordIds != null) {

			int idx = recordIds.indexOf(variable.getLastProcessedRegistryRecord());
			if (idx < 0) {
				log.warn("Registry record with id={} did not find", variable.getLastProcessedRegistryRecord());
			} else {
				recordIds = recordIds.subList(idx + 1, recordIds.size() - 1);
			}
		} else if (variable == null) {

			variable = new ProcessRegistryVariableInstance();
			variable.setRegistry(registry);
			variable.setProcessId(getProcessId());
		}

		ProcessingContext context = processRegistryService.prepareContext(registry);

		try {
			variable = prepare(variable, context);
		} catch (Throwable t) {
			setError(context, t);
			return RESULT_ERROR;
		}

		if (variable == null) {
			return RESULT_NEXT;
		}

		if (recordIds != null) {
			if (!processing(recordIds, range.getPageSize(), variable, context)) return  RESULT_ERROR;
		} else {
			if (!processing(range, variable, context)) return RESULT_ERROR;
		}

		if (!processRegistryService.endProcessing(context)) return RESULT_ERROR;

		return RESULT_NEXT;
	}

	public boolean processing(@NotNull FetchRange range, @NotNull ProcessRegistryVariableInstance variable, @NotNull ProcessingContext context) {
		try {
			do {
				processRegistryRecordRangeWatch.resume();
				try {
					if (processRegistryService.processRegistryRecordRange(range, variable, context)) return false;
				} finally {
					processRegistryRecordRangeWatch.suspend();
				}
				range.nextPage();
				context.nextOperation();

				log.debug("Processed count records: {}", variable.getProcessedCountRecords());
				printWatch();
			} while (range.hasMore());

		} catch (Throwable t) {
			setError(context, t);
		}
		
		return false;
	}

	private ProcessRegistryVariableInstance prepare(ProcessRegistryVariableInstance variable, ProcessingContext context) throws Exception {
		try {
			if (variable.isNew()) {
				variable = processRegistryService.prepare(context, variable);
			}
			return variable;
		} catch (TransitionNotAllowed e) {
			log.debug("Inner error {}", e);
		}
		return null;
	}

	public boolean processing(@NotNull List<Long> records, int flushCount, @NotNull ProcessRegistryVariableInstance variable, @NotNull ProcessingContext context) {
		try {
			while (records.size() > 0) {
				if (records.size() < flushCount) {
					flushCount = records.size();
				}
				List<Long> currentRecordProcessed = records.subList(0, flushCount - 1);

				processRegistryRecordRangeWatch.resume();
				try {
					if (processRegistryService.processRegistryRecordEnumeration(currentRecordProcessed, variable, context)) return false;
				} finally {
					processRegistryRecordRangeWatch.suspend();
				}
				context.nextOperation();

				if (flushCount == records.size()) {
					records.clear();
				} else {
					records = records.subList(flushCount, records.size() - 1);
				}

				log.debug("Processed count records: {}", variable.getProcessedCountRecords());
				printWatch();

			}

			return true;
		} catch (Throwable t) {
			setError(context, t);
		}

		return false;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private void setError(ProcessingContext context, Throwable t) {
		try {
			handleError.handleError(t, context);
		} catch (Exception e) {
			log.error("Inner error", e);
		}
	}

	private void printWatch() {
		log.debug("Time process registry record range: {}", processRegistryRecordRangeWatch);
		processRegistryService.printWatch();
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryFileService(RegistryFileService registryFileService) {
		this.registryFileService = registryFileService;
	}

	@Required
	public void setProcessRegistryVariableInstanceService(ProcessRegistryVariableInstanceService processRegistryVariableInstanceService) {
		this.processRegistryVariableInstanceService = processRegistryVariableInstanceService;
	}

	@Required
	public void setProcessRegistryService(ProcessRegistryService processRegistryService) {
		this.processRegistryService = processRegistryService;
	}

	@Required
	public void setHandleError(HandleError handleError) {
		this.handleError = handleError;
	}
}
