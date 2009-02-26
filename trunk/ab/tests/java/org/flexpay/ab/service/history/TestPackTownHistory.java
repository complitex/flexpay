package org.flexpay.ab.service.history;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.history.HistoryPacker;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.ab.persistence.Town;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPackTownHistory extends SpringBeanAwareTestCase {

	@Autowired
	private HistoryPacker historyPacker;
	@Autowired
	private TownHistoryGenerator townHistoryGenerator;
	@Autowired
	private HistoryConsumerService historyConsumerService;

	@Test
	public void testPackNskHistory() throws Exception {

		// first, generate some history if needed
		townHistoryGenerator.generateFor(new Town(2L));

		Stub<HistoryConsumer> consumer = new Stub<HistoryConsumer>(1L);

		// second, clean up all previously generated packes
		historyConsumerService.deleteConsumptions(consumer);

		// finally, dump history to the file
		FPFile history = historyPacker.packHistory(consumer);
		assertNotNull("history packing failed", history);

		FPFile nextHistory = historyPacker.packHistory(consumer);
		assertNull("Subsecuent packing should not create a new file", nextHistory);
	}
}
