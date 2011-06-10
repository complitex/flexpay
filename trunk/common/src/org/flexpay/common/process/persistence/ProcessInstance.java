package org.flexpay.common.process.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

public class ProcessInstance {

	/**
	 * Name of a parameter holding process instance id
	 */
	public static final String PROCESS_INSTANCE_ID = "ProcessInstanceID";

	/**
	 * the active state of an instance
	 */
	public static enum STATE {
		PENDING, RUNNING, SUSPENDED, ABORTED, ENDED
	}

	/**
	 * the end state of an instance
	 */
	public static enum RESULT {
			COMPLETED, FAILED, ERROR, EXITED, OBSOLETE
	}

	private long id;
	private String logFileName;
	private Date startDate;
	private Date endDate;
	private boolean suspended;
	private String processDefinitionId;
	private long processDefenitionVersion;

	private RESULT endResult;
	private transient Lifecycle lifecycle;

	private Map<String, Object> parameters = map();

	public ProcessInstance() {
		initLifecycle();
	}

	public ProcessInstance(long id, String processDefinitionId, Date startDate, Date endDate, boolean suspended) {

		if (null == startDate)
			throw new IllegalArgumentException("An instance requires a start date");

		if (endDate != null && suspended)
			throw new IllegalArgumentException("An instance cannot be ended and suspended at the same time");

		this.id = id;
		this.processDefinitionId = processDefinitionId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.suspended = suspended;
		initLifecycle();
	}

	/**
	 * If not ENDED or SUSPENDED the instance is RUNNING
	 */
	private void initLifecycle() {
		if (hasEnded())
			this.lifecycle = new Lifecycle(this, STATE.ENDED);
		else if (isSuspended())
			this.lifecycle = new Lifecycle(this, STATE.SUSPENDED);
		else
			this.lifecycle = new Lifecycle(this, STATE.RUNNING);
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public STATE getState() {
		return this.lifecycle.getState();
	}

	public void setState(String nextState) {
		setState(STATE.valueOf(nextState));
	}

	public void setState(STATE nextState) {
		this.lifecycle = this.lifecycle.transitionTo(nextState);
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
		initLifecycle();
	}

	public RESULT getEndResult() {
		return endResult;
	}

	public void setEndResult(RESULT endResult) {
		if (getState() != STATE.ENDED)
			throw new IllegalArgumentException("Cannot set end result in state " + getState());

		this.endResult = endResult;
	}

	public boolean isRunning() {
		return this.startDate != null && !isSuspended();
	}

	public boolean hasEnded() {
		return this.startDate != null
				&& this.endDate != null;
	}

	public boolean isSuspended() {
		return null == this.endDate && suspended;
	}

	public long getProcessDefenitionVersion() {
		return processDefenitionVersion;
	}

	public void setProcessDefenitionVersion(long processDefenitionVersion) {
		this.processDefenitionVersion = processDefenitionVersion;
	}

	private class Lifecycle {
		private STATE current;
		private ProcessInstance instance;

		public Lifecycle(ProcessInstance instance, STATE current) {
			this.instance = instance;
			this.current = current;
		}

		public Lifecycle transitionTo(STATE next) {
			Lifecycle nextLifecycle = null;

			switch (next) {
				case SUSPENDED: // only RUNNING instances can be SUSPENDED
					if (STATE.RUNNING.equals(current)) {
						nextLifecycle = new Lifecycle(instance, next);
						instance.suspended = true;
						break;
					} else {
						throw new IllegalTransitionException(current, next);
					}
				case ENDED: // both RUNNING and SUSPENDED instances can be ENDED
					if (STATE.RUNNING.equals(current) || STATE.SUSPENDED.equals(current)) {
						nextLifecycle = new Lifecycle(instance, next);
						instance.suspended = false;
						instance.endDate = new Date();
						break;
					} else {
						throw new IllegalTransitionException(current, next);
					}
				case RUNNING: // only SUSPENDED instances can become RUNNING
					if (STATE.SUSPENDED.equals(current)) {
						nextLifecycle = new Lifecycle(instance, next);
						instance.suspended = false;
						break;
					} else {
						throw new IllegalTransitionException(current, next);
					}
				default:
					throw new IllegalTransitionException(current, next);
			}

			return nextLifecycle;
		}

		public STATE getState() {
			return current;
		}


		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Lifecycle lifecycle = (Lifecycle) o;

			if (current != lifecycle.current) return false;

			return true;
		}

		public int hashCode() {
			int result;
			result = (current != null ? current.hashCode() : 0);
			return result;
		}
	}

	private class IllegalTransitionException extends IllegalArgumentException {

		public IllegalTransitionException(STATE current, STATE next) {
			super("Illegal transition current " + current + " next " + next);
		}
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", id).
				append("logFileName", logFileName).
				append("processStartDate", startDate).
				append("processEndDate", endDate).
				append("processState", getState()).
				append("processDefinitionName", processDefinitionId).
				append("processDefenitionVersion", processDefenitionVersion).
				append("parameters", parameters).
				toString();
	}
}
