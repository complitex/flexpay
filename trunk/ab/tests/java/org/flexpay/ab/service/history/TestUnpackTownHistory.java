package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryPacker;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.impl.XmlHistoryUnPacker;
import org.flexpay.common.service.HistoryConsumerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class TestUnpackTownHistory extends AbSpringBeanAwareTestCase {

	@Autowired
	private XmlHistoryUnPacker historyUnPacker;
	@Autowired
	private HistoryPacker historyPacker;
	@Autowired
	private TownHistoryGenerator townHistoryGenerator;
	@Autowired
	private HistoryConsumerService historyConsumerService;

	@Test
	public void testUnpackHistory() throws Exception {

		// first, generate some history if needed
		townHistoryGenerator.generateFor(new Town(2L));

		Stub<HistoryConsumer> consumer = new Stub<HistoryConsumer>(1L);

		// second, clean up all previously generated packes
		historyConsumerService.deleteConsumptions(consumer);

		// third, dump history to the file
		List<FPFile> history = historyPacker.packHistory(consumer);
		assertFalse("history packing failed", history.isEmpty());

		// forth, clean up generated packes
		historyConsumerService.deleteConsumptions(consumer);

		// set custom reference restorer that changes instance id of diff
		historyUnPacker.setRefRestorer(new XmlHistoryUnPacker.RefRestorer() {
            @Override
			public void restoreReferences(Diff diff) {
				diff.setId(null);
				diff.setInstanceId(getClass().getName());
				for (HistoryRecord record : diff.getHistoryRecords()) {
					record.setId(null);
					record.setDiff(diff);
				}
			}
		});

		// finally, unpack history
		for (FPFile file : history) {
			historyUnPacker.unpackHistory(file);
		}
	}
}
