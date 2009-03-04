package org.flexpay.common.process;

import org.apache.commons.io.IOUtils;
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
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.context.SecurityContextHolder;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Process manager allows to create and maintain processes lyfecycle
 */
public class ProcessManagerImpl implements ProcessManager, Runnable {

	/**
	 * Loggger
	 */
	private static final Logger log = LoggerFactory.getLogger(ProcessManagerImpl.class);

	/**
	 * singleton instance
	 */
	private static final ProcessManagerImpl instance = new ProcessManagerImpl();
	private static final Thread thread = new Thread(instance, "ProcessManager Thread");
	private volatile boolean started = false;
	private volatile boolean stopped = false;
	private static final Object sleepSemaphore = new Object();

	/**
	 * Set of task instance ids currently in progress
	 */
	private final Set<Long> runningTaskIds = CollectionUtils.set();

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
	private ProcessManagerImpl() {
		log.debug("ProcessManager constructor called ");
	}

	public static ProcessManagerImpl getInstance() {

		return instance;
	}

	/**
	 * Start ProcessManager Thread
	 */
	public void start() {

		log.debug("Starting ProcessManager thread");
		synchronized (instance) {
			if (instance.isStarted()) {
				return;
			}
			instance.setStarted(true);
		}

		thread.start();
		log.debug("ProcessManager thread started");
	}

	/**
	 * Unload process manager and stop process manager thread
	 */
	public void stop() {
		log.debug("Stoping ProcessManager thread");
		synchronized (instance) {
			if (instance.isStopped()) {
				return;
			}
			instance.stopProcessManager();
		}

		try {
			thread.join();
		} catch (InterruptedException e) {
			log.error("Failed joining thread", e);
			throw new RuntimeException("Failed joining thread", e);
		}
		log.debug("ProcessManager thread stoped");
	}

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @param replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deplot process definition to jbpm
	 */
	public long deployProcessDefinition(String name, boolean replace) throws ProcessDefinitionException {

		log.debug("Requested definition deployment: {}", name);
		InputStream is = null;
		try {
			for (String path : definitionPaths) {
				String resource = path + "/" + name + ".xml";
				log.debug("Looking up {}", resource);
				is = ApplicationConfig.getResourceAsStream(resource);
				if (is != null) {
					log.debug("Found!");
					return deployProcessDefinition(is, replace);
				}
			}

			log.warn("No definition found: {}", name);
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
            processDefinition.setName(ApplicationConfig.getApplicationName() + "." + processDefinition.getName());
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
	 * Deploys parsed process definition to jbpm
	 *
	 * @param processDefinition parsed process definition
	 * @param replace		   replace replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 */
	public long deployProcessDefinition(final ProcessDefinition processDefinition, final boolean replace) {

		return execute(new ContextCallback<Long>() {
			public Long doInContext(@NotNull JbpmContext context) {

				GraphSession graphSession = context.getGraphSession();
				ProcessDefinition latestProcessDefinition =
						graphSession.findLatestProcessDefinition(processDefinition.getName());

				int newVersion;

				if (replace || (latestProcessDefinition == null)) {
					if (latestProcessDefinition == null) {
						log.info("Process definition not found. Deploying {}", processDefinition.getName());
						newVersion = 1;
						processDefinition.setVersion(newVersion);
					} else {
						int oldVersion = latestProcessDefinition.getVersion();
						log.info("Deploying new version of process definition {}", processDefinition.getName());
						newVersion = oldVersion + 1;
						processDefinition.setVersion(newVersion);
						log.info("Old version = {}, New version = {}", oldVersion, newVersion);
					}
					graphSession.saveProcessDefinition(processDefinition);
					log.info("Deployed");
				}

				return processDefinition.getId();
			}
		});
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
				execute(new ContextCallback<Void>() {
					public Void doInContext(@NotNull JbpmContext context) {
						for (Object o : context.getTaskMgmtSession().findTaskInstances(JobManagerAssignmentHandler.JOB_MANAGER_ACTOR_NAME)) {
							// are there any not runningTasks taskInstances?
							TaskInstance taskInstance = (TaskInstance) o;
							taskInstance = context.getTaskMgmtSession().loadTaskInstance(taskInstance.getId());
							if (!isTaskExecuting(taskInstance)) {
								startTask(taskInstance);
							}
						}
						return null;
					}
				});

				//all task instances started, going to sleep
				synchronized (sleepSemaphore) {
					try {
						sleepSemaphore.wait(rescanFrequency);
					} catch (InterruptedException e) {
						log.debug("Somebody interrupts me.", e);
						Thread.interrupted(); // clear "interrupted" state of thread
					}
				}
			} catch (Throwable e) {
				log.debug("ProcessManager mainLoop : ", e);
			}
		}
		log.debug("process manager stopped...");
	}

