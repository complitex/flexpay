package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryHandler;
import org.flexpay.common.persistence.history.ObjectsSyncer;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class ObjectsSyncerImpl implements ObjectsSyncer {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private List<HistoryHandler> handlers = Collections.emptyList();

	/**
	 * Process history
	 *
	 * @param diffs History records to process
	 */
	public void processHistory(@NotNull List<Diff> diffs) {

		log.info("About to process {} diffs", diffs.size());

		for (Diff diff : diffs) {

			log.info("Processing history diff: {}", diff);

			try {
				boolean processed = true;
				for (HistoryHandler handler : handlers) {
					if (handler.supports(diff)) {
						handler.process(diff);
						diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
						break;
					}
				}

				if (!processed) {
					// no handler found, setting diff to ignored
					log.warn("No handler found to process diff {}", diff);
					diff.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
				}
				diffService.update(diff);
			} catch (Exception e) {
				log.error("Failed processing diff " + diff, e);
			}
		}

		log.info("Ended processing diffs");
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	public void setHandlers(List<HistoryHandler> handlers) {
		this.handlers = handlers;
	}
}
