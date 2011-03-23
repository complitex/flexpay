package org.flexpay.common.persistence.history;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.service.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ObjectsSyncerJob {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String LOCK_NAME = ObjectsSyncQuartzJob.class.getName() + ".LOCK";

	private DiffService diffService;
	private LockManager lockManager;
	private ObjectsSyncer syncer;

	public void execute() {
		boolean locked = lockManager.lock(LOCK_NAME);
		if (!locked) {
			log.debug("Lock request failed, probably another instance already running");
			return;
		}

		log.debug("Starting objects sync");

		try {
			Security.authenticateSyncer();

			FetchRange range = new FetchRange();
			do {
				List<Diff> diffs = diffService.findNewDiffs(range);
				if (!syncer.processHistory(diffs)) {
					return;
				}
				range.nextPage();
			} while (range.hasMore());
		} catch (Exception e) {
			log.error("Sync failed", e);
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}

		log.debug("Ended objects sync");
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Required
	public void setSyncer(ObjectsSyncer syncer) {
		this.syncer = syncer;
	}
}