	/**
	 * Stops ProcessManager execution
	 */
	public void stopProcessManager() {
		stopped = true;
	}

	/**
	 * @return true if ProcessManager is not running
	 */
	public boolean isStopped() {
		return stopped;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * Init process by process definition name
	 *
	 * @param processDefinitionName name of process definition
	 * @return ID of process instance
	 * @throws ProcessDefinitionException when process definition has an error
	 * @throws ProcessInstanceException   when jbpm can't instanciate process from process definition
	 */
	private Long initProcess(String processDefinitionName)
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
			log.error("Can't find process definition for name: {}", processDefinitionName);
			throw new ProcessDefinitionException("Can't find process definition for name: " + processDefinitionName);
		}

		log.info("Initializing  process. Process Definition id = {}, name = {}, version = {}",
				new Object[] {processDefinition.getId(), processDefinition.getName(), processDefinition.getVersion()});

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
	 * Create process for process definition name
	 *
	 * @param definitionName process definitiona name
	 * @param parameters	 initial context variables
	 * @return process instance identifier
	 * @throws ProcessInstanceException   when can't instantiate process instance
	 * @throws ProcessDefinitionException when process definition not found
	 */
	public long createProcess(String definitionName, Map<Serializable, Serializable> parameters)
			throws ProcessInstanceException, ProcessDefinitionException {

		final long processId = initProcess(definitionName);
		final Map<Serializable, Serializable> params;
		if (parameters != null) {
			params = parameters;
		} else {
			params = CollectionUtils.map();
		}

		params.put(PARAM_SECURITY_CONTEXT, SecurityContextHolder.getContext().getAuthentication());

		return execute(new ContextCallback<Long>() {
			public Long doInContext(@NotNull JbpmContext context) {

				//starting process
				GraphSession graphSession = context.getGraphSession();
				ProcessInstance processInstance = graphSession.loadProcessInstance(processId);

				ContextInstance ci = processInstance.getContextInstance();
				params.put(Process.PROCESS_INSTANCE_ID, String.valueOf(processId));
				ci.addVariables(params);
				processInstance.getRootToken().signal();

				log.info("Process Instance id = {} started.", processId);

				return processId;
			}
		});
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
		ProcessInstance processInstance = task.getProcessInstance();
		ContextInstance contextInstance = processInstance.getContextInstance();

		Integer runCounter = getRunCount(task);

		if (runCounter > taskRepeatLimit) {
			log.info("Exceded limit ({}) for task '{}' ({}, pid - {}). Process ended.",
					new Object[] {taskRepeatLimit, task.getName(), task.getId(), processInstance.getId()});
			completeTask(task);
			return false;
		}

		LyfecycleVote vote = voteStart(task);
		switch (vote) {
			case CANCEL:
				log.info("Task '{}' ({}, pid - {}). Cancelled by voters.",
					new Object[] {task.getName(), task.getId(), processInstance.getId()});
				completeTask(task);
				return false;
			case POSTPONE:
				log.info("Task {} is beign postponed.", task.getName());
				return false;
			case START:
				break;
		}

		log.info("Starting task '{}' ({}, pid - {})",
					new Object[] {task.getName(), task.getId(), processInstance.getId()});

		if (task.getStart() == null) {
			task.start();
		} else {
			log.info("Task '{}' ({}, pid={}) restarted. Recovering from failure.",
					new Object[] {task.getName(), task.getId(), processInstance.getId()});
		}

		try {
			Map<Serializable, Serializable> params = contextInstance.getVariables();
			JobManager.getInstance().addJob(processInstance.getId(), task.getId(), task.getName(), params);
		} catch (FlexPayException e) {
			log.error("Can't start task with name " + task.getName(), e);
			return false;
		}

		runningTaskIds.add(task.getId());
		return true;
	}

