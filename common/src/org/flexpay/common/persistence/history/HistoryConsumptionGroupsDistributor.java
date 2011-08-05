package org.flexpay.common.persistence.history;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.service.JpaSetService;
import org.flexpay.common.service.transport.OutTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class HistoryConsumptionGroupsDistributor implements JpaSetService {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private HistoryConsumerService consumerService;
	private LockManager lockManager;

	// number of groups sent via one cycle of work
	private int groupsBatchSize = 200;
	// number of seconds after postponed group could be send again
	private long postponeTimeout = 60 * 5;

	public static final String LOCK_NAME = HistoryConsumptionGroupsDistributor.class.getName() + ".LOCK";

	public void distribute() {

		boolean locked = lockManager.lock(LOCK_NAME);
		if (!locked) {
			log.debug("Lock request failed, probably another instance already running");
			return;
		}

		try {
			List<HistoryConsumer> consumers = consumerService.listConsumers();
			for (HistoryConsumer consumer : consumers) {
				distributeFor(consumer);
			}
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}
	}

	private void distributeFor(HistoryConsumer consumer) {

		List<HistoryConsumptionGroup> groups = consumerService.listNotSentGroups(
				stub(consumer), new Page<HistoryConsumptionGroup>(groupsBatchSize));
		for (HistoryConsumptionGroup group : groups) {
			OutTransportConfig transportConfig = consumer.getOutTransportConfig();
			OutTransport transport = transportConfig.createTransport();
			try {
				if (group.wasPostponed()) {
					if (group.getPostponeTime().getTime() + postponeTimeout * 1000 < System.currentTimeMillis()) {
						log.debug("Group postpone timeout not reached: {}", group);
						return;
					}
					group.depostpone();
				}
				group.nextTry();
				transport.send(group.getFile());
				group.sent();
			} catch (Exception ex) {
				log.warn("Failed sending history group file", ex);
				if (group.getSendTries() % 5 == 0) {
					group.postpone();
				}
				return;
			} finally {
				consumerService.update(group);
			}
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        consumerService.setJpaTemplate(jpaTemplate);
        lockManager.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setConsumerService(HistoryConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	public void setGroupsBatchSize(int groupsBatchSize) {
		this.groupsBatchSize = groupsBatchSize;
	}

	public void setPostponeTimeout(long postponeTimeout) {
		this.postponeTimeout = postponeTimeout;
	}

}
