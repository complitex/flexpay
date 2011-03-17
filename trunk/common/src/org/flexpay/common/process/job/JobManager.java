package org.flexpay.common.process.job;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessManagerImpl;
import org.flexpay.common.process.exception.JobConfigurationNotFoundException;
import org.flexpay.common.process.exception.JobInstantiationException;
import org.flexpay.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.*;

import static org.flexpay.common.util.CollectionUtils.list;


public class JobManager implements BeanFactoryAware {

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
		List<Job> result = list();
		result.addAll(runningJobs.values());
		result.addAll(sleepingJobs.values());
		result.addAll(waitingJobs);
		return result;
	}

	public static void unload() {
		instance = null;
	}

	private String start(Job job, Map<Serializable, Serializable> parameters) {
		synchronized (ProcessManagerImpl.getInstance()) {
			synchronized (this) {

				if (runningJobs.containsKey(job.getId())) {
					job.getJobThread().start();
					//noinspection ThrowableInstanceNeverThrown
					log.error("AHTUNG, WTF?", new RuntimeException());
				}
				runningJobs.put(job.getId(), job);
				if (log.isDebugEnabled()) {
					log.debug("Starting job, task-id {}, id {}", job.getTaskId(), job.getId());
					log.debug("Running jobs: {}", runningJobs.keySet());
				}
				job.startThread(parameters);
				return job.getId();
			}
		}
	}

	private void runNextJob() {

		synchronized (ProcessManagerImpl.getInstance()) {
			synchronized (this) {
				if (!waitingJobs.isEmpty()) {
					Job nextJob = waitingJobs.getFirst();
					waitingJobs.remove(nextJob);
					this.start(nextJob, jobParameters.get(nextJob.getId()));
				}
			}
		}
	}

	public void jobFinished(String id, String transition, Map<Serializable, Serializable> parameters) {

		synchronized (ProcessManagerImpl.getInstance()) {
			synchronized (this) {
				if (log.isDebugEnabled()) {
					log.debug("Ending job, id {}", id);
					log.debug("Running jobs: {}", runningJobs.keySet());
				}
				Job job = runningJobs.get(id);
				if (job == null) {
					job = sleepingJobs.get(id);
					if (job == null) {
						log.warn("Know nothing about job #{}", id);
						throw new RuntimeException("Know nothing about job #" + id);
					}
				}

				processManager.taskCompleted(job.getTaskId(), parameters, transition);

				jobParameters.remove(id);
				runningJobs.remove(id);
				sleepingJobs.remove(id);
				runNextJob();
			}
		}
	}


	public final void addJob(Job job, Map<Serializable, Serializable> param) {

		synchronized (ProcessManagerImpl.getInstance()) {
			synchronized (this) {
				if (runningJobs.size() < maximumRunningJobs) {
					start(job, param);
				} else {
					waitingJobs.addLast(job);
				}
				jobParameters.put(job.getId(), param);
			}
		}
	}

	public boolean addJob(long processId, long taskId, String jobName, Map<Serializable, Serializable> parameters)
			throws JobInstantiationException, JobConfigurationNotFoundException {

		synchronized (ProcessManagerImpl.getInstance()) {
			synchronized (this) {
				if (beanFactory.containsBean(jobName)) {
					try {
						Job job = (Job) beanFactory.getBean(jobName);
						job.setTaskId(taskId);
						job.setProcessId(processId);
						addJob(job, parameters);
						log.info("Job {} was added. Id = {}", jobName, job.getId());
					} catch (ClassCastException e) {
						log.error("Illegal exception when creating instance of " + jobName, e);
						throw new JobInstantiationException("Illegal exception when creating instance of " + jobName, e,
								"error.common.jm.job_create_failed", jobName);
					} catch (BeansException e) {
						log.error("Illegal exception when creating instance of " + jobName, e);
						throw new JobInstantiationException("Illegal exception when creating instance of " + jobName, e,
								"error.common.jm.job_create_failed", jobName);
					}
				} else {
					throw new JobConfigurationNotFoundException("Job bean is not configured " + jobName,
							"error.common.jm.job_not_found", jobName);
				}
				return true;
			}
		}
	}

	/**
	 * Method setBeanFactory sets the beanFactory of this JobManager object.
	 *
	 * @param beanFactory the beanFactory of this JobManager object.
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void setMaximumRunningJobs(int maximumRunningJobs) {
		this.maximumRunningJobs = maximumRunningJobs;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
