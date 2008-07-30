package org.flexpay.common.process;

import org.flexpay.common.logger.FPLogger;
import org.flexpay.common.process.job.JobManager;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.exception.*;
import org.flexpay.common.exception.FlexPayException;
import org.jbpm.db.*;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.def.ProcessDefinition;

import java.util.*;
import java.io.Serializable;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.*;


public class ProcessManager implements Runnable {
    /**
     * singleton instance
     */
    protected static ProcessManager instance;
    public static final String PROCESS_INSTANCE_ID = "ProcessInstanceID";

    private volatile boolean stop = false;
    private HashMap<Long, Process> running = new HashMap<Long, Process>();
    private HashMap<Long, Process> waiting = new HashMap<Long, Process>();
    private final Object sleepSemaphore = new Object();
    public static final int RESCAN_FREQ = 10000; //@todo into global
    public int MAXIMUM_PROCESS_THREADS = 10; //@todo into global

    private JbpmConfiguration jbpmConfiguration = null;
    protected LoggingSession loggingSession = null;
    private int startTaskLimit = 10; //@todo into global

    /**
     * protected constructor
     */
    protected ProcessManager() {
//        this.jbpmConfiguration = JbpmConfiguration.getInstance();
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
     * Deploys process definition to jbpm by process definition name
     *
     * @param name    name of process definition
     * @param replace if true old process definition should be removed with new one
     * @return ID of process definition
     * @throws ProcessDefinitionException when can't deplot process definition to jbpm
     * @throws ProcessManagerConfigurationException
     *                                    when misconfiguration present
     */
    public long deployProcessDefinition(String name, boolean replace)
            throws ProcessDefinitionException, ProcessManagerConfigurationException {

        InputStream inputStream = null;
        try {
            inputStream = ProcessManagerConfiguration.getProcessDefinitionOSByName(name); //new FileInputStream("c:\\processDefinition.xml");
        } catch (FileNotFoundException e) {
            FPLogger.logMessage(FPLogger.ERROR, "ProcessManager: process definition for name " + name + "file not found!");
            throw new ProcessManagerConfigurationException(e);
        }
        return deployProcessDefinition(inputStream, replace);
    }

    /**
     * Deploys process definition to jbpm from inputStream
     *
     * @param in      input stream with process definition
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
            FPLogger.logMessage(FPLogger.ERROR, "deployProcessDefinition: ", e);
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
     * @param replace           replace replace if true old process definition should be removed with new one
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
                FPLogger.logMessage(FPLogger.INFO, "Process definition not found. Deploying " + processDefinition.getName() + "...");
                newVersion = 1;
                processDefinition.setVersion(newVersion);
            } else {
                int oldVersion = latestProcessDefinition.getVersion();
                FPLogger.logMessage(FPLogger.INFO, "Deploying new version of process definition " + processDefinition.getName() + "...");
                newVersion = oldVersion + 1;
                processDefinition.setVersion(newVersion);
                FPLogger.logMessage(FPLogger.INFO, "Old version = " + oldVersion + " New version = " + newVersion);
            }
            graphSession.saveProcessDefinition(processDefinition);
            FPLogger.logMessage(FPLogger.INFO, "Deployed.");
        }
        //close JbpmContext and commit transaction
        jbpmContext.close();
        return processDefinition.getId();
    }

    /**
     * main loop
     */
    public void run() {
        FPLogger.logMessage(FPLogger.DEBUG, "Starting process manager...");
        while (!isStop()) {
            try {
                //write tick-tack message
                FPLogger.logMessage(FPLogger.DEBUG, "tick...");
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
                    } catch (InterruptedException e) {
                        FPLogger.logMessage(FPLogger.ERROR, "ProcessManager.run: Somebody interrupts me.", e);
                        Thread.interrupted(); // clear "interrupted" state of thread
                    }
                }
            } catch (Throwable e) {
                FPLogger.logMessage(FPLogger.ERROR, "ProcessManager mainLoop : ", e);
            }
        }
        FPLogger.logMessage(FPLogger.DEBUG, "process manager stoped...");
    }

    /**
     * Stops ProcessManager execution
     */
    public synchronized void stop() {
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
            FPLogger.logMessage(FPLogger.ERROR, "initProcess: findLatestProcessDefinition", e);
            throw new ProcessDefinitionException("Can't access ProcessDefinition for " + processDefinitionName);

        }
        if (processDefinition == null) {
            FPLogger.logMessage(FPLogger.ERROR, "initProcess: Can't find process definition for name: " + processDefinitionName);
            throw new ProcessDefinitionException("initProcess: Can't find process definition for name: " + processDefinitionName);
        }

        FPLogger.logMessage(FPLogger.INFO, "initProcess: Initializing  process. Process Definition id = " + processDefinition.getId() + " name = " + processDefinition.getName() + " version = " + processDefinition.getVersion());

        try {
            ProcessInstance processInstance = new ProcessInstance(processDefinition);
            processId = processInstance.getId();
        } catch (RuntimeException e) {
            jbpmContext.close();
            FPLogger.logMessage(FPLogger.ERROR, "initProcess: ProcessInstanceCreation", e);
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
            FPLogger.logMessage(FPLogger.ERROR, "ProcessManager: getProcessParameters: ", e);
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
            FPLogger.logMessage(FPLogger.ERROR, "ProcessManager: getProcessIDList: ", e);
            throw new ProcessInstanceException("Can't get Process List");
        }
        return result;
    }

    /**
     *
     */
    public synchronized void createProcess(String processDefinitionName, Map<Serializable, Serializable> parameters) throws ProcessInstanceException, ProcessDefinitionException {
//        Process process = new Process(processDefinitionName);
        long processInstanceID = initProcess(processDefinitionName);
//        process.setId(processInstanceID);

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
        FPLogger.logMessage(FPLogger.INFO, "initProcess: Process Instance id = " + processInstanceID + " started.");

        //@TODO method body
//        waiting.put((long) 1, process);
    }

    /**
     * Start task instance
     *
     * @param task taskInstance to start
     * @return true if task started
     */
    @SuppressWarnings({"unchecked"})
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

            FPLogger.logMessage(FPLogger.INFO, "Starting task \"" + task.getName() + "\" (" + ti + ", pid - " + pid + ")");

            if (null == task.getStart()) {
                task.start();
            } else {
                FPLogger.logMessage(FPLogger.INFO, "Task \"" + task.getName() + "\" (" + ti + ", pid - " + pid + ")" + " restarted. Recovering from failure.");
            }

            try {
                JobManager.getInstance().addJob(pi.getId(), task.getId(), task.getName(), params);
            } catch (FlexPayException e) {
                FPLogger.logMessage(FPLogger.ERROR,"ProcessManager: startTask: can't start task with name "+task.getName(),e);
                return false;
            }
            return true;
        } else {
            FPLogger.logMessage(FPLogger.INFO, "Exceded limit (" + startTaskLimit + ") of starting task \"" + task.getName() + "\" (" + ti + ", pid - " + pid + "). Process ended.");
            pi.end();
            task.end();
            //set status to failed
            ci.setVariable(Job.ERROR, ProcessState.COMPLITED_WITH_ERRORS);
            //remove from running tasks
            running.remove(Long.valueOf(pi.getId()));
            return false;
        }
    }

    /**
     * Called when process job was finished
     *
     * @param taskId     Task ID
     * @param parameters Task context parameters
     * @param transition transition name
     */
    public synchronized void jobFinished(Long taskId, HashMap<Serializable, Serializable> parameters, String transition) {
        // this method called by Job to report finish
        FPLogger.logMessage(FPLogger.DEBUG, "ProcessManager: jobFinished: taskId: " + taskId);

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
                    FPLogger.logMessage(FPLogger.ERROR, "ProcessManager: jobFinished: Can't find Task Instance, id: " + taskId);
                } else {
                    FPLogger.logMessage(FPLogger.INFO, "ProcessManager: jobFinished: Finishing Task Instance, id: " + taskId);
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
                    FPLogger.logMessage(FPLogger.DEBUG, "ProcessManager: jobFinished: Task removed from list of running tasks: " + removed);
                    FPLogger.logMessage(FPLogger.DEBUG, "ProcessManager: jobFinished: Number of running tasks: " + running.size());
                    sleepSemaphore.notify();
                }
            } catch (RuntimeException e) {
                FPLogger.logMessage(FPLogger.ERROR, "ProcessManager: jobFinished: Failed to finish task: " + taskId, e);
                FPLogger.logMessage(FPLogger.ERROR, "ProcessManager: jobFinished: Sleeping 30 sec and try again to finish task: " + taskId);
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ie) {
                    FPLogger.logMessage(FPLogger.FATAL, "ProcessManager: jobFinished: System failure when finishing task: " + taskId, e);
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
            FPLogger.logMessage(FPLogger.INFO, "initProcess: Process Instance id = " + processID.toString() + " started.");
            jbpmContext.close();
        } catch (RuntimeException e) {
            FPLogger.logMessage(FPLogger.ERROR, "initProcess: ProcessInstanceCreation", e);
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
                    FPLogger.logMessage(FPLogger.DEBUG, "ProcessManager.removeProcess: removeProcess: removed process definition " + processDefinitionID);
                }
            }
            jbpmContext.close();
        } catch (RuntimeException e) {
            FPLogger.logMessage(FPLogger.ERROR, "ProcessManager.removeProcess: ", e);
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

    /**
     * Unload process manager and stop process manager thread
     */
    public static synchronized void unload() {
        if (instance != null) {
            instance.stop();
            instance = null;
        }
    }

    public List<Process> getProcessList(){
        ArrayList<Process> processList = new ArrayList<Process>();
        JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
        GraphSession graphSession = jbpmContext.getGraphSession();
        List<ProcessDefinition> processDefinitionList = graphSession.findAllProcessDefinitions();
        for (ProcessDefinition processDefinition: processDefinitionList){
            List<ProcessInstance> processInstanceList = graphSession.findProcessInstances(processDefinition.getId());
            for (ProcessInstance processInstance: processInstanceList){
                Process process = new Process();
                process.setProcessDefinitionName(processInstance.getProcessDefinition().getName());
                process.setProcess_end_date(processInstance.getEnd());
                process.setProcess_start_date(processInstance.getStart());
                process.setProcessDefenitionVersion(processInstance.getProcessDefinition().getVersion());
                HashMap prameters = new HashMap();
                prameters.putAll(processInstance.getContextInstance().getVariables());
                process.setParameters(prameters);
                processList.add(process);
            }
        }
        jbpmContext.close();
        return processList;
    }
    /**
             List<WorkflowProcessInstanceInfo> li = new ArrayList<WorkflowProcessInstanceInfo> ();
        //noinspection unchecked
        List <ProcessDefinition> pdList = jbpmSession.getGraphSession().findAllProcessDefinitionVersions(processDefinitionName);
        for (ProcessDefinition pd : pdList){
            //noinspection unchecked
            List <ProcessInstance> piList = jbpmSession.getGraphSession().findProcessInstances(pd.getId());
            for (ProcessInstance process : piList){
              ContextInstance contextInstance = process.getContextInstance();
              String processType = (String) contextInstance.getVariable("PROCESS_TYPE");
              if (processType !=null && processType.equals(PROCESS_TYPE_WORKFLOF)){
                  WorkflowProcessInstanceInfo pInfo = new WorkflowProcessInstanceInfo(
                          new ProcessState(process.getId(),
                                  ProcessInfoHelper.getProcessState(process.getStart(), process.getEnd()),
                                  process.getStart(),
                                  process.getEnd()));
                  pInfo.setWorkflowProcessdefinitionID(pd.getId());
                  pInfo.setName(process.getProcessDefinition().getName());
                  pInfo.setId(process.getId());
                  pInfo.setDescription((String)process.getContextInstance().getVariable(WORKFLOW_PROCESS_DESCRIPTION));
                  li.add(pInfo);
              }
            }
        }
        return li;
     */
}
