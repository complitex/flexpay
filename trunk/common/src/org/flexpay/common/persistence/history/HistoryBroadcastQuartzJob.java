package org.flexpay.common.persistence.history;

import org.springframework.scheduling.quartz.QuartzJobBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.locking.LockManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job performing history broadcasting for all registered {@link org.flexpay.common.service.transport.OutTransport}
 */
public class HistoryBroadcastQuartzJob extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String LOCK_NAME = HistoryBroadcastQuartzJob.class.getName() + ".LOCK";

	private DiffService diffService;
	private LockManager lockManager;

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

		log.debug("Starting objects sync");

		try {
			
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}

		log.debug("Ended objects sync");
	}
}