	private void completeTask(TaskInstance task) {

		ProcessInstance processInstance = task.getProcessInstance();
		ContextInstance contextInstance = processInstance.getContextInstance();
		processInstance.end();
		task.end(Job.RESULT_ERROR);
		//set status to failed
		contextInstance.setVariable(Job.RESULT_ERROR, ProcessState.COMPLITED_WITH_ERRORS);
		//remove from runningTasks tasks
		runningTaskIds.remove(task.getId());
	}

	private Integer getRunCount(TaskInstance task) {

		ProcessInstance processInstance = task.getProcessInstance();
		ContextInstance contextInstance = processInstance.getContextInstance();
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
	public void taskCompleted(final Long taskId, final Map<Serializable, Serializable> parameters, final String transition) {
		// this method called by Job to report finish
		log.debug("finished task #: {}", taskId);

		boolean proceed = false;

		while (!proceed) {
			try {
				TaskInstance task = execute(new ContextCallback<TaskInstance>() {
					public TaskInstance doInContext(@NotNull JbpmContext context) {
						TaskMgmtSession taskMgmtSession = context.getTaskMgmtSession();
						TaskInstance task = taskMgmtSession.loadTaskInstance(taskId);

						if (task == null) {
							log.error("Can't find Task Instance, id: {}", taskId);
						} else {
							log.info("Finishing Task Instance, id: {}", taskId);
							ContextInstance ci = task.getProcessInstance().getContextInstance();
							// save the variables in ProcessInstance dictionary
							ci.addVariables(parameters);
							ci.setVariable("StartTaskCounter", 0, task.getToken());
							// mark task as ended with job result code as decision transition value
							task.end(transition);
						}
						return task;
					}
				});

				proceed = true;
				boolean removed;
				synchronized (sleepSemaphore) {
					removed = runningTaskIds.remove(taskId);
					sleepSemaphore.notifyAll();
				}

				checkProcessCompleted(task.getProcessInstance().getId());

				log.debug("Task removed from list of running tasks: {}", removed);
				log.debug("Number of running tasks: {}", runningTaskIds.size());
			} catch (RuntimeException e) {
				log.error("Failed finishing task: " + taskId, e);
				log.error("Sleeping for 30 sec and try again to finish task: {}", taskId);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException ie) {
					log.error("System failure when finishing task: " + taskId, e);
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
			log.info("Closing process log: {}", processId);
		}
	}

	/**
	 * Remove process from execution
	 *
	 * @param processID process ID
	 * @throws ProcessInstanceException when runtime exception
	 */
	public void removeProcess(final Long processID) throws ProcessInstanceException {
		try {
			execute(new ContextCallback<Void>() {
				public Void doInContext(@NotNull JbpmContext context) {
					GraphSession graphSession = context.getGraphSession();
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
							log.debug("Removed process definition {}", processDefinitionID);
						}
					}

					return null;
				}
			});
		} catch (RuntimeException e) {
			log.error("Failed removeProcess", e);
			throw new ProcessInstanceException("Can't remove ProcessInstance for " + processID, e);
		}
	}

	public List<TaskInstance> getRunningTasks() {

		return execute(new ContextCallback<List<TaskInstance>>() {

			public List<TaskInstance> doInContext(@NotNull JbpmContext context) {
				List<Long> taskIds = CollectionUtils.list(runningTaskIds);
				if (taskIds.isEmpty()) {
					return Collections.emptyList();
				}

				// Init lazy fields
				List<?> instances = context.getTaskMgmtSession().findTaskInstancesByIds(taskIds);
				for (Object obj : instances) {
					TaskInstance instance = (TaskInstance) obj;
					instance.getProcessInstance().getContextInstance().getVariables();
				}

				//noinspection unchecked
				return (List<TaskInstance>) instances;
			}
		}, true);
	}

	/**
	 * Get list of system processes
	 *
	 * @return Process list
	 */
	@SuppressWarnings ({"unchecked"})
	public List<Process> getProcesses() {
		return execute(new ContextCallback<List<Process>>() {

			public List<Process> doInContext(@NotNull JbpmContext context) {
				List<Process> processes = CollectionUtils.list();
				GraphSession graphSession = context.getGraphSession();
				List<ProcessDefinition> processDefinitions = graphSession.findAllProcessDefinitions();
				for (ProcessDefinition processDefinition : processDefinitions) {
					List<ProcessInstance> processInstances = graphSession.findProcessInstances(processDefinition.getId());
					for (ProcessInstance processInstance : processInstances) {
						processes.add(getProcessInfo(processInstance));
					}
				}

				return processes;
			}
		});
	}

