package org.flexpay.common.persistence.history;

import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.beans.factory.annotation.Required;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HistoryDistributorQuartzJob extends QuartzJobBean {

	private HistoryConsumptionGroupsDistributor distributor;

	/**
	 * Execute the actual job. The job data map will already have been applied as bean property values by execute. The
	 * contract is exactly the same as for the standard Quartz execute method.
	 *
	 * @see #execute
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		distributor.distribute();
	}

	@Required
	public void setDistributor(HistoryConsumptionGroupsDistributor distributor) {
		this.distributor = distributor;
	}
}
