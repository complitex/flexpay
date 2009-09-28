package org.flexpay.common.process.job;

/**
 * Callback interface for Job execution events
 */
public interface JobExecutionListener {

	void jobStarted(JobExecutionContext context);

	void jobProgressUpdated(JobExecutionContext context);

	void jobCompleted(JobExecutionContext context);
}
