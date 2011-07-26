package org.flexpay.common.persistence.history.impl;

import com.thoughtworks.xstream.XStream;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;

public class XmlHistoryUnPacker extends HistoryUnPackerBase {

	private XStream xstream = new XStream();
	private ObjectInputStream ois = null;

	private RefRestorer refRestorer = new RefRestorer() {
        @Override
		public void restoreReferences(Diff diff) {
			diff.setId(null);
			for (HistoryRecord record : diff.getHistoryRecords()) {
				record.setId(null);
				record.setDiff(diff);
			}
		}
	};

	/**
	 * Do optional unpacking preparations
	 *
	 * @param is	  InputStream to read from
	 * @param context Packing context
	 * @throws Exception if failure occurs
	 */
	@Override
	protected void beginUnpacking(InputStream is, HistoryPackingContext context) throws Exception {

		super.beginUnpacking(is, context);

		xstream.alias("diff", Diff.class);
		xstream.alias("record", HistoryRecord.class);

		ois = xstream.createObjectInputStream(getReader(is));
	}

	/**
	 * Do optional unpacking preparations
	 *
	 * @param is	  InputStream to read from
	 * @param context Packing context
	 * @throws Exception if failure occurs
	 */
	@Override
	protected void endUnpacking(InputStream is, HistoryPackingContext context) throws Exception {
		super.endUnpacking(is, context);

		ois.close();
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
    @Override
	protected List<Diff> unpackBatch(InputStream is, HistoryPackingContext context) throws Exception {

		List<Diff> diffs = CollectionUtils.list();
		while (diffs.size() < 5) {
			try {
				Object obj = ois.readObject();
				if (!(obj instanceof Diff)) {
					log.debug("Deserialized unknown object: {}", obj);
					continue;
				}

				Diff diff = (Diff) obj;

				log.debug("Deserialized diff: {}, with records: {}", diff, diff.getHistoryRecords());

				refRestorer.restoreReferences(diff);
				diffs.add(diff);
			} catch (Exception ex) {
				if (ex instanceof EOFException) {
					log.debug("EOF found");
					break;
				}
				log.error("Failed deserializing object from xml", ex);
				throw ex;
			}
		}

		log.debug("unpacked {} objects", diffs.size());

		return diffs;
	}

	private Reader getReader(@NotNull InputStream is) throws IOException {
		return new InputStreamReader(is, "UTF-8");
	}

	public static interface RefRestorer {
		void restoreReferences(Diff diff);
	}

	public void setRefRestorer(RefRestorer refRestorer) {
		this.refRestorer = refRestorer;
	}
}
