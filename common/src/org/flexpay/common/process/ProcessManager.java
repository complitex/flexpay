package org.flexpay.common.process;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.job.JobManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.db.GraphSession;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Process manager allows to create and maintain processes lyfecycle
 */
public class ProcessManager implements Runnable {

	/**
	 * Loggger
	 */
	private static final Logger log = Logger.getLogger(ProcessManager.class);

	/**
	 * singleton instance
	 */
	protected volatile static ProcessManager instance;
	private volatile static Thread localThread;
	private volatile boolean stopped = false;
	private final Object sleepSemaphore = new Object();

	/**
	 * Set of task instance ids currently in progress
	 */
	private final Set<Long> runningTasks = CollectionUtils.set();

	private int rescanFrequency = 10000;

	/**
	 * Limit number of task restarts
	 */
	private int taskRepeatLimit = 10;

	private JbpmConfiguration jbpmConfiguration = null;

	/**
	 * Predefined set of paths where to lookup definitions if not already deployed
	 */
	private final List<String> definitionPaths = CollectionUtils.list();

	/**
	 * LyfecycleVoters
	 */
	private List<LyfecycleVoter> lyfecycleVoters = CollectionUtils.list();

	/**
	 * protected constructor
	 */
	protected ProcessManager() {
		log.debug("ProcessManager constructor called ");
	}

	/**
	 * @return ProcessManager instance
	 */
	public synchronized static ProcessManager getInstance() {
		if (instance == null) {
			instance = new ProcessManager();
		}
		return instance;
	}

	/**
	 * Start ProcessManager Thread
	 */
	public static synchronized void startProcessManager() {
		log.debug("Starting ProcessManager thread");
		if (localThread == null || !localThread.isAlive()) {
			ProcessManager pmInstance = getInstance();
			localThread = new Thread(pmInstance, "ProcessManager Thread");
			localThread.start();
			log.debug("ProcessManager thread started");
		} else {
			log.debug("ProcessManager thread already started");
		}
	}