	private Process getProcessInfo(ProcessInstance processInstance) {

		Process process = new Process();
		if (processInstance == null) {
			return process;
		}

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

		@SuppressWarnings ({"unchecked"})
		Map<Serializable, Serializable> parameters = processInstance.getContextInstance().getVariables();
		if (parameters == null) {
			parameters = CollectionUtils.map();
		}
		process.setParameters(parameters);

		if (parameters.containsKey(Job.RESULT_ERROR)) {
			process.setProcessState((ProcessState) parameters.get(Job.RESULT_ERROR));
		}

		return process;
	}

	/**
	 * Retrive process info
	 *
	 * @param processId ProcessInstance id
	 * @return Process info
	 */
	@NotNull
	@SuppressWarnings ({"unchecked"})
	public Process getProcessInstanceInfo(@NotNull final Long processId) {

		return execute(new ContextCallback<Process>() {

			public Process doInContext(@NotNull JbpmContext context) {
				return getProcessInfo(context.getProcessInstance(processId));
			}
		});
	}

	/**
	 * Check if task is currently executing
	 *
	 * @param task Task instance
	 * @return <code>true</code> if task is being executing, or <code>false</code> otherwise
	 */
	private boolean isTaskExecuting(final TaskInstance task) {

		if (task.hasEnded()) {
			log.debug("Task already finished, ignoring task # {}", task.getId());
			return true;
		} else {
			if (runningTaskIds.contains(task.getId())) {
				log.debug("Task {} is already started, checking runner", task.getId());
				return true;
			} else {
				log.debug("Task {} is not started", task.getId());
				return false;
			}
		}
	}

	/**
	 * Quietly close jBPM context
	 *
	 * @param context context to close
	 */
	private void closeQuietly(@Nullable JbpmContext context) {
		if (context != null) {
			context.close();
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
			// wait untill there is any 
			synchronized (sleepSemaphore) {
				Process info = getProcessInstanceInfo(processId);
				if (info.getId() != processId) {
					return;
				}

				ProcessState state = info.getProcessState();
				if (state.isCompleted()) {
					return;
				}

				sleepSemaphore.wait(5000);
			}
		}
	}

	/**
	 * Delete process instance
	 *
	 * @param process Process to delete
	 */
	public void deleteProcessInstance(final Process process) {

		execute(new ContextCallback<Void>() {

			public Void doInContext(@NotNull JbpmContext context) {
				context.getGraphSession().deleteProcessInstance(process.getId());
				return null;
			}
		});
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
	public void deleteProcessInstances(final Set<Long> processIds) {

		execute(new ContextCallback<Void>() {

			public Void doInContext(@NotNull JbpmContext context) {
				for (Long processId : processIds) {
					context.getGraphSession().deleteProcessInstance(processId);
				}

				return null;
			}
		});
	}

	/**
	 * Execute Context callback
	 *
	 * @param callback ContextCallback to execute
	 * @param <T>      Return value type
	 * @return instance of T
	 */
	private <T> T execute(@NotNull ContextCallback<T> callback) {
		return execute(callback, false);
	}

	/**
	 * Execute Context callback
	 *
	 * @param callback		   ContextCallback to execute
	 * @param useExistingContext Whether to use existing context or not
	 * @param <T>                Return value type
	 * @return instance of T
	 */
	synchronized
	private <T> T execute(@NotNull ContextCallback<T> callback, boolean useExistingContext) {

		JbpmContext context = null;
		boolean needClose = true;
		try {
			context = useExistingContext ? jbpmConfiguration.getCurrentJbpmContext() : jbpmConfiguration.createJbpmContext();
			if (useExistingContext) {
				needClose = false;
				if (context == null) {
					log.warn("Required existing context, but not exists");
					context = jbpmConfiguration.createJbpmContext();
					needClose = true;
				}
			}
			return callback.doInContext(context);
		} finally {
			if (needClose) {
				closeQuietly(context);
			}

		}
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

interface ContextCallback<T> {

	T doInContext(@NotNull JbpmContext context);
}
