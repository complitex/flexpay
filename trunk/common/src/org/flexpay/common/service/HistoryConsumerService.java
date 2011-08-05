package org.flexpay.common.service;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryConsumptionGroup;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface HistoryConsumerService extends JpaSetService {

	/**
	 * Persist a new history consumer
	 *
	 * @param consumer history consumer
	 * @return Consumer object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	HistoryConsumer create(@NotNull HistoryConsumer consumer) throws FlexPayExceptionContainer;

	/**
	 * Update existing history consumer
	 *
	 * @param consumer history consumer
	 * @return Consumer object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	HistoryConsumer update(@NotNull HistoryConsumer consumer) throws FlexPayExceptionContainer;

	/**
	 * Fetch diffs got from last consumer update
	 *
	 * @param consumer HistoryConsumer to get records for
	 * @param range	Fetch range
	 * @return list of diffs, possibly empty
	 */
	@NotNull
	List<Diff> findNewDiffs(@NotNull Stub<HistoryConsumer> consumer, FetchRange range);

	/**
	 * Create new consumption group
	 *
	 * @param consumer HistoryConsumer to generate group for
	 * @return HistoryConsumptionGroup
	 */
	@NotNull
	HistoryConsumptionGroup newGroup(@NotNull HistoryConsumer consumer);

	/**
	 * Create consumptions for a set of history records
	 *
	 * @param group   Consumption group to put new consumptions in
	 * @param records History records to create consumptions for
	 */
	void addConsumptions(HistoryConsumptionGroup group, List<HistoryRecord> records);

	/**
	 * Delete all history consumer consumptions
	 *
	 * @param stub Consumer stub
	 */
	void deleteConsumptions(@NotNull Stub<HistoryConsumer> stub);

	/**
	 * Delete group and all its consumptions
	 *
	 * @param group Consumption group to delete
	 */
	void deleteConsumptionGroup(HistoryConsumptionGroup group);

	/**
	 * Read full history consumer info
	 *
	 * @param stub Consumer stub
	 * @return History consumer if found, or <code>null</code> otherwise
	 */
	@Nullable
	HistoryConsumer readFull(@NotNull Stub<HistoryConsumer> stub);

	/**
	 * List all history consumers
	 *
	 * @return List of consumers
	 */
	List<HistoryConsumer> listConsumers();

	/**
	 * Update created consumption group
	 *
	 * @param group HistoryConsumptionGroup to update
	 * @return updated group back
	 */
	@NotNull
	HistoryConsumptionGroup update(@NotNull HistoryConsumptionGroup group);

	/**
	 * List yet not sent consumer groups
	 *
	 * @param consumerStub HistoryConsumer to check groups of
	 * @param pager Page
	 * @return List of groups that was not sent yet
	 */
	List<HistoryConsumptionGroup> listNotSentGroups(
			Stub<HistoryConsumer> consumerStub, Page<HistoryConsumptionGroup> pager);
}