	/**
	 * Unload process manager and stop process manager thread
	 */
	public static synchronized void unload() {
		log.debug("Stoping ProcessManager thread");
		if (instance != null && localThread != null) {
			instance.stopProcessManager();
			try {
				localThread.join();
			} catch (InterruptedException e) {
				log.debug("can't stop ProcessManager thread");
			}
			instance = null;
			localThread = null;
		}
		log.debug("ProcessManager thread stoped ");
	}

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @param replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deplot process definition to jbpm
	 */
	public long deployProcessDefinition(String name, boolean replace)
			throws ProcessDefinitionException {

		if (log.isDebugEnabled()) {
			log.debug("Requested definition deployment: " + name);
		}
		InputStream is = null;
		try {
			for (String path : definitionPaths) {
				String resource = path + "/" + name + ".xml";
				if (log.isDebugEnabled()) {
					log.debug("Looking up " + resource);
				}
				is = ApplicationConfig.getResourceAsStream(resource);
				if (is != null) {
					log.debug("Found!");
					return deployProcessDefinition(is, replace);
				}
			}

			log.warn("No definition found: " + name);
			throw new ProcessDefinitionException("Process definition for name " + name + " file not found!");
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Deploys process definition to jbpm from inputStream
	 *
	 * @param in	  input stream with process definition
	 * @param replace replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deplot process definition to jbpm
	 */
	public long deployProcessDefinition(InputStream in, boolean replace) throws ProcessDefinitionException {
		ProcessDefinition processDefinition = null;
		try {
			processDefinition = ProcessDefinition.parseXmlInputStream(in);
			return deployProcessDefinition(processDefinition, replace);
		} catch (Exception e) {
			log.error("deployProcessDefinition: ", e);
			if (processDefinition == null) {
				throw new ProcessDefinitionException("InputStream is not process definition file");
			} else {
				throw new ProcessDefinitionException("Can't deploy processDefinition for " + processDefinition.getName());
			}
		}
	}

	/**
	 * Delpoys parsed process definition to jbpm
	 *
	 * @param processDefinition parsed process definition
	 * @param replace		   replace replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 */
	public long deployProcessDefinition(ProcessDefinition processDefinition, boolean replace) {

		//create JbpmContext and open transaction
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();

		GraphSession graphSession = jbpmContext.getGraphSession();
		ProcessDefinition latestProcessDefinition = graphSession.findLatestProcessDefinition(processDefinition.getName());

		int newVersion;

		if (replace || (latestProcessDefinition == null)) {
			if (latestProcessDefinition == null) {
				log.info("Process definition not found. Deploying " + processDefinition.getName());
				newVersion = 1;
				processDefinition.setVersion(newVersion);
			} else {
				int oldVersion = latestProcessDefinition.getVersion();
				log.info("Deploying new version of process definition " + processDefinition.getName());
				newVersion = oldVersion + 1;
				processDefinition.setVersion(newVersion);
				log.info("Old version = " + oldVersion + ", New version = " + newVersion);
			}
			graphSession.saveProcessDefinition(processDefinition);
			log.info("Deployed.");
		}
		//close JbpmContext and commit transaction
		jbpmContext.close();
		return processDefinition.getId();
	}

	/**
	 * main loop
	 */
	@SuppressWarnings ({"ConstantConditions"})
	public void run() {
		log.debug("Starting process manager...");
		while (!isStopped()) {
			try {
				//write tick-tack message
				log.trace("Collecting task instances to run.");
				//find not runningTasks task instances and run
				JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
				for (Object o : jbpmContext.getTaskMgmtSession().findTaskInstances(JobManagerAssignmentHandler.JOB_MANAGER_ACTOR_NAME)) {
					// are there any not runningTasks taskInstances?
					TaskInstance taskInstance = (TaskInstance) o;
					taskInstance = jbpmContext.getTaskMgmtSession().loadTaskInstance(taskInstance.getId());
//                    if (!taskInstance.hasEnded() || (runningTasks.get(taskInstance.getId()) != null)) {
					if (!isTaskExecuting(taskInstance.getId())) {
						startTask(taskInstance);
					}
				}
				jbpmContext.close();
				//all task instances started, going to sleep
				synchronized (sleepSemaphore) {
					try {
						sleepSemaphore.wait(rescanFrequency);
						sleepSemaphore.notifyAll();
					} catch (InterruptedException e) {
						log.debug("Somebody interrupts me.", e);
						Thread.interrupted(); // clear "interrupted" state of thread
					}
				}
			} catch (Throwable e) {
				log.debug("ProcessManager mainLoop : ", e);
			}
		}
		log.debug("process manager stoped...");
	}

	/**
	 * Stops ProcessManager execution
	 */
	public synchronized void stopProcessManager() {
		stopped = true;
	}

	/**
	 * @return true if ProcessManager is not running
	 */
	public synchronized boolean isStopped() {
		return stopped;
	}

	/**
	 * Init process by process definition name
	 *
	 * @param processDefinitionName name of process definition
	 * @return ID of process instance
	 * @throws ProcessDefinitionException when process definition has an error
	 * @throws ProcessInstanceException   when jbpm can't instanciate process from process definition
	 */
	private synchronized Long initProcess(String processDefinitionName)
			throws ProcessDefinitionException, ProcessInstanceException {

		ProcessDefinition processDefinition;
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		try {
			processDefinition = graphSession.findLatestProcessDefinition(processDefinitionName);
		} catch (RuntimeException e) {
			jbpmContext.close();
			log.error("findLatestProcessDefinition", e);
			throw new ProcessDefinitionException("Can't access ProcessDefinition for " + processDefinitionName, e);
		}
		// try to search in predefined set of places
		if (processDefinition == null) {
			deployProcessDefinition(processDefinitionName, true);
			jbpmContext.close();
			jbpmContext = jbpmConfiguration.createJbpmContext();
			graphSession = jbpmContext.getGraphSession();
			processDefinition = graphSession.findLatestProcessDefinition(processDefinitionName);
		}
		if (processDefinition == null) {
			log.error("Can't find process definition for name: " + processDefinitionName);
			throw new ProcessDefinitionException("Can't find process definition for name: " + processDefinitionName);
		}

		if (log.isInfoEnabled()) {
			log.info("Initializing  process. Process Definition id = " + processDefinition.getId() +
					 " name = " + processDefinition.getName() + " version = " + processDefinition.getVersion());
		}

		try {
			ProcessInstance processInstance = new ProcessInstance(processDefinition);
			return processInstance.getId();
		} catch (RuntimeException e) {
			log.error("ProcessInstanceCreation", e);
			throw new ProcessInstanceException("Can't create ProcessInstance for " + processDefinitionName, e);
		} finally {
			jbpmContext.close();
		}
	}

	/**
	 * Return process context parameters
	 *
	 * @param processID process ID
	 * @return HashMap with process context parameters
	 * @throws ProcessInstanceException when can't get process parameters
	 */
	private Map<Serializable, Serializable> getProcessParameters(Long processID) throws ProcessInstanceException {
		Map<Serializable, Serializable> result = null;
		GraphSession graphSession = jbpmConfiguration.createJbpmContext().getGraphSession();
		try {
			ProcessInstance processInstance = graphSession.loadProcessInstance(processID);
			if (processInstance != null) {
				ContextInstance contextInstance = processInstance.getContextInstance();
				if (contextInstance != null) {
					result = contextInstance.getVariables();
				}
			}
		} catch (RuntimeException e) {
			log.error("getProcessParameters: ", e);
			throw new ProcessInstanceException("Can't get Process Dictionary for " + processID);
		}
		return result;
	}

	/**
	 * Create process for process definition name
	 *
	 * @param processDefinitionName process definitiona name
	 * @param parameters			initial context variables
	 * @return process instance identifier
	 * @throws ProcessInstanceException   when can't instantiate process instance
	 * @throws ProcessDefinitionException when process definition not found
	 */
	public synchronized long createProcess(String processDefinitionName, Map<Serializable, Serializable> parameters)
			throws ProcessInstanceException, ProcessDefinitionException {

		long processInstanceID = initProcess(processDefinitionName);
		//starting process
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		ProcessInstance processInstance = graphSession.loadProcessInstance(processInstanceID);

		ContextInstance ci = processInstance.getContextInstance();
		if (parameters == null) {
			parameters = new HashMap<Serializable, Serializable>();
		}
		parameters.put(Process.PROCESS_INSTANCE_ID, String.valueOf(processInstanceID));
		ci.addVariables(parameters);
		Token token = processInstance.getRootToken();
		token.signal();
		jbpmContext.close();

		if (log.isInfoEnabled()) {
			log.info("Process Instance id = " + processInstanceID + " started.");
		}

		return processInstanceID;
	}

	/**
	 * Start task instance
	 *
	 * @param task taskInstance to start
	 * @return true if task started
	 */
	@SuppressWarnings ({"unchecked"})
	protected boolean startTask(TaskInstance task) {
		// get ProcessInstance dictionary to pass it to Job
		ProcessInstance processInstance = task.getTaskMgmtInstance().getProcessInstance();
		ContextInstance contextInstance = processInstance.getContextInstance();

		Integer runCounter = getRunCount(task, contextInstance);

		if (runCounter > taskRepeatLimit) {
			log.info("Exceded limit (" + taskRepeatLimit + ") for task '" + task.getName() + "' (" +
					 task.getId() + ", pid - " + processInstance.getId() + "). Process ended.");
			completeTask(task, processInstance, contextInstance);
			return false;
		}

		LyfecycleVote vote = voteStart(task);
		switch (vote) {
			case CANCEL:
				log.info("Task '" + task.getName() + "' (" +
						 task.getId() + ", pid - " + processInstance.getId() + "). Cancelled by voters.");
				completeTask(task, processInstance, contextInstance);
				return false;
			case POSTPONE:
				log.info("Task " + task.getName() + " is beign postponed.");
				return false;
			case START:
				break;
		}

		Map<Serializable, Serializable> params = contextInstance.getVariables();

		log.info("Starting task '" + task.getName() + "' (" + task.getId() + ", pid - " + processInstance.getId() + ")");

		if (task.getStart() == null) {
			task.start();
		} else {
			log.info("Task '" + task.getName() + "' (" + task.getId() + ", pid=" + processInstance.getId() + ")" +
					 " restarted. Recovering from failure.");
		}

		try {
			JobManager.getInstance().addJob(processInstance.getId(), task.getId(), task.getName(), params);
		} catch (FlexPayException e) {
			log.error("Can't start task with name " + task.getName(), e);
			return false;
		}
		runningTasks.add(task.getId());
		return true;
	}

	private void completeTask(TaskInstance task, ProcessInstance processInstance, ContextInstance contextInstance) {

		processInstance.end();
		task.end(Job.RESULT_ERROR);
		//set status to failed
		contextInstance.setVariable(Job.RESULT_ERROR, ProcessState.COMPLITED_WITH_ERRORS);
		//remove from runningTasks tasks
		runningTasks.remove(task.getId());
	}

	private Integer getRunCount(TaskInstance task, ContextInstance contextInstance) {

		Integer runCounter = (Integer) contextInstance.getVariable("StartTaskCounter", task.getToken());

		if (runCounter == null) {
			runCounter = 1;
		} else {
			runCounter = runCounter + 1;
		}
		contextInstance.setVariable("StartTaskCounter", runCounter, task.getToken());
		return runCounter;
	}

	/**
	 * Vote for task start
	 *
	 * @param instance TaskInstance to vote for
	 * @return LyfecycleVote
	 */
	private LyfecycleVote voteStart(TaskInstance instance) {

		// defualt one is to start task
		LyfecycleVote vote = LyfecycleVote.START;
		for (LyfecycleVoter voter : lyfecycleVoters) {
			LyfecycleVote v = voter.onStart(instance);
			if (v.ordinal() > vote.ordinal()) {
				vote = v;
			}
		}

		return vote;
	}

	/**
	 * Called when process job was finished
	 *
	 * @param taskId	 Task ID
	 * @param parameters Task context parameters
	 * @param transition transition name
	 */
	public void jobFinished(Long taskId, Map<Serializable, Serializable> parameters, String transition) {
		// this method called by Job to report finish
		log.debug("finished taskId: " + taskId);

		boolean proceed = false;

		while (!proceed) {
			try {
				JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
				TaskMgmtSession taskMgmtSession = jbpmContext.getTaskMgmtSession();
				TaskInstance task = taskMgmtSession.loadTaskInstance(taskId);

				if (task == null) {
					log.error("Can't find Task Instance, id: " + taskId);
				} else {
					log.info("Finishing Task Instance, id: " + taskId);
					ContextInstance ci = task.getTaskMgmtInstance().getProcessInstance().getContextInstance();
					// save the variables in ProcessInstance dictionary
					ci.addVariables(parameters);
					ci.setVariable("StartTaskCounter", 0, task.getToken());
					// mark task as ended with job result code as decision transition value
					task.end(transition);
				}
				jbpmContext.close();
				proceed = true;
				Object removed;
				synchronized (sleepSemaphore) {
					removed = runningTasks.remove(taskId);
					sleepSemaphore.notifyAll();
				}

				checkProcessCompleted(task.getTaskMgmtInstance().getProcessInstance().getId());

				if (log.isDebugEnabled()) {
					log.debug("Task removed from list of running tasks: " + removed);
					log.debug("Number of running tasks: " + runningTasks.size());
				}
			} catch (RuntimeException e) {
				log.error("Failed finishing task: " + taskId, e);
				log.error("Sleeping for 30 sec and try again to finish task: " + taskId);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException ie) {
					log.fatal("System failure when finishing task: " + taskId, e);
				}
			}
		}
	}

	/**
	 * Check if process was completed and close its log
	 *
	 * @param processId Process ID
	 */
	private void checkProcessCompleted(long processId) {
		Process process = getProcessInstanceInfo(processId);
		if (process.getProcessState().isCompleted()) {
			ProcessLogger.closeLog(processId);
			log.info("Closing process log: " + processId);
		}
	}

	/**
	 * Start process by processID
	 *
	 * @param processID  process ID
	 * @param parameters process parameters
	 * @throws ProcessInstanceException when can't start process
	 */
	private void startProcess(Long processID, Map<Serializable, Serializable> parameters)
			throws ProcessInstanceException {
		try {
			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
			GraphSession graphSession = jbpmContext.getGraphSession();
			ProcessInstance processInstance = graphSession.loadProcessInstance(processID);

			ContextInstance ci = processInstance.getContextInstance();
			if (parameters == null) {
				parameters = new HashMap<Serializable, Serializable>();
			}
			parameters.put(Process.PROCESS_INSTANCE_ID, String.valueOf(processID));
			ci.addVariables(parameters);
			Token token = processInstance.getRootToken();
			token.signal();
			log.info("Process Instance id = " + processID.toString() + " started.");
			jbpmContext.close();
		} catch (RuntimeException e) {
			log.error("Failed start process", e);
			throw new ProcessInstanceException("Can't start ProcessInstance pid - " + processID, e);
		}
	}

	/**
	 * Remove proces from execution
	 *
	 * @param processID process ID
	 * @throws ProcessInstanceException when runtime exception
	 */
	public void removeProcess(Long processID) throws ProcessInstanceException {
		try {
			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
			GraphSession graphSession = jbpmContext.getGraphSession();
			ProcessInstance processInstance = graphSession.loadProcessInstance(processID);
			if (processInstance != null) {
				ProcessDefinition processDefinition = processInstance.getProcessDefinition();
				ProcessDefinition latestProcessDefinition = graphSession.findLatestProcessDefinition(processDefinition.getName());
				graphSession.deleteProcessInstance(processID);
				long processDefinitionID = processDefinition.getId();
				if (processDefinitionID != latestProcessDefinition.getId()) {
					List processInstances = graphSession.findProcessInstances(processDefinitionID);
					if (processInstances.size() == 0) {
						graphSession.deleteProcessDefinition(processDefinitionID);
					}
					log.debug("Removed process definition " + processDefinitionID);
				}
			}
			jbpmContext.close();
		} catch (RuntimeException e) {
			log.error("Failed removeProcess", e);
			throw new ProcessInstanceException("Can't remove ProcessInstance for " + processID, e);
		}
	}

	synchronized
	List<TaskInstance> getRunningTasks() {
		JbpmContext context = jbpmConfiguration.createJbpmContext();
		List<Long> taskIds = CollectionUtils.list(runningTasks);
		if (taskIds.isEmpty()) {
			return Collections.emptyList();
		}
		return (List<TaskInstance>) context.getTaskMgmtSession().findTaskInstancesByIds(taskIds);
	}

	/**
	 * Get list of system processes
	 *
	 * @return Process list
	 */
	@SuppressWarnings ({"unchecked"})
	synchronized 
	public List<Process> getProcessList() {
		List<Process> processes = CollectionUtils.list();
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		List<ProcessDefinition> processDefinitions = graphSession.findAllProcessDefinitions();
		for (ProcessDefinition processDefinition : processDefinitions) {
			List<ProcessInstance> processInstances = graphSession.findProcessInstances(processDefinition.getId());
			for (ProcessInstance processInstance : processInstances) {
				Process process = new Process();
				process.setId(processInstance.getId());
				process.setProcessDefinitionName(processInstance.getProcessDefinition().getName());
				process.setProcessEndDate(processInstance.getEnd());
				process.setProcessStartDate(processInstance.getStart());
				process.setProcessDefenitionVersion(processInstance.getProcessDefinition().getVersion());
				Map<Serializable, Serializable> parameters = processInstance.getContextInstance().getVariables();
				if (parameters == null) {
					process.setParameters(new HashMap<Serializable, Serializable>());
				} else {
					process.setParameters(parameters);
				}
				processes.add(process);
			}
		}
		jbpmContext.close();
		return processes;
	}

	/**
	 * Retrive process info
	 *
	 * @param processId ProcessInstance id
	 * @return Process info
	 */
	@NotNull
	@SuppressWarnings ({"unchecked"})
	synchronized 
	public Process getProcessInstanceInfo(@NotNull Long processId) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		Process process = new Process();
		ProcessInstance processInstance = jbpmContext.getProcessInstance(processId);
		if (processInstance != null) {
			process.setId(processInstance.getId());
			process.setProcessDefinitionName(processInstance.getProcessDefinition().getName());
			process.setProcessEndDate(processInstance.getEnd());
			process.setProcessStartDate(processInstance.getStart());
			process.setProcessDefenitionVersion(processInstance.getProcessDefinition().getVersion());
			File logFile = ProcessLogger.getLogFile(processInstance.getId());
			if (logFile.exists()) {
				process.setLogFileName(logFile.getAbsolutePath());
			} else {
				process.setLogFileName("");
			}

			Map<Serializable, Serializable> parameters = processInstance.getContextInstance().getVariables();
			if (parameters == null) {
				process.setParameters(new HashMap<Serializable, Serializable>());
			} else {
				process.setParameters(parameters);
			}
		}
		jbpmContext.close();
		return process;
	}

