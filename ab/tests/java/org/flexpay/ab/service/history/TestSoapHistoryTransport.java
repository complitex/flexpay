package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryPacker;
import org.flexpay.common.persistence.history.impl.SoapOutHistoryTransport;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TestSoapHistoryTransport extends SpringBeanAwareTestCase {

	@Autowired
	private HistoryPacker historyPacker;
	@Autowired
	private TownHistoryGenerator townHistoryGenerator;
	@Autowired
	private HistoryConsumerService historyConsumerService;
	@Autowired
	private SoapOutHistoryTransport outTransport;
	@Autowired
	private ProcessManager processManager;

	@Test
	public void testHttpServerIsUp() throws Exception {
		URL url = new URL("http://localhost:58080/ShareHistory.wsdl");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		assertTrue("", 200 <= connection.getResponseCode() && connection.getResponseCode() < 400);
		connection.disconnect();
	}

	@Test
	public void testSendHistoryViaSoap() throws Exception {

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

		for (FPFile file : history) {
			outTransport.send(file);
		}

		// wait while process manager ends
		List<Process> processes = processManager.getProcesses();
		for (Process process : processes) {
			processManager.join(process.getId());
		}
	}
}
