package org.flexpay.common.process.dao;

import org.apache.commons.collections.map.LRUMap;
import org.drools.definition.KnowledgePackage;
import org.drools.event.process.*;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.flexpay.common.process.audit.ProcessInstanceDbLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.process.instance.impl.ProcessInstanceImpl;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.map;

public class ProcessJbpmDaoImpl implements ProcessJbpmDao {

	private static final Logger log = LoggerFactory.getLogger(ProcessJbpmDaoImpl.class);

	private StatefulKnowledgeSession session;

	private ProcessInstanceDbLog processInstanceDbLog;

	//private ProcessStats processStats = new ProcessStats();
	private ThreadLocal<Boolean> statsRegistered = new ThreadLocal<Boolean>();

	private StatefulKnowledgeSession getSession() {
		return session;
	}

	public void registerEventListeners() {
		if (!statsRegistered.get()) {
			//registerEventListener(processStats);
		}
	}

	@Override
	public List<org.drools.definition.process.Process> getProcesses() {
		List<org.drools.definition.process.Process> result = new ArrayList<org.drools.definition.process.Process>();
		for (KnowledgePackage kpackage: getSession().getKnowledgeBase().getKnowledgePackages()) {
			result.addAll(kpackage.getProcesses());
		}
		return result;
	}

	@Override
	public org.drools.definition.process.Process getProcess(String processId) {
		return getSession().getKnowledgeBase().getProcess(processId);
		/*
		for (KnowledgePackage kpackage: getSession().getKnowledgeBase().getKnowledgePackages()) {
			for (org.drools.definition.process.Process process: kpackage.getProcesses()) {
				if (processId.equals(process.getId())) {
					return process;
				}
			}
		}
		return null;
		*/
	}

	@Override
	public org.drools.definition.process.Process getProcessByName(String name) {
		for (KnowledgePackage kpackage: getSession().getKnowledgeBase().getKnowledgePackages()) {
			for (org.drools.definition.process.Process process: kpackage.getProcesses()) {
				if (name.equals(process.getName())) {
					return process;
				}
			}
		}
		return null;
	}

	@Override
	public void addPackages(Collection<KnowledgePackage> packages) {
		session.getKnowledgeBase().addKnowledgePackages(packages);
	}

	@Override
	public void removeProcess(String processId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ProcessInstanceLog> getProcessInstanceLogs() {
		return processInstanceDbLog.findProcessInstances();
	}

	@Override
	public ProcessInstanceLog getProcessInstanceLog(long processInstanceId) {
		log.debug("Execute getProcessInstanceLog");
		return processInstanceDbLog.findProcessInstance(processInstanceId);
	}

	@Override
	public List<ProcessInstanceLog> getProcessInstanceLogsByProcessId(String processId) {
		return processInstanceDbLog.findProcessInstances(processId);
	}

	@Override
	public ProcessInstance startProcess(final String processId, final Map<String, Object> parameters) {

//		final Authentication auth = SecurityUtil.getAuthentication();
//		Thread processThread = new Thread(new Runnable() {
//			public void run() {
//				SecurityContextHolder.getContext().setAuthentication(auth);
//				getSession().startProcess(processId, parameters);
//			}
//		});
//		processThread.start();
//		Long processThreadId = processThread.getId();
//		try {
//			while (!processThread.getState().equals(Thread.State.TERMINATED)) {
//				log.debug("Waiting result process thread {}", processThreadId);
//				Thread.sleep(100);
//				log.debug("Wake up process thread {}", processThreadId);
//
//				ProcessInstance processInstance = processStats.getProcessInstance(processThreadId);
//				if (processInstance != null) {
//					log.debug("Got result process thread {}: process instance id {}", processThreadId, processInstance);
//					return processInstance;
//				}
				/*
				if (resultProcessInstanceId != null) {
					//ProcessInstance processInstance2 = getSession().getProcessInstance(resultProcessInstanceId);
					//log.debug("Process instance: {}", processInstance2);

					try {
						processInstance = processInstanceDbLog.findProcessInstance(resultProcessInstanceId);
					} catch (NoResultException e) {
						log.debug("Can not get process instance log {} (thread {})", resultProcessInstanceId, processThreadId);
					}
				}
				*/
//			}
//		} catch (InterruptedException e) {
//			log.warn("Process thread {} interrupted", processThreadId);
//		}
		return getSession().startProcess(processId, parameters);
	}

	@Override
	public void abortProcessInstance(long processInstanceId) {
		ProcessInstanceImpl processInstance = (ProcessInstanceImpl)getSession().getProcessInstance(processInstanceId);
		if (processInstance != null) {
			getSession().abortProcessInstance(processInstanceId);
		} else {
			throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
		}
	}

	@Override
	public Map<String, Object> getProcessInstanceVariables(long processInstanceId) {
		ProcessInstanceImpl processInstance = (ProcessInstanceImpl)getSession().getProcessInstance(processInstanceId);
		if (processInstance != null) {
		    Map<String, Object> variables =
		        ((WorkflowProcessInstanceImpl) processInstance).getVariables();
            if (variables == null) {
				return new HashMap<String, Object>();
			}
			// filter out null values
			Map<String, Object> result = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry: variables.entrySet()) {
				if (entry.getValue() != null) {
					result.put(entry.getKey(), entry.getValue());
				}
			}
			return result;
		} else
		if (getProcessInstanceLog(processInstanceId) != null) {
			Map<String, Object> variables = map();
			for (VariableInstanceLog variableInstanceLog : processInstanceDbLog.findVariableInstances(processInstanceId)) {
				variables.put(variableInstanceLog.getVariableId(), variableInstanceLog.getValue());
			}
			return variables;
		}
		throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
	}