	/**
	 * Check if task is currently executing
	 *
	 * @param taskInstanceId Task instance id
	 * @return <code>true</code> if task is being executing, or <code>false</code> otherwise
	 */
	private boolean isTaskExecuting(long taskInstanceId) {

		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		TaskInstance task = jbpmContext.getTaskMgmtSession().loadTaskInstance(taskInstanceId);
		if (task.hasEnded()) {
			log.debug("Task already finished, ignoring task # " + String.valueOf(taskInstanceId));
			jbpmContext.close();
			return true;
		} else {
			if (runningTasks.contains(taskInstanceId)) {
				log.debug(taskInstanceId + " is already started, checking runner");
				jbpmContext.close();
				return true;
			} else {
				log.debug(taskInstanceId + " is not started");
				jbpmContext.close();
				return false;
			}
		}
	}

	/**
	 * Wait for process completion
	 *
	 * @param processId ProcessInstance id
	 * @throws InterruptedException if waiting thread is interrupted
	 */
	public void join(long processId) throws InterruptedException {
		while (true) {
			Process info = getProcessInstanceInfo(processId);
			if (info.getId() != processId) {
				return;
			}

			ProcessState state = info.getProcessState();
			if (state.isCompleted()) {
				return;
			}

			Thread.sleep(50);
		}
	}

