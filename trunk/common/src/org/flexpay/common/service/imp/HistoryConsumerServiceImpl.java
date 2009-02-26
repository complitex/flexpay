package org.flexpay.common.service.imp;

import org.flexpay.common.dao.HistoryConsumerDao;
import org.flexpay.common.dao.HistoryConsumerDaoExt;
import org.flexpay.common.dao.HistoryConsumptionDao;
import org.flexpay.common.dao.HistoryConsumptionGroupDao;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.*;
import org.flexpay.common.service.HistoryConsumerService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class HistoryConsumerServiceImpl implements HistoryConsumerService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private HistoryConsumerDao consumerDao;
	private HistoryConsumerDaoExt consumerDaoExt;
	private HistoryConsumptionDao consumptionDao;
	private HistoryConsumptionGroupDao consumptionGroupDao;

	/**
	 * Persist a new history consumer
	 *
	 * @param consumer history consumer
	 * @return Consumer object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	public HistoryConsumer create(@NotNull HistoryConsumer consumer) throws FlexPayExceptionContainer {

		log.debug("creating new history consumer {}", consumer);

		consumerDao.create(consumer);
		return consumer;
	}

	/**
	 * Update existing history consumer
	 *
	 * @param consumer history consumer
	 * @return Consumer object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	public HistoryConsumer update(@NotNull HistoryConsumer consumer) throws FlexPayExceptionContainer {

		log.debug("updating history consumer {}", consumer);

		consumerDao.update(consumer);
		return consumer;
	}

	/**
	 * Fetch diffs got from last consumer update
	 *
	 * @param consumer HistoryConsumer to get records for
	 * @param range	FetchRange
	 * @return list of diffs, possibly empty
	 */
	@NotNull
	public List<Diff> findNewDiffs(@NotNull HistoryConsumer consumer, FetchRange range) {
		return consumerDaoExt.findNewHistoryRecords(consumer.getId(), range);
	}

	/**
	 * Create new consumption group
	 *
	 * @param consumer HistoryConsumer to generate group for
	 * @return HistoryConsumptionGroup
	 */
	@Transactional (readOnly = false)
	@NotNull
	public HistoryConsumptionGroup newGroup(@NotNull HistoryConsumer consumer) {

		HistoryConsumptionGroup group = new HistoryConsumptionGroup();
		group.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		group.setConsumer(consumer);

		consumptionGroupDao.create(group);

		log.debug("creating new history consumption group {}", group);

		return group;
	}

	/**
	 * Create consumptions for a set of history records
	 *
	 * @param group   Consumption group to put new consumptions in
	 * @param records History records to create consumptions for
	 */
	@Transactional (readOnly = false)
	public void addConsumptions(HistoryConsumptionGroup group, List<HistoryRecord> records) {

		for (HistoryRecord record : records) {
			HistoryConsumption consumption = new HistoryConsumption();
			consumption.setHistoryRecord(record);
			consumption.setGroup(group);
			consumptionDao.create(consumption);
		}
	}

	/**
	 * Delete all history consumer consumptions
	 *
	 * @param stub Consumer stub
	 */
	@Transactional (readOnly = false)
	public void deleteConsumptions(@NotNull Stub<HistoryConsumer> stub) {

		HistoryConsumer consumer = readFull(stub);
		if (consumer == null) {
			log.debug("delete consumptions requested, but consumer not found {}", stub);
			return;
		}

		List<HistoryConsumptionGroup> groups = consumptionGroupDao.findConsumerGroups(stub.getId());
		for (HistoryConsumptionGroup group : groups) {
			deleteConsumptionGroup(group);
		}

		consumer.setLastPackedDiff(null);
		try {
			update(consumer);
		} catch (FlexPayExceptionContainer e) {
			log.error("Failed removing last packed diff", e.getFirstException());
		}
	}

	/**
	 * Delete group and all its consumptions
	 *
	 * @param group Consumption group to delete
	 */
	@Transactional (readOnly = false)
	public void deleteConsumptionGroup(HistoryConsumptionGroup group) {
		consumptionDao.deleteGroupConsumptions(group.getId());
		consumptionGroupDao.delete(group);
	}

	/**
	 * Read full history consumer info
	 *
	 * @param stub Consumer stub
	 * @return History consumer if found, or <code>null</code> otherwise
	 */
	public HistoryConsumer readFull(@NotNull Stub<HistoryConsumer> stub) {
		return consumerDao.read(stub.getId());
	}

	@Required
	public void setConsumerDao(HistoryConsumerDao consumerDao) {
		this.consumerDao = consumerDao;
	}

	@Required
	public void setConsumerDaoExt(HistoryConsumerDaoExt consumerDaoExt) {
		this.consumerDaoExt = consumerDaoExt;
	}

	@Required
	public void setConsumptionGroupDao(HistoryConsumptionGroupDao consumptionGroupDao) {
		this.consumptionGroupDao = consumptionGroupDao;
	}

	@Required
	public void setConsumptionDao(HistoryConsumptionDao consumptionDao) {
		this.consumptionDao = consumptionDao;
	}
}
