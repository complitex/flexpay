package org.flexpay.common.process.job;

import org.flexpay.common.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class JobExecutionContext {

	private final Set<JobExecutionListener> executionListeners =
			Collections.synchronizedSet(CollectionUtils.<JobExecutionListener>set());

	private long taskId;
	private long totalSize = 0;
	private long complete = 0;

	private Map<String, Object> parameters = CollectionUtils.map();

	public JobExecutionContext(long taskId) {
		this.taskId = taskId;
	}

	public long getTaskId() {
		return taskId;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		if (totalSize <= 0) {
			totalSize = 0;
		}
		this.totalSize = totalSize;

		for (JobExecutionListener listener : executionListeners) {
			listener.jobStarted(this);
		}
	}

	public long getComplete() {
		return complete;
	}

	public void setComplete(long complete) {
		if (complete <= 0) {
			complete = 0;
		}
		this.complete = complete;

		for (JobExecutionListener listener : executionListeners) {
			if (complete < totalSize) {
				listener.jobProgressUpdated(this);
			} else {
				listener.jobCompleted(this);
			}
		}
	}

	void complete() {
		for (JobExecutionListener listener : executionListeners) {
			listener.jobCompleted(this);
		}
	}

	public void addListener(JobExecutionListener listener) {
		executionListeners.add(listener);
	}

	public void setParameter(String name, Object obj) {
		parameters.put(name, obj);
	}

	public Object getParameter(String name) {
		return parameters.get(name);
	}

	public void removeParameter(String name) {
		parameters.remove(name);
	}
}
