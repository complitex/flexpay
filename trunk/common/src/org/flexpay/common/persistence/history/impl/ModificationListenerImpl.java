package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.*;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class ModificationListenerImpl<T extends DomainObject> implements ModificationListener<T> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private HistoryBuilder<T> historyBuilder;
	private DiffService diffService;
	private ReferencesHistoryGenerator<T> referencesHistoryGenerator;
	private DiffProcessor<T> diffProcessor = new DiffProcessorNope<T>();

	private void createDiff(Diff diff) {
		// check if the result of this operation is a sync
		if (SyncContext.isSyncing()) {
			Diff processingDiff = SyncContext.getProcessingDiff();
			diff.setMasterIndex(processingDiff.getMasterIndex());
			log.debug("Replacing master index");
		}

		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		if (diff.isNotEmpty()) {
			diffService.create(diff);
			log.debug("Creating diff {}", diff);
		} else {
			log.debug("Diff is empty");
		}
	}

	/**
	 * Notify of new object created
	 *
	 * @param obj New object
	 */
    @Override
	public void onCreate(@NotNull T obj) {

		log.debug("On CREATE: {}", obj);

		referencesHistoryGenerator.generateReferencesHistory(obj);

		Diff diff = historyBuilder.diff(null, obj);
		diffProcessor.onCreate(obj, diff);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		createDiff(diff);
	}

	/**
	 * Notify of object update
	 *
	 * @param objOld object old version
	 * @param obj	object new version
	 */
    @Override
	public void onUpdate(@NotNull T objOld, @NotNull T obj) {

		log.debug("On UPDATE:\n{}\n{}", objOld, obj);

		// check if old object already has history build, i.e. we are not updating new object
		if (!diffService.hasDiffs(objOld)) {
			// no diffs found for this object, calling onCreate first on old object version
			onCreate(objOld);
			log.debug("On UPDATE, just created old obj history");
		}

		referencesHistoryGenerator.generateReferencesHistory(obj);

		Diff diff = historyBuilder.diff(objOld, obj);
		diffProcessor.onUpdate(objOld, obj, diff);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		createDiff(diff);
	}

	/**
	 * Notify of object delete
	 *
	 * @param obj object that was deleted
	 */
    @Override
	public void onDelete(@NotNull T obj) {

		Diff diff = historyBuilder.deleteDiff(obj);
		diffProcessor.onDelete(obj, diff);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        historyBuilder.setJpaTemplate(jpaTemplate);
        diffService.setJpaTemplate(jpaTemplate);
        referencesHistoryGenerator.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setHistoryBuilder(HistoryBuilder<T> historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setReferencesHistoryGenerator(ReferencesHistoryGenerator<T> referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}

	public void setDiffProcessor(DiffProcessor<T> diffProcessor) {
		this.diffProcessor = diffProcessor;
	}

}
