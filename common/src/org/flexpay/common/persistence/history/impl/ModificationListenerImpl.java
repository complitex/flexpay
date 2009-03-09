package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ModificationListenerImpl<T extends DomainObject> implements ModificationListener<T> {

	private HistoryBuilder<T> historyBuilder;
	private DiffService diffService;

	/**
	 * Notify of new object created
	 *
	 * @param obj New object
	 */
	public void onCreate(@NotNull T obj) {

		Diff diff = historyBuilder.diff(null, obj);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		if (diff.isNotEmpty()) {
			diffService.create(diff);
		}
	}

	/**
	 * Notify of object update
	 *
	 * @param objOld object old version
	 * @param obj	object new version
	 */
	public void onUpdate(@NotNull T objOld, @NotNull T obj) {

		Diff diff = historyBuilder.diff(objOld, obj);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		if (diff.isNotEmpty()) {
			diffService.create(diff);
		}
	}

	/**
	 * Notify of object delete
	 *
	 * @param obj object that was deleted
	 */
	public void onDelete(@NotNull T obj) {

		Diff diff = historyBuilder.deleteDiff(obj);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Required
	public void setHistoryBuilder(HistoryBuilder<T> historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}
}
