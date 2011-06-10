package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.history.HistoryConsumer;
import org.flexpay.common.persistence.history.HistoryPacker;
import org.flexpay.common.persistence.history.impl.SoapOutHistoryTransport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.service.HistoryConsumerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestSoapHistoryTransport extends AbSpringBeanAwareTestCase {

	@Autowired
	private HistoryPacker historyPacker;
	@Autowired
	private TownHistoryGenerator townHistoryGenerator;
	@Autowired
	private PersonsHistoryGenerator personsHistoryGenerator;
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

		// generate some history
		townHistoryGenerator.generateFor(new Town(2L));
		personsHistoryGenerator.generate();

		Stub<HistoryConsumer> consumer = new Stub<HistoryConsumer>(1L);

		// dump history to the file
		List<FPFile> history = historyPacker.packHistory(consumer);
		assertFalse("history packing failed", history.isEmpty());

		for (FPFile file : history) {
			outTransport.send(file);
		}

		// wait while process manager ends
		List<ProcessInstance> processes = processManager.getProcesses();
		for (ProcessInstance process : processes) {
			processManager.join(process.getId());
		}
	}
}
