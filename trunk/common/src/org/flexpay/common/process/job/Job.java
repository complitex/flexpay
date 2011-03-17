package org.flexpay.common.process.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.job.listeners.JobCompletePercentListener;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Job implements Runnable {

	protected final Logger log = LoggerFactory.getLogger(getClass());

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

	/**
	 * object for generate random string of transaction id (job id)
	 */
	private final static Random random = new Random();

	/**
	 * Constructor Job creates a new Job instance.
	 */
	public Job() {
		this.id = Long.toString(Math.abs(random.nextLong()), Character.MAX_RADIX);
	}

    @Override
	public void run() {

		JobManager jobMgr = JobManager.getInstance();
		setStart(new Date());

		// prepare process logger
		ProcessLogger.setThreadProcessId(processId);

		Logger plog = ProcessLogger.getLogger(getClass());
		plog.info("Job {} started", getId());

		try {

			// setup security context
			Authentication auth = (Authentication) parameters.get(ProcessManager.PARAM_SECURITY_CONTEXT);
			SecurityContextHolder.getContext().setAuthentication(auth);

			// setup execution context
			JobExecutionContext jobExecutionContext = new JobExecutionContext(taskId != null ? taskId : 0L);
			jobExecutionContext.addListener(new JobCompletePercentListener());
			JobExecutionContextHolder.setContext(jobExecutionContext);

			// execute
			String transition = execute(parameters);

			plog.info("Job with id = {} completed with status: {}", getId(), transition);

			if (transition.equals(RESULT_ERROR)) {
				parameters.put(STATUS_ERROR, Boolean.TRUE);
			}

			jobMgr.jobFinished(id, transition, parameters);
		} catch (Throwable e) {
			if (e instanceof FlexPayException) {
				FlexPayException ex = (FlexPayException) e;
				ex.error(plog, "Job with id #{} completed with exception", getId());
			} else if (e instanceof FlexPayExceptionContainer) {
				FlexPayExceptionContainer ex = (FlexPayExceptionContainer) e;
				ex.error(plog, "Job with id #{} completed with exception", getId());
			} else {
				plog.error("Job with id = " + getId() + " completed with exception", e);
			}
			parameters.put(STATUS_ERROR, Boolean.TRUE);
			jobMgr.jobFinished(id, RESULT_ERROR, parameters);
		} finally {
			SecurityContextHolder.clearContext();
			JobExecutionContextHolder.complete();
		}
		setEnd(new Date());
	}

	public Thread startThread(Map<Serializable, Serializable> parameters) {

		if (this.jobThread == null) {
			this.parameters.putAll(parameters);
			jobThread = new Thread(this, "JobThread-" + id);
			jobThread.start();
		} else {
			log.error("Job thread already started! Check Job Bean configuration. Scope must be prototype!");
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

	@NotNull
	@SuppressWarnings ({"unchecked"})
	protected static <T> T required(String name, Map<Serializable, Serializable> params)
			throws FlexPayException {

		return (T)required(name, params, null);
	}

	@NotNull
	@SuppressWarnings ({"unchecked"})
	protected static <T> T required(String name, Map<Serializable, Serializable> params, String i18nErrorKey)
			throws FlexPayException {

		try {
			T result = (T) params.get(name);
			if (result == null) {
				throw new FlexPayException("No param " + name, i18nErrorKey);
			}
			return result;
		} catch (ClassCastException ex) {
			throw new FlexPayException(ex);
		}
	}

	@Nullable
	@SuppressWarnings ({"unchecked"})
	protected static <T> T optional(String name, Map<Serializable, Serializable> params)
			throws FlexPayException {

		try {
			return (T) params.get(name);
		} catch (ClassCastException ex) {
			throw new FlexPayException(ex);
		}
	}
}
