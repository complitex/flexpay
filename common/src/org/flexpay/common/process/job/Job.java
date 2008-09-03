package org.flexpay.common.process.job;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.logger.FPLogger;
import org.flexpay.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Job implements Runnable {

	public final static String RESULT_NEXT = "next";
	public final static String RESULT_ERROR = "error";
	public final static String STATUS_ERROR = "ERROR_STATUS";

	private Thread jobThread = null;
	private String id;
	private Date start;
	private Date end;
	private Map<Serializable, Serializable> parameters = CollectionUtils.map();
	private Long taskId;
	private Long processId;
	private Logger log = Logger.getLogger(Job.class);

	/**
	 * object for generate random string of transaction id (job id)
	 */
	private final static Random random = new Random();

	public Job() {
		synchronized (random) {
			long n = random.nextLong();
			this.id = Long.toString(Math.abs(n), Character.MAX_RADIX);
		}
	}

	public void run() {

		JobManager jobMgr = JobManager.getInstance();
		setStart(new Date());
		FPLogger.logMessage(FPLogger.INFO, "Job " + getId() + " started");

		try {
			String transition = this.execute(parameters);
			FPLogger.logMessage(FPLogger.INFO, "Job with id = " + getId() + " completed with status: " + transition);
			if (transition.equals(RESULT_ERROR)) {
				parameters.put(STATUS_ERROR, Boolean.TRUE);
			}
			jobMgr.jobFinished(id, transition);
		} catch (Throwable e) {
			FPLogger.logMessage(FPLogger.ERROR, "Job with id = " + getId() + " completed with exception", e);
			parameters.put(STATUS_ERROR, Boolean.TRUE);
			setEnd(new Date());
			jobMgr.jobFinished(getId(), RESULT_ERROR);
		}
		setEnd(new Date());
	}

	public Thread startThread(Map<Serializable, Serializable> parameters) {

		if (this.jobThread == null) {
			this.parameters.putAll(parameters);
			jobThread = new Thread(this, "JobThread-" + id);
			jobThread.start();
		} else {
			log.fatal("Job thread already started! Check Job Bean configuration. Scope must be prototype!");
		}

		return jobThread;
	}

	public abstract String execute(Map<Serializable, Serializable> parameters) throws FlexPayException;

	public Thread getJobThread() {
		return jobThread;
	}

	public void setJobThread(Thread jobThread) {
		this.jobThread = jobThread;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Map<Serializable, Serializable> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<Serializable, Serializable> parameters) {
		this.parameters.putAll(parameters);
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}
}
