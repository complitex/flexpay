package org.flexpay.common.process;

import org.flexpay.common.exception.FlexPayException;
//import org.flexpay.common.logger.FPLogger;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessManagerConfigurationException;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.job.JobManager;
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
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;


public class ProcessManager implements Runnable {
	/**
	 * singleton instance
	 */
	protected volatile static ProcessManager instance;
    private volatile static Thread localThread;
    private volatile boolean stop = false;
    private volatile Object sleepSemaphore = new Object();
    private static final Logger log  = Logger.getLogger(ProcessManager.class);


    private HashMap<Long, Process> running = new HashMap<Long, Process>();
//	private HashMap<Long, Process> waiting = new HashMap<Long, Process>();

	public static final int RESCAN_FREQ = 10000; //@todo into global
	public static final int MAXIMUM_PROCESS_THREADS = 10; //@todo into global
    public static final String PROCESS_INSTANCE_ID = "ProcessInstanceID";
    private static final int startTaskLimit = 10; //@todo into global

    private JbpmConfiguration jbpmConfiguration = null;
//	protected LoggingSession loggingSession = null;



    /**
	 * protected constructor
	 */
	protected  ProcessManager() {
//        this.jbpmConfiguration = JbpmConfiguration.getInstance();
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
      * @return ProcessManager
     */
    public static synchronized void startProcessManager(){
        log.debug( "Starting ProcessManager thread");
        if (localThread == null || !localThread.isAlive()){
            ProcessManager pmInstance = getInstance();
            localThread = new Thread (pmInstance, "ProcessManager Thread");
            localThread.start();
            log.debug( "ProcessManager thread started");
//            return pmInstance;
//        }else{
//            return getInstance();
        } else {
            log.debug( "ProcessManager thread already started");
        }
    }

    /**
     * Unload process manager and stop process manager thread
     */
    public static synchronized void unload() {
        log.debug( "Stoping ProcessManager thread");
        if (instance != null && localThread != null) {
            instance.stopProcessManager();
            try {
                localThread.join();
            } catch (InterruptedException e) {
                log.debug( "can't stop ProcessManager thread");
            }
            instance = null;
            localThread = null;
        }
        log.debug( "ProcessManager thread stoped ");
    }

    /**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @param replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deplot process definition to jbpm
	 * @throws ProcessManagerConfigurationException
	 *                                    when misconfiguration present
	 */
	public long deployProcessDefinition(String name, boolean replace)
			throws ProcessDefinitionException, ProcessManagerConfigurationException {

		InputStream inputStream;
		try {
			inputStream = ProcessManagerConfiguration.getProcessDefinitionOSByName(name); //new FileInputStream("c:\\processDefinition.xml");
		} catch (FileNotFoundException e) {
			log.error("ProcessManager: process definition for name " + name + "file not found!");
			throw new ProcessManagerConfigurationException(e);
		}
		return deployProcessDefinition(inputStream, replace);
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
				throw new ProcessDefinitionException("ProcessManager: InputStream is not process definition file");
			} else {
				throw new ProcessDefinitionException("ProcessManager: Can't deploy processDefinition for " + processDefinition.getName());
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
				log.info("Process definition not found. Deploying " + processDefinition.getName() + "...");
				newVersion = 1;
				processDefinition.setVersion(newVersion);
			} else {
				int oldVersion = latestProcessDefinition.getVersion();
				log.info("Deploying new version of process definition " + processDefinition.getName() + "...");
				newVersion = oldVersion + 1;
				processDefinition.setVersion(newVersion);
				log.info("Old version = " + oldVersion + " New version = " + newVersion);
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
	public void run() {
		log.debug("Starting process manager...");
		while (!isStop()) {
			try {
				//write tick-tack message
				log.debug("Collecting task instances to run.");
				//find not running task instances and run
				JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
				for (Object o : jbpmContext.getTaskMgmtSession().findTaskInstances(JobManagerAssignmentHandler.JOB_MANAGER_ACTOR_NAME)) {
					// are there any not running taskInstances?
					TaskInstance taskInstance = (TaskInstance) o;
					taskInstance = jbpmContext.getTaskMgmtSession().loadTaskInstance(taskInstance.getId());
					if (!taskInstance.hasEnded()) {
						startTask(taskInstance);
					}
				}
				jbpmContext.close();
				//all task instances started, going to sleep
				synchronized (sleepSemaphore) {
					try {
						sleepSemaphore.wait(RESCAN_FREQ);
                        sleepSemaphore.notify();
                    } catch (InterruptedException e) {
						log.debug("ProcessManager.run: Somebody interrupts me.", e);
						Thread.interrupted(); // clear "interrupted" state of thread
					}
				}
			} catch (Throwable e) {
				log.debug("ProcessManager mainLoop : ", e);
			}
		}
		log.debug( "process manager stoped...");
	}

	/**
	 * Stops ProcessManager execution
	 */
	public synchronized void stopProcessManager() {
		stop = true;
	}

	/**
	 * @return true if ProcessManager id not running
	 */
	public synchronized boolean isStop() {
		return stop;
	}

	/**
	 * Init process by process definition name
	 *
	 * @param processDefinitionName name of process definition
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when process definition has an error
	 * @throws ProcessInstanceException   when jbpm can't instanciate process from process definition
	 */
	public synchronized Long initProcess(String processDefinitionName)
			throws ProcessDefinitionException, ProcessInstanceException {
		ProcessDefinition processDefinition;
		Long processId;
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		try {
			processDefinition = graphSession.findLatestProcessDefinition(processDefinitionName);
		} catch (RuntimeException e) {
			jbpmContext.close();
			log.error("initProcess: findLatestProcessDefinition", e);
			throw new ProcessDefinitionException("Can't access ProcessDefinition for " + processDefinitionName);

		}
		if (processDefinition == null) {
			log.error("initProcess: Can't find process definition for name: " + processDefinitionName);
			throw new ProcessDefinitionException("initProcess: Can't find process definition for name: " + processDefinitionName);
		}

		log.error("initProcess: Initializing  process. Process Definition id = " + processDefinition.getId() + " name = " + processDefinition.getName() + " version = " + processDefinition.getVersion());

		try {
			ProcessInstance processInstance = new ProcessInstance(processDefinition);
			processId = processInstance.getId();
		} catch (RuntimeException e) {
			jbpmContext.close();
			log.error("initProcess: ProcessInstanceCreation", e);
			throw new ProcessInstanceException("Can't create ProcessInstance for " + processDefinitionName);
		}
		jbpmContext.close();
		return processId;
	}

	/**
	 * Return process context parameters
	 *
	 * @param processID process ID
	 * @return HashMap with process context parameters
	 * @throws ProcessInstanceException when can't get process parameters
	 */
	public synchronized HashMap getProcessParameters(Long processID) throws ProcessInstanceException {
		HashMap result = null;
		GraphSession graphSession = jbpmConfiguration.createJbpmContext().getGraphSession();
		try {
			ProcessInstance processInstance = graphSession.loadProcessInstance(processID);
			if (processInstance != null) {
				ContextInstance contextInstance = processInstance.getContextInstance();
				if (contextInstance != null) {
					result = (HashMap) contextInstance.getVariables();
				}
			}
		} catch (RuntimeException e) {
			log.error("ProcessManager: getProcessParameters: ", e);
			throw new ProcessInstanceException("ProcessManager: Can't get Process Dictionary for " + processID);
		}
		return result;
	}

	/**
	 * Return all process definitions
	 *
	 * @return List with process definition IDs
	 * @throws ProcessInstanceException when runtime exception caught
	 */
	public List<Long> getProcessIDList() throws ProcessInstanceException {
		Vector<Long> result = new Vector<Long>();
		try {
			GraphSession graphSession = jbpmConfiguration.createJbpmContext().getGraphSession();
			List processDefinitionList = graphSession.findAllProcessDefinitions();
			for (Object def1 : processDefinitionList) {
				ProcessDefinition processDefinition = (ProcessDefinition) def1;
				List procs = graphSession.findProcessInstances(processDefinition.getId());
				for (Object proc1 : procs) {
					ProcessInstance processInstance = (ProcessInstance) proc1;
					result.addElement(processInstance.getId());
				}
			}
		} catch (RuntimeException e) {
			log.error("ProcessManager: getProcessIDList: ", e);
			throw new ProcessInstanceException("Can't get Process List");
		}
		return result;
	}

    /**
     * Create process for process definition name
     * @param processDefinitionName process definitiona name
     * @param parameters initial context variables
     * @throws ProcessInstanceException when can't instantiate process instance
     * @throws ProcessDefinitionException when process definition not found
     */
    public synchronized void createProcess(String processDefinitionName, Map<Serializable, Serializable> parameters) throws ProcessInstanceException, ProcessDefinitionException {
		long processInstanceID = initProcess(processDefinitionName);
		//starting process
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		ProcessInstance processInstance = graphSession.loadProcessInstance(processInstanceID);

		ContextInstance ci = processInstance.getContextInstance();
		if (parameters == null) {
			parameters = new HashMap<Serializable, Serializable>();
		}
		parameters.put(PROCESS_INSTANCE_ID, String.valueOf(processInstanceID));
		ci.addVariables(parameters);
		Token token = processInstance.getRootToken();
        token.signal();
		jbpmContext.close();
		log.info("initProcess: Process Instance id = " + processInstanceID + " started.");

//        waiting.put((long) 1, process);
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
		ProcessInstance pi = task.getTaskMgmtInstance().getProcessInstance();
		ContextInstance ci = pi.getContextInstance();
		Integer startTaskCounter = (Integer) ci.getVariable("StartTaskCounter", task.getToken());
		if (startTaskCounter == null) {
			startTaskCounter = 1;
		} else {
			startTaskCounter = startTaskCounter + 1;
		}
		ci.setVariable("StartTaskCounter", startTaskCounter, task.getToken());

		String ti = String.valueOf(task.getId());
		String pid = String.valueOf(pi.getId());

		if (startTaskCounter <= startTaskLimit) {
			HashMap<Serializable, Serializable> params = (HashMap<Serializable, Serializable>) ci.getVariables();

			log.info("Starting task \"" + task.getName() + "\" (" + ti + ", pid - " + pid + ")");

			if (null == task.getStart()) {
				task.start();
			} else {
				log.info("Task \"" + task.getName() + "\" (" + ti + ", pid - " + pid + ")" + " restarted. Recovering from failure.");
			}

			try {
				JobManager.getInstance().addJob(pi.getId(), task.getId(), task.getName(), params);
			} catch (FlexPayException e) {
				log.error("ProcessManager: startTask: can't start task with name " + task.getName(), e);
				return false;
			}
			return true;
		} else {
			log.info("Exceded limit (" + startTaskLimit + ") of starting task \"" + task.getName() + "\" (" + ti + ", pid - " + pid + "). Process ended.");
			pi.end();
			task.end(Job.RESULT_ERROR);
			//set status to failed
			ci.setVariable(Job.RESULT_ERROR, ProcessState.COMPLITED_WITH_ERRORS);
			//remove from running tasks
			running.remove(Long.valueOf(pi.getId()));
			return false;
		}
	}

	/**
	 * Called when process job was finished
	 *
	 * @param taskId	 Task ID
	 * @param parameters Task context parameters
	 * @param transition transition name
	 */
	public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
		// this method called by Job to report finish
		log.debug("ProcessManager: jobFinished: taskId: " + taskId);

		boolean proceed = false;
		JbpmContext jbpmContext;
		TaskMgmtSession taskMgmtSession;

		while (!proceed) {
			try {
				jbpmContext = jbpmConfiguration.createJbpmContext();
				taskMgmtSession = jbpmContext.getTaskMgmtSession();
				TaskInstance task;

				task = taskMgmtSession.loadTaskInstance(taskId);

				if (task == null) {
					log.error("ProcessManager: jobFinished: Can't find Task Instance, id: " + taskId);
				} else {
					log.info("ProcessManager: jobFinished: Finishing Task Instance, id: " + taskId);
					ContextInstance ci = task.getTaskMgmtInstance().getProcessInstance().getContextInstance();
					// save the variables in ProcessInstance dictionary
					ci.addVariables(parameters);
					ci.setVariable("StartTaskCounter", 0, task.getToken());
					// mark task as ended with job result code as decision transition value
					task.end(transition);
				}
				jbpmContext.close();
				proceed = true;
				synchronized (sleepSemaphore) {
					Object removed = running.remove(taskId);
					log.debug("ProcessManager: jobFinished: Task removed from list of running tasks: " + removed);
					log.debug("ProcessManager: jobFinished: Number of running tasks: " + running.size());
					sleepSemaphore.notify();
				}
			} catch (RuntimeException e) {
				log.error("ProcessManager: jobFinished: Failed to finish task: " + taskId, e);
				log.error("ProcessManager: jobFinished: Sleeping 30 sec and try again to finish task: " + taskId);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException ie) {
					log.fatal("ProcessManager: jobFinished: System failure when finishing task: " + taskId, e);
				}
			}
		}
	}

