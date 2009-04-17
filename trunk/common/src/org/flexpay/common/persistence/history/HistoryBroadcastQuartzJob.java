package org.flexpay.common.persistence.history;

import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.FPFile;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.service.transport.OutTransport;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * Job performing history broadcasting for all registered {@link org.flexpay.common.service.transport.OutTransport}
 */
public class HistoryBroadcastQuartzJob extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String LOCK_NAME = HistoryBroadcastQuartzJob.class.getName() + ".LOCK";

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

		boolean locked = lockManager.lock(LOCK_NAME);
		if (!locked) {
			log.debug("Lock request failed, probably another instance already running");
			return;
		}

		log.debug("Starting history broadcast");

		try {
			List<HistoryConsumer> consumers = historyConsumerService.listConsumers();
			for (HistoryConsumer consumer : consumers) {
				List<FPFile> history = historyPacker.packHistory(stub(consumer));
				OutTransport transport = consumer.getOutTransportConfig().createTransport();
				for (FPFile file : history) {
					transport.send(file);
				}
			}
		} catch (Exception ex) {
			throw new JobExecutionException("Failed history broadcast", ex);
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}

		log.debug("Ended history broadcast");
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
}
