package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryPacker;
import org.flexpay.common.service.HistoryConsumerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPackTownHistory extends AbSpringBeanAwareTestCase {

	@Autowired
	private HistoryPacker historyPacker;
	@Autowired
	private TownHistoryGenerator townHistoryGenerator;
	@Autowired
	private HistoryConsumerService historyConsumerService;

	@Test
	public void testPackNskHistory() throws Exception {

		// first, generate some history if needed
		townHistoryGenerator.generateFor(new Town(1L));

		Stub<HistoryConsumer> consumer = new Stub<HistoryConsumer>(1L);

		// second, clean up all previously generated packs
		historyConsumerService.deleteConsumptions(consumer);

		// finally, dump history to the files
		historyPacker.setGroupSize(50);
		historyPacker.setPagingSize(50);
		List<FPFile> history = historyPacker.packHistory(consumer);
		assertFalse("history packing failed", history.isEmpty());

		List<FPFile> nextHistory = historyPacker.packHistory(consumer);
		assertTrue("Subsecuent packing should not create a new files", nextHistory.isEmpty());
	}
}
