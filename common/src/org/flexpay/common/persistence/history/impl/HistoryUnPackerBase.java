package org.flexpay.common.persistence.history.impl;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryUnPacker;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.util.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

public abstract class HistoryUnPackerBase implements HistoryUnPacker {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;

	/**
	 * Generate pack of history records for consumer
	 *
	 * @param file FPFile containing history records
	 * @throws Exception if failure occurs while unpacking
	 */
    @Override
	public void unpackHistory(@NotNull FPFile file) throws Exception {

		InputStream is = null;
		HistoryPackingContext context = new HistoryPackingContext();
		try {
			is = file.getInputStream();
			is = new GZIPInputStream(is);
			beginUnpacking(is, context);

			List<Diff> diffs = unpackBatch(is, context);
			while (!diffs.isEmpty()) {
				for (Diff diff : diffs) {
					context.addDiff();
					diff.setUserName(SecurityUtil.getUserName());
					diff.setProcessingStatus(ProcessingStatus.STATUS_LOADED);
				}
				diffService.create(diffs);
				diffs = unpackBatch(is, context);
			}

			endUnpacking(is, context);
			diffService.moveLoadedDiffsToNewState();
		} catch (Exception ex) {
			log.error("Failed unpacking history file: " + file, ex);
			diffService.removeLoadedDiffs();
			throw ex;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Do actual records unpacking
	 *
	 * @param is	  Inputream to read from
	 * @param context Packing context
	 * @return List of unpacked diffs
	 * @throws Exception if unpacking fails
	 */
	@NotNull
	protected abstract List<Diff> unpackBatch(InputStream is, HistoryPackingContext context) throws Exception;

	/**
	 * Do optional unpacking preparations
	 *
	 * @param is	  InputStream to read from
	 * @param context Packing context
	 * @throws Exception if failure occurs
	 */
	protected void beginUnpacking(InputStream is, HistoryPackingContext context) throws Exception {
		log.debug("Begining unpack");
	}

	/**
	 * Do optional unpacking preparations
	 *
	 * @param is	  InputStream to read from
	 * @param context Packing context
	 * @throws Exception if failure occurs
	 */
	protected void endUnpacking(InputStream is, HistoryPackingContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Ended unpack, number of packed diffs {}", context.getNumberOfDiffs());
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}
}
