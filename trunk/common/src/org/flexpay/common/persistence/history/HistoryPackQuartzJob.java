package org.flexpay.common.persistence.history;

import org.flexpay.common.locking.LockManager;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.service.Security;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

/**
 * Job performing history packing to groups
 */
public class HistoryPackQuartzJob extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String LOCK_NAME = HistoryPackQuartzJob.class.getName() + ".LOCK";

	private boolean enabled;
	private LockManager lockManager;
	private HistoryPacker historyPacker;
	private HistoryConsumerService historyConsumerService;

	/**
	 * Execute the actual job. The job data map will already have been applied as bean property values by execute. The
	 * contract is exactly the same as for the standard Quartz execute method.
	 *
	 * @see #execute
	 */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		if (!enabled) {
			return;
		}

		boolean locked = lockManager.lock(LOCK_NAME);
		if (!locked) {
			log.debug("Lock request failed, probably another instance already running");
			return;
		}

		log.debug("Starting history pack");

		try {
			Security.authenticateHistoryPacker();

			List<HistoryConsumer> consumers = historyConsumerService.listConsumers();
			for (HistoryConsumer consumer : consumers) {
				log.debug("History consumer: {}", consumer);
				historyPacker.packHistory(stub(consumer));
			}
		} catch (Exception ex) {
			log.error("Failed history pack", ex);
			throw new JobExecutionException("Failed history pack", ex);
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}

		log.debug("Ended history pack");
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Required
	public void setHistoryPacker(HistoryPacker historyPacker) {
		this.historyPacker = historyPacker;
	}

	@Required
	public void setHistoryConsumerService(HistoryConsumerService historyConsumerService) {
		this.historyConsumerService = historyConsumerService;
	}

	@Required
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
