package org.flexpay.common.process.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.JobClassNotFoundException;
import org.flexpay.common.process.exception.JobConfigurationNotFoundException;
import org.flexpay.common.process.exception.JobInstantiationException;
import org.flexpay.common.util.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;


public class JobManager implements BeanFactoryAware, InitializingBean {

	private static JobManager instance = null;
	private BeanFactory beanFactory;

	/**
	 * Logger
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

	private Map<String, Job> runningJobs = new Hashtable<String, Job>();
	private Map<String, Job> sleepingJobs = new Hashtable<String, Job>();
	private LinkedList<Job> waitingJobs = new LinkedList<Job>();

	/**
	 * Jobs parameters
	 */
	private Map<String, Map<Serializable, Serializable>> jobParameters = CollectionUtils.map();

	private int maximumRunningJobs = 10;

	private ProcessManager processManager;

	protected JobManager() {

	}

	public synchronized static JobManager getInstance() {
		if (instance == null) {
			instance = new JobManager();
		}
		return instance;
	}

	public synchronized List<Job> getJobList() {
		List<Job> result = new ArrayList<Job>();
		result.addAll(runningJobs.values());
		result.addAll(sleepingJobs.values());
		result.addAll(waitingJobs);
		return result;
	}

	public synchronized void reload() {
		runningJobs = new Hashtable<String, Job>();
		sleepingJobs = new Hashtable<String, Job>();
		waitingJobs = new LinkedList<Job>();
	}

	public synchronized static void unload() {
		instance = null;
	}

	private synchronized String start(Job job, Map<Serializable, Serializable> parameters) {
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
			if (job == null) {
				log.warn("Know nothing about job #{}", id);
				throw new RuntimeException("Know nothing about job #" + id);
			}
		}

		processManager.taskCompleted(job.getTaskId(), jobParameters.get(id), transition);

		jobParameters.remove(id);
		if (runningJobs.get(id) != null) {
			runningJobs.remove(id);
		} else {
			sleepingJobs.remove(id);
		}
		runNextJob();
	}


	public synchronized final void addJob(Job job, Map<Serializable, Serializable> param) {
		if (runningJobs.size() < maximumRunningJobs) {
			start(job, param);
		} else {
			waitingJobs.addLast(job);
		}
		jobParameters.put(job.getId(), param);
	}

	public synchronized boolean addJob(long processId, long taskId, String jobName, Map<Serializable, Serializable> parameters)
			throws JobInstantiationException, JobClassNotFoundException, JobConfigurationNotFoundException {

		if (beanFactory.containsBean(jobName)) {
			try {
				Job job = (Job) beanFactory.getBean(jobName);
				job.setTaskId(taskId);
				job.setProcessId(processId);
				addJob(job, parameters);
				log.info("Job {} was added. Id = {}", jobName, job.getId());
			} catch (ClassCastException e) {
				log.error("Illegal exception when creating instance of {}", jobName, e);
				throw new JobInstantiationException("Illegal exception when creating instance of " + jobName, e);
			} catch (BeansException e) {
				log.error("Illegal exception when creating instance of {}", jobName, e);
				throw new JobInstantiationException("Illegal exception when creating instance of " + jobName, e);
			}
		} else {
			throw new JobConfigurationNotFoundException("Job bean is not configured");
		}
		return true;
	}

	public synchronized void setSleepStateOn(String jobId) {
		Job job = runningJobs.get(jobId);
		if (job != null) {
			sleepingJobs.put(jobId, job);
			runningJobs.remove(jobId);
		}
	}

	public synchronized void setSleepStateOff(String jobId) {
		Job job = sleepingJobs.get(jobId);
		if (job != null) {
			runningJobs.put(jobId, job);
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

	/**
	 * Method setBeanFactory sets the beanFactory of this JobManager object.
	 *
	 * @param beanFactory the beanFactory of this JobManager object.
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * Invoked by a BeanFactory after it has set all bean properties supplied (and satisfied BeanFactoryAware and
	 * ApplicationContextAware). <p>This method allows the bean instance to perform initialization only possible when all
	 * bean properties have been set and to throw an exception in the event of misconfiguration.
	 *
	 * @throws Exception in the event of misconfiguration (such as failure to set an essential property) or if
	 *                   initialization fails.
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(processManager, "Process manager was not set");
	}

	public void setMaximumRunningJobs(int maximumRunningJobs) {
		this.maximumRunningJobs = maximumRunningJobs;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
