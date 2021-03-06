package org.flexpay.common.process;

import org.apache.commons.lang.ArrayUtils;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.process.audit.WorkItemCompleteLocker;
import org.flexpay.common.process.dao.ProcessJbpmDao;
import org.flexpay.common.process.dao.WorkItemDao;
import org.flexpay.common.process.dao.WorkItemHandlerDao;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.handler.HumanTaskHandler;
import org.flexpay.common.process.persistence.ProcessDefinition;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.DateUtil.now;

/**
 * ProcessInstance manager allows to create and maintain processes life cycle
 */
@Transactional (readOnly = true)
public class ProcessManagerImpl implements ProcessManager, ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(ProcessManagerImpl.class);

	private static final String HUMAN_TASK_NAME = "Human Task";
	private static final String PROCESS_DEFINITION_VERSION_ID = "PROCESS_DEFINITION_VERSION_ID";

		/**
	 * singleton instance
	 */
	private static final ProcessManagerImpl instance = new ProcessManagerImpl();

	private volatile boolean started = false;

	private ApplicationContext applicationContext;

	private ProcessJbpmDao delegate;

	private WorkItemDao workItemDao;

	private WorkItemHandlerDao workItemHandlerDao;

	private ProcessDefinitionManager processDefinitionManager;

	/**
	 * protected constructor
	 */
	private ProcessManagerImpl() {
		log.debug("ProcessManager constructor called");
	}

	public static ProcessManagerImpl getInstance() {
		return instance;
	}


	public void start() {

		synchronized (instance) {

			if (instance.isStarted()) {
				return;
			}

			log.debug("Starting ProcessManager");
			registerWorkItemHandlers();
			instance.setStarted(true);
		}
		log.debug("ProcessManager started");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProcessInstance startProcess(@NotNull String definitionId, @Nullable Map<String, Object> parameters)
			throws ProcessInstanceException, ProcessDefinitionException {
		return startProcess(definitionId, parameters, null);
	}

	@Override
	public ProcessInstance startProcess(@NotNull String definitionId, @Nullable Map<String, Object> parameters, @Nullable Long processDefinitionVersion)
			throws ProcessInstanceException, ProcessDefinitionException {

		log.debug("Start process (definitionId={}, parameters={}, version={})", new Object[]{definitionId, parameters, processDefinitionVersion});

		if (processDefinitionVersion != null) {
			processDefinitionManager.deployProcessDefinition(definitionId, processDefinitionVersion);
		} else {
			processDefinitionManager.deployProcessDefinition(definitionId);
		}

		ProcessDefinition definition = processDefinitionManager.getProcessDefinition(definitionId);
		if (definition == null) {
			throw new ProcessDefinitionException("Process definition for name '" + definitionId + "' not found!", "error.common.pm.pd_not_found", definitionId);
		}
		if (parameters == null) {
			parameters = CollectionUtils.map();
		}
		parameters.put(PROCESS_DEFINITION_VERSION_ID, definition.getVersion());
		parameters.put(PARAM_SECURITY_CONTEXT, SecurityUtil.getAuthentication());
		org.drools.runtime.process.ProcessInstance processInstance = delegate.startProcess(definitionId, parameters);
        log.debug("Started process {} (definitionId={}, parameters={}, version={})", new Object[]{processInstance.getId(), definitionId, parameters, processDefinitionVersion});

        try {
            log.debug("Start sleep: {}", processInstance.getId());
            Thread.sleep(10000);
            log.debug("End sleep: {}", processInstance.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return processInstance(processInstance, definition.getVersion());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endProcess(@NotNull ProcessInstance process) {
		delegate.abortProcessInstance(process.getId());
	}

	@Override
	public List<ProcessInstance> getProcesses() {
		return getProcesses(null);
	}

	@Override
	public List<ProcessInstance> getProcesses(Page<ProcessInstance> pager) {
		return getProcesses(null, pager, null, null, null, null);
	}

	@Override
	public List<ProcessInstance> getProcesses(ProcessSorter processSorter, Page<ProcessInstance> pager, Date startFrom, Date endBefore,
											  ProcessInstance.STATE state, String name) {
		List<ProcessInstanceLog> processInstanceLogs = delegate.getProcessInstances(processSorter, pager, startFrom, endBefore, state, name);

		if (processInstanceLogs.isEmpty()) {
			return Collections.emptyList();
		}

		List<ProcessInstance> result = list();

		for (ProcessInstanceLog processInstanceLog : processInstanceLogs) {
			result.add(refactorProcessInstance(processInstanceLog));
		}

		return result;
	}

	/**
	 * Wait for process completion
	 *
	 * @param processId ProcessInstance id
	 * @throws InterruptedException if waiting thread is interrupted
	 */
	@Override
	public void join(long processId) throws InterruptedException {
		while (true) {

			ProcessInstance info = getProcessInstance(processId);
			if (info == null || info.getId() != processId) {
				return;
			}

			// wait until there is any
			/*
			synchronized (sleepSemaphore) {

				ProcessState state = info.getProcessState();
				if (state.isCompleted()) {
					return;
				}

				sleepSemaphore.wait(5000);
			}
			*/
		}
	}

	/**
	 * Delete process instance
	 *
	 * @param process ProcessInstance to delete
	 */
	@Override
	public void deleteProcessInstance(@NotNull ProcessInstance process) {

	}

	/**
	 * Delete several process instances
	 *
	 * @param processes Processes to delete
	 */
	@Override
	public void deleteProcessInstances(List<ProcessInstance> processes) {

	}

	/**
	 * Delete several process instances
	 *
	 * @param processIds ProcessInstance instances identifiers to delete
	 */
	@Override
	public void deleteProcessInstances(final Set<Long> processIds) {

	}

    @Transactional(readOnly = false)
	@Override
	public void deleteProcessInstances(DateRange range, String processDefinitionName) {

		delegate.deleteFinishedProcessInstances(range, processDefinitionName);
	}

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public ProcessInstance getProcessInstance(@NotNull Long instanceId) {

		return refactorProcessInstance(delegate.getProcessInstanceLog(instanceId));
	}

	@NotNull
	@Override
	public List<ProcessInstance> getProcessInstances() {

		List<ProcessInstance> result = list();
		for (ProcessInstanceLog processInstanceLog : delegate.getProcessInstanceLogs()) {
			result.add(refactorProcessInstance(processInstanceLog));
		}
		return result;
	}

	private ProcessInstance refactorProcessInstance(ProcessInstanceLog processInstanceLog) {
		if (processInstanceLog == null) {
			return null;
		}
		Map<String, Object> variables = getInstanceData(processInstanceLog.getProcessInstanceId());

		Long processDefinitionVersionId = getProcessDefinitionVersion(variables);
		ProcessInstance processInstance = processInstance(processInstanceLog, processDefinitionVersionId);
		processInstance.setParameters(variables);
		return processInstance;
	}

	@Override
	public List<ProcessInstance> getProcessInstances(@NotNull String definitionId) {
        WorkItemCompleteLocker.lock();
        List<ProcessInstance> result = new ArrayList<ProcessInstance>();
        try {
            List<ProcessInstanceLog> processInstances = delegate.getProcessInstanceLogsByProcessId(definitionId);
            for (ProcessInstanceLog processInstance : processInstances) {
                if (processInstance.getEnd() == null) {
                    Map<String, Object> variables = delegate.getProcessInstanceVariables(processInstance.getProcessInstanceId());
                    Long processDefinitionVersionId = getProcessDefinitionVersion(variables);
                    result.add(processInstance(processInstance, processDefinitionVersionId));
                }
		    }
        } finally {
            WorkItemCompleteLocker.unlock();
        }
		return result;
	}

	public void setProcessState(long instanceId, ProcessInstance.STATE nextState) {
		if (nextState == ProcessInstance.STATE.ENDED) {
			delegate.abortProcessInstance(instanceId);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public Map<String, Object> getInstanceData(long instanceId) {
        WorkItemCompleteLocker.lock();
        try {
		    return delegate.getProcessInstanceVariables(instanceId);
        } finally {
            WorkItemCompleteLocker.unlock();
        }
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isStarted() {
		return started;
	}

	private void setInstanceData(long instanceId, Map<String, Object> data) {
		delegate.setProcessInstanceVariables(instanceId, data);
	}

	@Override
	public void signalExecution(@NotNull ProcessInstance execution, String signal) {
		delegate.signalExecution(execution.getId(), signal);

        try {
            log.debug("Start sleep signalExecution: {}", execution.getId());
            Thread.sleep(10000);
            log.debug("End sleep signalExecution: {}", execution.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void messageExecution(@NotNull ProcessInstance execution, @NotNull String messageName, @NotNull String messageValue) {
		delegate.messageExecution(execution.getId(), messageName, messageValue);

        try {
            log.debug("Start sleep messageExecution: {}", execution.getId());
            Thread.sleep(10000);
            log.debug("End sleep messageExecution: {}", execution.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Check human task execute in process
	 *
	 * @param processInstance Process instance
	 * @return <code>true</code> if current task is human task  or <code>false</code> otherwise
	 */
	@Override
	public boolean isHumanTaskExecute(@NotNull ProcessInstance processInstance) {
		HumanTaskHandler handler = getHumanWorkItemHandler();
		if (handler != null) {
			for (Long processInstanceId : handler.getWorkItems().keySet()) {
				log.debug("Human handler process instance id: {}", processInstanceId);
			}
		}
		log.debug("Check process instance id={}", processInstance.getId());
		return handler != null && handler.getWorkItems().containsKey(processInstance.getId());
	}

	/**
	 * Complete human task from actor
	 *
	 *
	 * @param processInstance Process instance
	 * @param actorId		 Actor id
	 * @param result
	 * @return <code>true</code> if actor assigned to human task and execute human task, otherwise <code>false</code>
	 */
	@Override
	synchronized public boolean completeHumanTask(@NotNull ProcessInstance processInstance, @NotNull String actorId, @Nullable String result) {
		HumanTaskHandler handler = getHumanWorkItemHandler();
		log.debug("Human task handler: {}", handler);
		if (handler != null && handler.getWorkItems().containsKey(processInstance.getId())) {
			WorkItem workItem = handler.getWorkItems().get(processInstance.getId());

			String processActorsString = (String) workItem.getParameter("ActorId");
			log.debug("Actors: {}", processActorsString);
			log.debug("Parameters: {}", workItem.getParameters());

			String[] processActors = processActorsString.split(",");

			Map<String, Object> results = null;
			if (result != null) {
				results = map("Result", (Object)result);
			}

			if (ArrayUtils.contains(processActors, actorId)) {
				log.debug("Complete work item: {}", workItem.getId());
				handler.getWorkItems().remove(processInstance.getId());
				workItemDao.completeWorkItem(workItem.getId(), results);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void registerWorkItemHandlers() {

		log.debug("Register work item handlers");

		Map<String, WorkItemHandler> workItemHandlers = applicationContext.getBeansOfType(WorkItemHandler.class);

		Map<String, WorkItem> workItemsWaiting = map();
        try {
            for (WorkItem workItem : workItemDao.getWorkItemsWaiting()) {
                workItemsWaiting.put(workItem.getName(), workItem);
            }
        } catch (Throwable th) {
            log.warn("Can not get waiting work items. Continue register work item handlers...", th);
        }

		for (Map.Entry<String, WorkItemHandler> workItemHandlerEntry : workItemHandlers.entrySet()) {

			List<String> workItemHandlerNames = list();
			String[] aliases = applicationContext.getAliases(workItemHandlerEntry.getKey());

			if (aliases != null && aliases.length > 0) {
				org.apache.commons.collections.CollectionUtils.addAll(workItemHandlerNames, aliases);
			} else {
				workItemHandlerNames.add(workItemHandlerEntry.getKey());
			}

			workItemHandlerDao.registerWorkItemHandler(workItemHandlerEntry.getValue(), workItemHandlerNames);

			for (String workItemHandlerName : workItemHandlerNames) {
				if (workItemsWaiting.containsKey(workItemHandlerName)) {
					WorkItem workItem = workItemsWaiting.get(workItemHandlerName);
					workItemDao.executeWorkItem(workItemHandlerEntry.getValue(), workItem);
					log.debug("execute waiting work item: {}", workItem);
				}
			}
		}
	}

	@Nullable
	private HumanTaskHandler getHumanWorkItemHandler() {
		Map<String, org.drools.runtime.process.WorkItemHandler> workItemHandlers = workItemHandlerDao.getWorkItemHandlers();
		log.debug("Work item handlers: {}", workItemHandlers.keySet());
		if (workItemHandlers.containsKey(HUMAN_TASK_NAME)) {
			org.drools.runtime.process.WorkItemHandler workItemHandler = workItemHandlers.get(HUMAN_TASK_NAME);
			if (workItemHandler instanceof HumanTaskHandler) {
				return (HumanTaskHandler) workItemHandler;
			}
			log.warn("Human item handler ('{}') do not instance of {}", HUMAN_TASK_NAME, HumanTaskHandler.class);
		} else {
			log.warn("Human item handler ('{}') did not registry", HUMAN_TASK_NAME);
		}
		return null;
	}

	private static ProcessInstance processInstance(ProcessInstanceLog processInstance, Long version) {
		log.debug("Process instance log: {}, start date {}, end date {}, version {}",
				new Object[]{processInstance, processInstance.getStart(), processInstance.getEnd(), version});
		ProcessInstance result = new ProcessInstance(
				processInstance.getProcessInstanceId(),
				processInstance.getProcessId(),
				processInstance.getStart(),
				processInstance.getEnd(),
				false);
		result.setProcessDefenitionVersion(version);
        File logFile = ProcessLogger.getLogFile(processInstance.getId());
		if (logFile.exists()) {
			result.setLogFileName(logFile.getAbsolutePath());
		} else {
			result.setLogFileName("");
		}
		/*
		TokenReference token = new TokenReference(
				processInstance.getProcessInstanceId() + "", null, "");
		result.setRootToken(token);
		*/
		return result;
	}

	private static ProcessInstance processInstance(org.drools.runtime.process.ProcessInstance processInstance, Long version) {
		ProcessInstance result = new ProcessInstance(
				processInstance.getId(),
				processInstance.getProcessId(),
				now(),
				null,
				false);
		result.setProcessDefenitionVersion(version);
		/*
		switch (processInstance.getState()) {
			case 0:
				result.setState(ProcessInstance.STATE.PENDING);
				break;
			case 1:
				result.setState(ProcessInstance.STATE.RUNNING);
				break;
			case 2:
				result.setState(ProcessInstance.STATE.ENDED);
				break;
			case 3:
				result.setState(ProcessInstance.STATE.ABORTED);
				break;
			case 4:
				result.setState(ProcessInstance.STATE.SUSPENDED);
				break;
			default:
				break;
		}
		*/
		/*
		TokenReference token = new TokenReference(
				processInstance.getProcessInstanceId() + "", null, "");
		result.setRootToken(token);
		*/
		return result;
	}

	private Long getProcessDefinitionVersion(Map<String, Object> variables) {
		Object pdv = variables.get(PROCESS_DEFINITION_VERSION_ID);
		if (pdv == null) {
			log.warn("Did not find process definition version from process instance variable");
		} else if (pdv instanceof String) {
			try{
				return Long.valueOf((String)pdv);
			} catch (NumberFormatException e) {
				log.warn("Did not parse process definition version '{}': number format exception", pdv);
			}
		} else if (pdv instanceof Long) {
			return (Long)pdv;
		} else {
			log.warn("Can not get process definition version from process instance variable: {}. Class is {}, but need String or Long.", pdv, pdv.getClass());
		}
		return -1L;
	}

	@Required
	public void setDelegate(ProcessJbpmDao delegate) {
		this.delegate = delegate;
	}

	@Required
	public void setWorkItemDao(WorkItemDao workItemDao) {
		this.workItemDao = workItemDao;
	}

	@Required
	public void setWorkItemHandlerDao(WorkItemHandlerDao workItemHandlerDao) {
		this.workItemHandlerDao = workItemHandlerDao;
	}

	@Required
	public void setProcessDefinitionManager(ProcessDefinitionManager processDefinitionManager) {
		this.processDefinitionManager = processDefinitionManager;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
