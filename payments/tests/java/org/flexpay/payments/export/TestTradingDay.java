package org.flexpay.payments.export;

import static junit.framework.Assert.assertTrue;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestTradingDay extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ProcessManager testProcessManager;

	@Test
	public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {

		testProcessManager.deployProcessDefinition("TradingDay", true);

		Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		long processId = testProcessManager.createProcess("TradingDay", parameters);
		assertTrue(processId > 0);

		testProcessManager.join(processId);
		Process process = testProcessManager.getProcessInstanceInfo(processId);
		assertNotNull("Process not found", process);
		log.debug("Process work: {}", process.getParameters().get(TradingDay.PROCESS_STATUS));
	}
}
