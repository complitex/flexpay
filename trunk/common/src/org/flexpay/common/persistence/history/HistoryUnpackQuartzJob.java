package org.flexpay.common.persistence.history;

import org.flexpay.common.locking.LockManager;
import org.flexpay.common.service.Security;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class HistoryUnpackQuartzJob extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String LOCK_NAME = HistoryUnpackQuartzJob.class.getName() + ".LOCK";

	private LockManager lockManager;
	private HistoryUnPacker historyUnPacker;
	private HistoryUnpackManager unpackManager;

	/**
	 * Execute the actual job. The job data map will already have been applied as bean property values by execute. The
	 * contract is exactly the same as for the standard Quartz execute method.
	 *
	 * @see #execute
	 */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		boolean locked = lockManager.lock(LOCK_NAME);
		if (!locked) {
			log.debug("Lock request failed, probably another instance already running");
			return;
		}

		log.debug("Starting history unpacking");

		long count = 0;
		try {
			Security.authenticateSyncer();
			List<ExternalHistoryPack> packs = unpackManager.getNextPacks();
			for (ExternalHistoryPack pack : packs) {
				historyUnPacker.unpackHistory(pack.getFile());
				unpackManager.setLastUnpacked(pack);
				++count;
			}
		} catch (Exception ex) {
			throw new JobExecutionException("Failed history unpack", ex);
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}

		log.debug("Ended history unpack, unpacked {} packets", count);
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Required
	public void setUnpackManager(HistoryUnpackManager unpackManager) {
		this.unpackManager = unpackManager;
	}

	@Required
	public void setHistoryUnPacker(HistoryUnPacker historyUnPacker) {
		this.historyUnPacker = historyUnPacker;
	}
}
