package org.flexpay.common.persistence.history.impl;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.util.CollectionUtils;

import java.io.*;
import java.util.List;

public class XmlHistoryPacker extends HistoryPackerBase {

	private XStream xstream = new XStream();
	private ObjectOutputStream oos = null;

	/**
	 * Do optional packing preparations
	 *
	 * @param os	  OutputStream to write to
	 * @param context Packing context
	 */
	@Override
	protected void beginPacking(OutputStream os, HistoryPackingContext context) throws Exception {
		super.beginPacking(os, context);

		xstream.alias("diff", Diff.class);
		xstream.alias("record", HistoryRecord.class);

		xstream.omitField(HistoryRecord.class, "diff");
		xstream.omitField(HistoryRecord.class, "processingStatus");

		xstream.omitField(Diff.class, "userName");
		xstream.omitField(Diff.class, "processingStatus");

		if (os != null) {
			oos = xstream.createObjectOutputStream(getWriter(os));
		}

//		Writer wr = getWriter(os);
//		wr.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<history>\n");
//		wr.flush();
	}

	@Override
	protected void closeCurrentStream(OutputStream os) throws Exception {
		IOUtils.closeQuietly(oos);
		super.closeCurrentStream(os);
	}

	/**
	 * Do optional packing preparations
	 *
	 * @param os	  OutputStream to write to
	 * @param context Packing context
	 */
	@Override
	protected void endPacking(OutputStream os, HistoryPackingContext context) throws Exception {
		super.endPacking(os, context);

		IOUtils.closeQuietly(oos);
		oos = null;
//		Writer wr = getWriter(os);
//		wr.write("</history>");
//		wr.flush();
	}

	/**
	 * Do actual records packing
	 *
	 * @param diffs   Set of diffs to pack
	 * @param os	  OutputStream to write to
	 * @param context Packing context
	 * @throws Exception if failure occurs
	 */
	protected void packBatch(List<Diff> diffs, OutputStream os, HistoryPackingContext context) throws Exception {

		for (Diff diff : diffs) {

			log.debug("Packing diff #{}", diff.getId());

			context.getConsumer().setLastPackedDiff(diff);
			List<HistoryRecord> records = CollectionUtils.list();
			for (HistoryRecord record : diff.getHistoryRecords()) {

				if (!recordsFilter.accept(record)) {
					log.debug("record filtered: {}", record);
					continue;
				}

				records.add(record);
				context.addRecord();
			}

			if (records.isEmpty() && diff.getOperationType() != HistoryOperationType.TYPE_DELETE) {
				log.debug("no records in diff to pack: {}", diff);
				continue;
			}

			Diff clone = diff.copy();
			clone.setHistoryRecords(records);
			oos.writeObject(clone);
			context.addDiff();

			consumerService.addConsumptions(context.getLastGroup(), records);
		}
	}

	private Writer getWriter(OutputStream os) throws IOException {
		return new OutputStreamWriter(os, "UTF-8");
	}

	/**
	 * Get output file extension
	 *
	 * @return File extension
	 */
	protected String getFileExtension() {
		return ".xml";
	}

}
