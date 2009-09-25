package org.flexpay.common.process.job;

public class JobExecutionContext {

	private long totalSize = 0;
	private long complete = 0;

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getComplete() {
		return complete;
	}

	public void setComplete(long complete) {
		this.complete = complete;
	}
}