	/**
	 * Delete process instance
	 *
	 * @param process Process to delete
	 */
	public void deleteProcessInstance(Process process) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		jbpmContext.getGraphSession().deleteProcessInstance(process.getId());
		jbpmContext.close();
	}

	/**
	 * Delete several process instances
	 *
	 * @param processes Processes to delete
	 */
	public void deleteProcessInstances(List<Process> processes) {
		Set<Long> processIds = CollectionUtils.set();
		for (Process process : processes) {
			processIds.add(process.getId());
		}

		deleteProcessInstances(processIds);
	}

	/**
	 * Delete several process instances
	 *
	 * @param processIds Process instances identifiers to delete
	 */
	public void deleteProcessInstances(Set<Long> processIds) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		for (Long processId : processIds) {
			jbpmContext.getGraphSession().deleteProcessInstance(processId);
		}
		jbpmContext.close();
	}

	/**
	 * Sets JbpmConfiguration
	 *
	 * @param jbpmConfiguration - current jbpmConfiguration
	 */
	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}

	public void setDefinitionPaths(List<String> definitionPaths) {
		this.definitionPaths.clear();
		this.definitionPaths.addAll(definitionPaths);
	}

	public void setRescanFrequency(int rescanFrequency) {
		this.rescanFrequency = rescanFrequency;
	}

	public void setTaskRepeatLimit(int taskRepeatLimit) {
		this.taskRepeatLimit = taskRepeatLimit;
	}

	/**
	 * Add voters
	 *
	 * @param lyfecycleVoters Voters to set
	 */
	public void setLyfecycleVoters(List<LyfecycleVoter> lyfecycleVoters) {
		this.lyfecycleVoters.addAll(lyfecycleVoters);
	}
}
