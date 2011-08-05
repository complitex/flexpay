package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryHandler;
import org.flexpay.common.persistence.history.ObjectsSyncer;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collections;
import java.util.List;

public class ObjectsSyncerImpl implements ObjectsSyncer {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private List<HistoryHandler> handlers = Collections.emptyList();

	/**
	 * ProcessInstance history
	 *
	 * @param diffs History records to process
	 */
    @Override
	public boolean processHistory(@NotNull List<Diff> diffs) {

		log.debug("About to process {} diffs", diffs.size());

		for (Diff diff : diffs) {

			log.debug("Processing history diff: {}", diff);
			if (diff.getErrorMessage() != null) {
				log.info("Diff has error message set, give up: {}", diff);
				return false;
			}

			try {
				boolean processed = true;
				for (HistoryHandler handler : handlers) {
					if (handler.supports(diff)) {
						SyncContext.setProcessingDiff(diff);
						handler.process(diff);
						SyncContext.setProcessingDiff(null);
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
			} catch (FlexPayExceptionContainer e) {
				e.error(log, "Sync failed");
				setErrorMessage(diff, e.getFirstException());
				return false;
			} catch (Exception e) {
				log.error("Failed processing diff " + diff, e);
				setErrorMessage(diff, e);
				return false;
			} finally {
				SyncContext.setProcessingDiff(null);
			}
		}

		log.debug("Ended processing diffs");
		return true;
	}
	
	private void setErrorMessage(Diff diff, Exception ex) {
		diff.setErrorMessage(ex.getMessage());
		diffService.update(diff);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        diffService.setJpaTemplate(jpaTemplate);
        for (HistoryHandler handler : handlers) {
            handler.setJpaTemplate(jpaTemplate);
        }
    }

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	public void setHandlers(List<HistoryHandler> handlers) {
		this.handlers = handlers;
	}
}
