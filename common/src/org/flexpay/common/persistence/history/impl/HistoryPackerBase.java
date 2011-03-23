package org.flexpay.common.persistence.history.impl;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.history.*;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public abstract class HistoryPackerBase implements HistoryPacker {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private FPFileService fileService;

	protected HistoryConsumerService consumerService;
	protected HistoryRecordsFilter recordsFilter;
	private LockManager lockManager;

	public static final String LOCK_NAME = HistoryPackerBase.class.getName() + ".LOCK";

	private int groupSize = 5000;
	private int pagingSize = 50;

	/**
	 * Generate pack of history records for consumer
	 *
	 * @param stub consumer stub
	 * @return FPFile if there were some new records, or <code>null</code> otherwise
	 * @throws Exception if failure occurs
	 */
	@NotNull
	public List<FPFile> packHistory(@NotNull Stub<HistoryConsumer> stub) throws Exception {

		boolean locked = lockManager.lock(LOCK_NAME);
		if (!locked) {
			log.debug("Lock request failed, probably another instance already running");
			return Collections.emptyList();
		}

		try {
			return doPack(stub);
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}
	}

	private List<FPFile> doPack(Stub<HistoryConsumer> stub) throws FlexPayException, FlexPayExceptionContainer {
		HistoryConsumer consumer = consumerService.readFull(stub);
		if (consumer == null) {
			log.warn("History consumer not found {}", stub);
			return Collections.emptyList();
		}

		FetchRange range = new FetchRange(pagingSize);
		List<Diff> diffs = consumerService.findNewDiffs(stub, range);
		log.debug("Having diffs to pack: {}", diffs.size());

		HistoryPackingContext context = new HistoryPackingContext();
		context.setConsumer(consumer);
		context.setRange(range);

		List<FPFile> files = CollectionUtils.list();

		OutputStream os = null;
		try {
			if (range.hasMore() || !diffs.isEmpty()) {
				// open stream and write header if needed
				FPFile file = getNewFile(consumer, context);
				files.add(file);
				os = file.getOutputStream();
				os = new GZIPOutputStream(os);
			}
			beginPacking(os, context);

			int currentGroupSize = 0;

			while (range.hasMore() || !diffs.isEmpty()) {

				if (currentGroupSize >= groupSize) {
					// close last stream
					closeCurrentStream(os);

					// open stream and write header if needed
					FPFile file = getNewFile(consumer, context);
					files.add(file);
					os = file.getOutputStream();
					os = new GZIPOutputStream(os);
					beginPacking(os, context);
					currentGroupSize = 0;
				}

				// do packing
				context.resetLastNumberOfDiffs();
				packBatch(diffs, os, context);
				currentGroupSize += context.getLastNumberOfDiffs();

				// fetch next batch
				consumer.setLastPackedDiff(diffs.get(diffs.size() - 1));
				consumerService.update(consumer);
				range.nextPage();
				diffs = consumerService.findNewDiffs(stub, range);
				log.debug("Having diffs to pack: {}", diffs.size());
			}

			// end writing
			endPacking(os, context);
		} catch (Exception ex) {
			clear(files, context);
			log.error("Packing failed", ex);
			return Collections.emptyList();
		} finally {
			IOUtils.closeQuietly(os);
		}

		if (context.getNumberOfRecords() == 0) {
			log.debug("No history records to share, cleaning up");
			clear(files, context);
			return Collections.emptyList();
		}

		// sync file size property
		fileService.update(files);

		// update last dumped diff
		consumerService.update(consumer);

		return files;
	}

	private FPFile getNewFile(HistoryConsumer consumer, HistoryPackingContext context) throws IOException, FlexPayException {

		HistoryConsumptionGroup group = consumerService.newGroup(consumer);
		context.addGroup(group);
		FPFile file = new FPFile();
		file.setModule(fileService.getModuleByName("common"));
		file.setOriginalName("history-" + ApplicationConfig.getInstanceId() + "-" +
							 group.getId() + getFileExtension() + ".gz");
		FPFileUtil.createEmptyFile(file);
		fileService.create(file);
		group.setFile(file);
		return file;
	}

	private void clear(List<FPFile> files, HistoryPackingContext context) {

		for (HistoryConsumptionGroup group : context.getGroups()) {
			group.cancelBuilding();
			consumerService.deleteConsumptionGroup(group);
		}

		for (FPFile file : files) {
			fileService.delete(file);
		}
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

	protected void closeCurrentStream(OutputStream os) throws Exception {
		IOUtils.closeQuietly(os);
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

		for (HistoryConsumptionGroup group : context.getGroups()) {
			group.created();
			consumerService.update(group);
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

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	public void setPagingSize(int pagingSize) {
		this.pagingSize = pagingSize;
	}

	/**
	 * Set the size of a group history is packed in, used to limit result file size
	 *
	 * @param groupSize Max group size
	 */
	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
}
