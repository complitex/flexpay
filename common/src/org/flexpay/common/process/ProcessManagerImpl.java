package org.flexpay.common.process;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.dao.ProcessDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.job.JobManager;
import org.flexpay.common.process.sorter.ProcessSorter;
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
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;

import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

import static org.flexpay.common.util.CollectionUtils.map;

/**
 * Process manager allows to create and maintain processes life cycle
 */
public class ProcessManagerImpl implements ProcessManager, Runnable {

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

	/**
	 * Process data access object
	 */
	private ProcessDao processDao;

	private JbpmConfiguration jbpmConfiguration = null;

	/**
	 * Predefined set of paths where to look definitions if not already deployed
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

		if (instance.isStarted()) {
			return;
		}

		log.debug("Starting ProcessManager thread");
		synchronized (instance) {
			if (instance.isStarted()) {
				return;
			}
			thread.start();
			instance.setStarted(true);
		}
		log.debug("ProcessManager thread started");
	}

	/**
	 * Unload process manager and stop process manager thread
	 */
	public void stop() {
		log.debug("Stopping ProcessManager thread");
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
		log.debug("ProcessManager thread stopped");
	}

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @param replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deplot process definition to jbpm
	 */
	@Override
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
			throw new ProcessDefinitionException("Process definition for name " + name + " file not found!",
					"error.common.pm.pd_file_not_found", name);
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
	 * @throws ProcessDefinitionException when can't deploy process definition to jbpm
	 */
	@Override
	public long deployProcessDefinition(InputStream in, boolean replace) throws ProcessDefinitionException {
		ProcessDefinition processDefinition = null;
		try {
			processDefinition = ProcessDefinition.parseXmlInputStream(in);
			return deployProcessDefinition(processDefinition, replace);
		} catch (Exception e) {
			log.error("Process definition deployment failed", e);
			if (processDefinition == null) {
				throw new ProcessDefinitionException("Not process definition", e, "error.common.pm.not_pd");
			} else {
				throw new ProcessDefinitionException("Can't deploy definition for " + processDefinition.getName(), e,
						"error.common.pm.pd_deployment_failed", processDefinition.getName());
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
	@Override
	public long deployProcessDefinition(final ProcessDefinition processDefinition, final boolean replace) {

		return execute(new ContextCallback<Long>() {
			@Override
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
	@Override
	public void run() {
		log.debug("Starting process manager...");
		while (!isStopped()) {
			try {
				//write tick-tack message
				log.trace("Collecting task instances to run.");

				//find not runningTasks task instances and run
				execute(new ContextCallback<Void>() {
					@Override
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
	 * @param definitionName name of process definition
	 * @return ID of process instance
	 * @throws ProcessDefinitionException when process definition has an error
	 * @throws ProcessInstanceException   when jbpm can't instanciate process from process definition
	 */
	private Long initProcess(String definitionName) throws ProcessDefinitionException, ProcessInstanceException {

		ProcessDefinition processDefinition;
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		try {
			processDefinition = graphSession.findLatestProcessDefinition(definitionName);
		} catch (RuntimeException e) {
			jbpmContext.close();
			log.error("findLatestProcessDefinition", e);
			throw new ProcessDefinitionException("Can't access ProcessDefinition for " + definitionName, e,
					"error.common.pm.cant_access_pd", definitionName);
		}
		// try to search in predefined set of places
		if (processDefinition == null) {
			deployProcessDefinition(definitionName, true);
			jbpmContext.close();
			jbpmContext = jbpmConfiguration.createJbpmContext();
			graphSession = jbpmContext.getGraphSession();
			processDefinition = graphSession.findLatestProcessDefinition(definitionName);
		}
		if (processDefinition == null) {
			log.error("Can't find process definition for name: {}", definitionName);
			throw new ProcessDefinitionException("Can't find process definition for name: " + definitionName,
					"error.common.pm.cant_access_pd", definitionName);
		}

		log.info("Initializing  process. Process Definition id = {}, name = {}, version = {}",
				new Object[] {processDefinition.getId(), processDefinition.getName(), processDefinition.getVersion()});

		try {
			ProcessInstance processInstance = new ProcessInstance(processDefinition);
			return processInstance.getId();
		} catch (RuntimeException e) {
			log.error("ProcessInstanceCreation", e);
			throw new ProcessInstanceException("Can't create ProcessInstance for " + definitionName, e,
					"error.common.pm.cant_create_pi", definitionName);
		} finally {
			jbpmContext.close();
		}
	}

	/**
	 * Create process for process definition name
	 *
	 * @param definitionName process definition name
	 * @param parameters	 initial context variables
	 * @return process instance identifier
	 * @throws ProcessInstanceException   when can't instantiate process instance
	 * @throws ProcessDefinitionException when process definition not found
	 */
	@Override
	public long createProcess(@NotNull String definitionName, @Nullable Map<Serializable, Serializable> parameters)
			throws ProcessInstanceException, ProcessDefinitionException {

		final long processId = initProcess(definitionName);
		final Map<Serializable, Serializable> params;
		if (parameters != null) {
			params = parameters;
		} else {
			params = map();
		}

		params.put(PARAM_SECURITY_CONTEXT, SecurityContextHolder.getContext().getAuthentication());

		return execute(new ContextCallback<Long>() {
			@Override
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

    @Override
    public void endProcess(Process process) {

        final Long processId = process.getId();
        final Map<Serializable, Serializable> params = process.getParameters();

        execute(new ContextCallback<Long>() {
            @Override
            public Long doInContext(@NotNull JbpmContext context) {

                //starting process
                GraphSession graphSession = context.getGraphSession();
                ProcessInstance processInstance = graphSession.loadProcessInstance(processId);

                ContextInstance ci = processInstance.getContextInstance();

                params.put(Process.PROCESS_INSTANCE_ID, String.valueOf(processId));
                ci.addVariables(params);
                processInstance.getRootToken().end();

                log.info("Process Instance id = {} ended.", processId);

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
					new Object[]{taskRepeatLimit, task.getName(), task.getId(), processInstance.getId()});
			completeTask(task);
			return false;
		}

		LyfecycleVote vote = voteStart(task);
		switch (vote) {
			case CANCEL:
				log.info("Task '{}' ({}, pid - {}). Cancelled by voters.",
						new Object[]{task.getName(), task.getId(), processInstance.getId()});
				completeTask(task);
				return false;
			case POSTPONE:
				log.info("Task {} is beign postponed.", task.getName());
				return false;
			case START:
				break;
		}

		log.info("Starting task '{}' ({}, pid - {})",
				new Object[]{task.getName(), task.getId(), processInstance.getId()});

		if (task.getStart() == null) {
			task.start();
		} else {
			log.info("Task '{}' ({}, pid={}) restarted. Recovering from failure.",
					new Object[]{task.getName(), task.getId(), processInstance.getId()});
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
		contextInstance.setVariable(Job.RESULT_ERROR, ProcessState.COMPLETED_WITH_ERRORS);
		//remove from runningTasks tasks
		runningTaskIds.remove(task.getId());
	}

	private Integer getRunCount(TaskInstance task) {

		ProcessInstance processInstance = task.getProcessInstance();
		ContextInstance contextInstance = processInstance.getContextInstance();
		Integer runCounter = (Integer) contextInstance.getVariable("StartTaskCounter", task.getToken());
		runCounter = runCounter == null ? 1 : runCounter + 1;

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
	@Override
	public void taskCompleted(final Long taskId, final Map<Serializable, Serializable> parameters, final String transition) {
		// this method called by Job to report finish
		log.debug("finished task #: {}", taskId);

		boolean proceed = false;

		while (!proceed) {
			try {
				TaskInstance task = execute(new ContextCallback<TaskInstance>() {
					@Override
					public TaskInstance doInContext(@NotNull JbpmContext context) {

						if (taskId == null) {
							log.error("Task id is null!");
							return null;
						}

						TaskMgmtSession taskMgmtSession = context.getTaskMgmtSession();
						TaskInstance task = taskMgmtSession.getTaskInstance(taskId);

						if (task == null) {
							log.debug("Can't find Task Instance, id: {}", taskId);
						} else {
							log.debug("Finishing Task Instance, id: {}", taskId);
							ContextInstance ci = task.getProcessInstance().getContextInstance();
							// save the variables in ProcessInstance dictionary
							log.trace(parameters.toString());
							ci.addVariables(parameters);
							ci.setVariable("StartTaskCounter", 0, task.getToken());
							// mark task as ended with job result code as decision transition value
							if (task.getEnd() == null) {
								task.end(transition);
							}
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

				if (task != null) {
					checkProcessCompleted(task.getProcessInstance().getId());
				}

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
		if (process != null && process.getProcessState().isCompleted()) {
			ProcessLogger.closeLog(processId);
			log.info("Closing process log: {}", processId);
		}
	}

	@SuppressWarnings ({"unchecked"})
    @Override
	public List<TaskInstance> getRunningTasks() {

		return execute(new ContextCallback<List<TaskInstance>>() {
			@Override
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

				return (List<TaskInstance>) instances;
			}
		}, true);
	}

	@Override
	public List<Process> getProcesses() {
		return getProcesses(null);
	}

	@Override
	public List<Process> getProcesses(Page<Process> pager) {
		return getProcesses(null, pager, null, null, null, null);
	}

	@Override
	public List<Process> getProcesses(ProcessSorter processSorter, Page<Process> pager, Date startFrom, Date endBefore,
									  ProcessState state, String name) {

		return processDao.findProcesses(processSorter, pager, startFrom, endBefore, state, name);
	}

	@Override
	public List<String> getAllProcessNames() {
		return processDao.findAllProcessNames();
	}

	/**
	 * Retrive process info
	 *
	 * @param processId ProcessInstance id
	 * @return Process info
	 */
	@Nullable
	@Override
	public Process getProcessInstanceInfo(@NotNull final Long processId) {

		return execute(new ContextCallback<Process>() {
			@Override
			public Process doInContext(@NotNull JbpmContext context) {
				ProcessInstance processInstance = context.getProcessInstance(processId);
				if (processInstance == null) {
					log.debug("Process with id = {} not found", processId);
					return null;
				}
				return processDao.getProcessInfoWithVariables(processInstance);
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
	@Override
	public void join(long processId) throws InterruptedException {
		while (true) {

			Process info = getProcessInstanceInfo(processId);
			if (info == null || info.getId() != processId) {
				return;
			}

			// wait until there is any 
			synchronized (sleepSemaphore) {

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
	@Override
	public void deleteProcessInstance(final Process process) {

		execute(new ContextCallback<Void>() {
			@Override
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
	@Override
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
	@Override
	public void deleteProcessInstances(final Set<Long> processIds) {

		execute(new ContextCallback<Void>() {
			@Override
			public Void doInContext(@NotNull JbpmContext context) {
				for (Long processId : processIds) {
					context.getGraphSession().deleteProcessInstance(processId);
				}

				return null;
			}
		});
	}

    @Override
	public void deleteProcessInstances(DateRange range, ProcessNameFilter nameFilter) {
		processDao.deleteProcessInstances(range, nameFilter.getSelectedName());
	}

	/**
	 * Retrieve ProcessInstance
	 *
	 * @param processInstanceId ProcessInstance id
	 * @return Process info
	 */
	@Override
	public ProcessInstance getProcessInstance(@NotNull final Long processInstanceId) {
		return execute(new ContextCallback<ProcessInstance>() {
			@Override
			public ProcessInstance doInContext(@NotNull JbpmContext context) {
				return context.getProcessInstance(processInstanceId);
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
	@Override
	public <T> T execute(@NotNull ContextCallback<T> callback) {
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
	@Override
	public <T> T execute(@NotNull ContextCallback<T> callback, boolean useExistingContext) {

		while (!isStarted()) {
			try {
				Thread.sleep(5L);
			} catch (InterruptedException e) {
				throw new RuntimeException("Failed waiting for start up.", e);
			}
		}

		synchronized (instance) {
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
	}

	/**
	 * Sets JbpmConfiguration
	 *
	 * @param jbpmConfiguration - current jbpmConfiguration
	 */
	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}

	@Override
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
	@Override
	public void setLyfecycleVoters(List<LyfecycleVoter> lyfecycleVoters) {
		this.lyfecycleVoters.addAll(lyfecycleVoters);
	}

	@Required
	public void setProcessDao(ProcessDao processDao) {
		this.processDao = processDao;
	}

}
