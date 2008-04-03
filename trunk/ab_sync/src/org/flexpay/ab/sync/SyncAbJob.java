package org.flexpay.ab.sync;

import org.apache.log4j.Logger;
import org.flexpay.ab.service.SyncService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class SyncAbJob extends QuartzJobBean {

	private Logger log = Logger.getLogger(getClass());

	private SyncService syncService;

	public SyncAbJob() {
		log.debug("Creating new SyncAbJob");
	}

	/**
	 * Execute the actual job, i.e. run synchronisation process
	 */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		if (log.isDebugEnabled()) {
			log.debug("Starting sync at " + new Date());
		}

		try {
			// do the job
			syncService.syncAB();
		} catch (Exception e) {
			log.error("Sync failed", e);
		}
	}

	public void setSyncService(SyncService syncService) {
		this.syncService = syncService;
	}
}