	/**
	 * Start process by processID
	 *
	 * @param processID  process ID
	 * @param parameters process parameters
	 * @throws ProcessInstanceException when can't start process
	 */
	public void startProcess(Long processID, HashMap<Serializable, Serializable> parameters)
			throws ProcessInstanceException {
		try {
			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
			GraphSession graphSession = jbpmContext.getGraphSession();
			ProcessInstance processInstance = graphSession.loadProcessInstance(processID);

			ContextInstance ci = processInstance.getContextInstance();
			if (parameters == null) {
				parameters = new HashMap<Serializable, Serializable>();
			}
			parameters.put(PROCESS_INSTANCE_ID, String.valueOf(parameters));
			ci.addVariables(parameters);
			Token token = processInstance.getRootToken();
			token.signal();
			log.info( "initProcess: Process Instance id = " + processID.toString() + " started.");
			jbpmContext.close();
		} catch (RuntimeException e) {
			log.error("initProcess: ProcessInstanceCreation", e);
			throw new ProcessInstanceException("Can't start ProcessInstance pid - " + processID.toString());
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
					log.debug("ProcessManager.removeProcess: removeProcess: removed process definition " + processDefinitionID);
				}
			}
			jbpmContext.close();
		} catch (RuntimeException e) {
			log.error("ProcessManager.removeProcess: ", e);
			throw new ProcessInstanceException("Can't remove ProcessInstance for " + processID.toString());
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

	public List<Process> getProcessList() {
		ArrayList<Process> processList = new ArrayList<Process>();
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		List<ProcessDefinition> processDefinitionList = graphSession.findAllProcessDefinitions();
		for (ProcessDefinition processDefinition : processDefinitionList) {
			List<ProcessInstance> processInstanceList = graphSession.findProcessInstances(processDefinition.getId());
			for (ProcessInstance processInstance : processInstanceList) {
				Process process = new Process();
                process.setId(processInstance.getId());
                process.setProcessDefinitionName(processInstance.getProcessDefinition().getName());
				process.setProcessEndDate(processInstance.getEnd());
				process.setProcessStartDate(processInstance.getStart());
				process.setProcessDefenitionVersion(processInstance.getProcessDefinition().getVersion());
				Map parameters = processInstance.getContextInstance().getVariables();
				if (parameters == null) {
					process.setParameters(new HashMap<Serializable, Serializable>());
				} else {
					process.setParameters(parameters);
				}
				processList.add(process);
			}
		}
		jbpmContext.close();
		return processList;
	}

    @NotNull
    public Process getProcessInastanceInfo(@NotNull Long processId) {
        JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
        Process process = new Process();
        ProcessInstance processInstance = jbpmContext.getProcessInstance(processId);
        if (processInstance != null) {
            process.setId(processInstance.getId());
            process.setProcessDefinitionName(processInstance.getProcessDefinition().getName());
            process.setProcessEndDate(processInstance.getEnd());
            process.setProcessStartDate(processInstance.getStart());
            process.setProcessDefenitionVersion(processInstance.getProcessDefinition().getVersion());
            Map parameters = processInstance.getContextInstance().getVariables();
            if (parameters == null) {
                process.setParameters(new HashMap<Serializable, Serializable>());
            } else {
                process.setParameters(parameters);
            }
        }
        return process;
    }
}
