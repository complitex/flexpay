package org.flexpay.ab.sync;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.flexpay.common.locking.LockManager;
import org.flexpay.ab.service.SyncService;

public class SyncAbJob extends QuartzJobBean {

	private LockManager lockManager;
	private SyncService syncService;

	/**
	 * Execute the actual job, i.e. run synchronisation process
	 */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if (!lockManager.lock("sync_ab_lock")) {
			// another process already requested a lock and is working
			return;
		}

		try {
			// do the job
			syncService.syncAB();
		} finally {
			// and release a lock
			lockManager.releaseLock("sync_ab_lock");
		}
	}

	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	public void setSyncService(SyncService syncService) {
		this.syncService = syncService;
	}
}
