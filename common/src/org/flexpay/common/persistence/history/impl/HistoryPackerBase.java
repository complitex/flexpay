package org.flexpay.common.persistence.history.impl;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryPacker;
import org.flexpay.common.persistence.history.HistoryRecordsFilter;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public abstract class HistoryPackerBase implements HistoryPacker {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private FPFileService fileService;

	protected HistoryConsumerService consumerService;
	protected HistoryRecordsFilter recordsFilter;

	/**
	 * Generate pack of history records for consumer
	 *
	 * @param stub consumer stub
	 * @return FPFile if there were some new records, or <code>null</code> otherwise
	 * @throws Exception if failure occurs
	 */
	public FPFile packHistory(@NotNull Stub<HistoryConsumer> stub) throws Exception {

		HistoryConsumer consumer = consumerService.readFull(stub);
		if (consumer == null) {
			log.warn("History consumer not found {}", stub);
			return null;
		}

		FetchRange range = new FetchRange();
		range.setPageSize(5);
		List<Diff> diffs = consumerService.findNewDiffs(consumer, range);

		HistoryPackingContext context = new HistoryPackingContext();
		context.setConsumer(consumer);
		context.setRange(range);
		context.setGroup(consumerService.newGroup(consumer));

		FPFile file = new FPFile();
		file.setModule(fileService.getModuleByName("common"));

		file.setOriginalName("history-" + ApplicationConfig.getInstanceId() + "-" +
							 context.getGroup().getId() + getFileExtension() + ".gz");

		FPFileUtil.createEmptyFile(file);
		fileService.create(file);

		OutputStream os = null;
		try {
			// open stream and write header if needed
			os = file.getOutputStream();
			os = new GZIPOutputStream(os);
			beginPacking(os, context);

			while (range.hasMore()) {

				// do packing
				packBatch(diffs, os, context);

				// fetch next batch
				consumer.setLastPackedDiff(diffs.get(diffs.size() - 1));
				consumerService.update(consumer);
				range.nextPage();
				diffs = consumerService.findNewDiffs(consumer, range);
			}

			// end writing
			endPacking(os, context);
		} catch (Exception ex) {
			clear(file, context);
			log.error("Packing failed", ex);
			return null;
		} finally {
			IOUtils.closeQuietly(os);
		}

		if (context.getNumberOfRecords() == 0) {
			log.info("No history records to share, cleaning up");
			clear(file, context);
			return null;
		}

		// update last dumped diff
		consumerService.update(consumer);

		return file;
	}

	private void clear(FPFile file, HistoryPackingContext context) {

		fileService.delete(file);
		consumerService.deleteConsumptionGroup(context.getGroup());
	}

	/**
	 * Do optional packing preparations
	 *
	 * @param os	  OutputStream to write to
	 * @param context Packing context
	 * @throws Exception if failure occurs
	 */
	protected void beginPacking(OutputStream os, HistoryPackingContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Begining pack, number of new diffs: {}", context.getRange().getCount());
		}
	}

	/**
	 * Do optional packing preparations
	 *
	 * @param os	  OutputStream to write to
	 * @param context Packing context
	 * @throws Exception if failure occurs
	 */
	protected void endPacking(OutputStream os, HistoryPackingContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Ended pack, number of packed diffs {} of {}",
					context.getNumberOfDiffs(), context.getRange().getCount());
		}
	}

	/**
	 * Do actual records packing
	 *
	 * @param diffs   Set of diffs to pack
	 * @param os	  OutputStream to write to
	 * @param context Packing context
	 * @throws Exception if packing fails
	 */
	protected abstract void packBatch(List<Diff> diffs, OutputStream os, HistoryPackingContext context)
			throws Exception;

	/**
	 * Get output file extension
	 *
	 * @return File extension
	 */
	protected abstract String getFileExtension();

	@Required
	public void setConsumerService(HistoryConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}

	@Required
	public void setRecordsFilter(HistoryRecordsFilter recordsFilter) {
		this.recordsFilter = recordsFilter;
	}
}