	@Override
	public void setProcessInstanceVariables(long processInstanceId, Map<String, Object> variables) {
		ProcessInstanceImpl processInstance = (ProcessInstanceImpl)getSession().getProcessInstance(processInstanceId);
		if (processInstance != null) {
			VariableScopeInstance variableScope = (VariableScopeInstance)
				((org.jbpm.process.instance.ProcessInstance) processInstance)
					.getContextInstance(VariableScope.VARIABLE_SCOPE);
			if (variableScope == null) {
				throw new IllegalArgumentException(
					"Could not find variable scope for process instance " + processInstanceId);
			}
			for (Map.Entry<String, Object> entry: variables.entrySet()) {
				variableScope.setVariable(entry.getKey(), entry.getValue());
			}
		} else {
			throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
		}
	}

	@Override
	public void signalExecution(long executionId, String signal) {
		getSession().signalEvent("signal", signal, executionId);
	}

	@Override
	public void messageExecution(long executionId, String messageName, String messageValue) {
		getSession().signalEvent(messageName, messageValue, executionId);
	}

	@Override
	public void registerEventListener(ProcessEventListener listener) {
		getSession().addEventListener(listener);
	}

	public class ProcessStats implements ProcessEventListener {

		@SuppressWarnings ({"unchecked"})
		private Map processInstanceIdsByThreadId = Collections.synchronizedMap(new LRUMap(1000));

		public ProcessInstance getProcessInstance(Long threadId) {
			return (ProcessInstance)processInstanceIdsByThreadId.get(threadId);
		}

		@SuppressWarnings ({"unchecked"})
		@Override
		public void beforeProcessStarted(ProcessStartedEvent event) {
			log.debug("Added to result from process thread {} process instance {}",
					Thread.currentThread().getId(), event.getProcessInstance());
			processInstanceIdsByThreadId.put(Thread.currentThread().getId(), event.getProcessInstance());
		}

		@Override
		public void afterProcessStarted(ProcessStartedEvent event) {
		}

		@Override
		public void beforeProcessCompleted(ProcessCompletedEvent event) {

		}

		@Override
		public void afterProcessCompleted(ProcessCompletedEvent event) {

		}

		@Override
		public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
			log.debug("Before Triggered: {}", event);
			printStack();
		}

		@Override
		public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
			log.debug("After Triggered: {}", event);
			printStack();
		}

		@Override
		public void beforeNodeLeft(ProcessNodeLeftEvent event) {

		}

		@Override
		public void afterNodeLeft(ProcessNodeLeftEvent event) {

		}

		@Override
		public void beforeVariableChanged(ProcessVariableChangedEvent event) {

		}

		@Override
		public void afterVariableChanged(ProcessVariableChangedEvent event) {

		}

		private void printStack() {
			StringBuilder logBuilder = new StringBuilder();
			for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
				logBuilder.append(element).append("\n");
			}
			log.debug("Stack: {}", logBuilder.toString());
		}
	}

	@Required
	public void setProcessInstanceDbLog(ProcessInstanceDbLog processInstanceDbLog) {
		this.processInstanceDbLog = processInstanceDbLog;
	}

	@Required
	public void setSession(StatefulKnowledgeSession session) {
		this.session = session;
		statsRegistered.set(false);
	}
}
