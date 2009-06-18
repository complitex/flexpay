package org.flexpay.common.persistence.history;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ObjectsSyncQuartzJob extends QuartzJobBean {

	private ObjectsSyncerJob syncerJob;

	/**
	 * Execute the actual job. The job data map will already have been applied as bean property values by execute. The
	 * contract is exactly the same as for the standard Quartz execute method.
	 *
	 * @see #execute
	 */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		syncerJob.execute();
	}

	@Required
	public void setSyncerJob(ObjectsSyncerJob syncerJob) {
		this.syncerJob = syncerJob;
	}
}
