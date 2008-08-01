package org.flexpay.common.process.job;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.*;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.BeansException;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;


public class JobManager implements BeanFactoryAware {

    volatile protected static JobManager instance = null;
    private volatile BeanFactory beanFactory;
    Logger log = Logger.getLogger(JobManager.class);
    private Map<String,Job> runningJobs = new Hashtable<String, Job>();
    private Map<String,Job> sleepingJobs = new Hashtable<String, Job>();
    private LinkedList<Job> waitingJobs = new LinkedList<Job>();

    private Hashtable<String, Map<Serializable, Serializable>> jobParameters = new Hashtable<String, Map<Serializable, Serializable>>();

    private int MAXIMUM_RUNNING_JOBS = 10;

    
    protected JobManager(){

    }

    public synchronized static JobManager getInstance(){
        if (instance == null) instance = new JobManager();
        return instance;
    }

    public synchronized List<Job> getJobList(){
        List<Job> result = new ArrayList<Job>();
        result.addAll(runningJobs.values());
        result.addAll(sleepingJobs.values());
        result.addAll(waitingJobs);
        return result;
    }

    
    public synchronized void reload(){
        runningJobs = new Hashtable<String, Job>();
        sleepingJobs = new Hashtable<String, Job>();
        waitingJobs = new LinkedList<Job>();
    }

    public synchronized static void unload(){
        instance = null;
    }

    private synchronized String start(Job job, Map <Serializable, Serializable> parameters) {
        if (runningJobs.containsKey(job.getId())) {
            job.getJobThread().start();
        }
        job.startThread(parameters);
        runningJobs.put(job.getId(), job);
        return job.getId();
    }

    private synchronized void runNextJob() {
        if (waitingJobs.size() > 0) {
            Job nextJob = waitingJobs.getFirst();
            waitingJobs.remove(nextJob);
            this.start(nextJob, jobParameters.get(nextJob.getId()));
        }
    }



    public void jobFinished(String id, String transition) {
        Job job = runningJobs.get(id);
        if (job == null) {
            job = sleepingJobs.get(id);
        }

        ProcessManager pm = ProcessManager.getInstance();
        pm.jobFinished(job.getTaskId(), jobParameters.get(id), transition);

        jobParameters.remove(id);
        if (runningJobs.get(id) != null){
            runningJobs.remove(id);
        } else {
            sleepingJobs.remove(id);
        }
        runNextJob();
    }


    public synchronized final void addJob(Job job, Map<Serializable, Serializable> param) {
        if (runningJobs.size() < MAXIMUM_RUNNING_JOBS) {
            this.start(job, param);
        } else {
            waitingJobs.addLast(job);
        }
        jobParameters.put(job.getId(), param);
    }

    public synchronized boolean addJob(long processId, long taskId, String jobName, HashMap<Serializable, Serializable> parameters)
        throws JobInstantiationException, JobClassNotFoundException, JobConfigurationNotFoundException{

        if (beanFactory.containsBean(jobName)){
            try {
                Job job = (Job) beanFactory.getBean(jobName);
                job.setTaskId(taskId);
                job.setProcessId(processId);
                addJob(job, parameters);
                log.info("JobManager.addJob: Job " + jobName + " was added. Id=" + job.getId());
            } catch (ClassCastException e){
                log.fatal("JobManager.addJob: Illegal exception when creating instance of " + jobName, e);
                throw new JobInstantiationException("Illegal exception when creating instance of " + jobName,e);
            } catch (BeansException e){
                log.fatal("JobManager.addJob: Illegal exception when creating instance of " + jobName, e);
                throw new JobInstantiationException("Illegal exception when creating instance of " + jobName,e);
            }
        }   else {
            throw new JobConfigurationNotFoundException("Job bean is not configured");
        }
        return true;
    }

    public synchronized void setSleepStateOn(String jobId) {
        Job job = runningJobs.get(jobId);
        if (job != null) {
            sleepingJobs.put(jobId,job);
            runningJobs.remove(jobId);
        }
    }

    public synchronized void setSleepStateOff(String jobId) {
        Job job = sleepingJobs.get(jobId);
        if (job != null) {
            runningJobs.put(jobId,job);
            sleepingJobs.remove(jobId);
        }
    }

    public boolean isRunning(Long taskID) {
        List<Job> jobs = getJobList();
        for (Job job : jobs) {
          if (job.getTaskId().equals(taskID)) {
            return true;
          }
        }
        return false;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
