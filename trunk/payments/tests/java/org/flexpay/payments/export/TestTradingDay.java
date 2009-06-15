package org.flexpay.payments.export;

import static junit.framework.Assert.assertTrue;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.jbpm.JbpmConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestTradingDay extends SpringBeanAwareTestCase {

    @Autowired
    private ProcessManager processManager;
	@Autowired
	private JbpmConfiguration jbpmConfiguration;

    @Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {

		processManager.deployProcessDefinition("TradingDay", true);

        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
        long processId = processManager.createProcess("TradingDay", parameters);
        assertTrue(processId > 0);

        Process process;
        log.debug("Process started");

        do {
            Thread.sleep(1000);
            process = processManager.getProcessInstanceInfo(processId);
            log.debug("Process work: " + process.getParameters().get("PROCESS_STATUS"));
        } while (!process.getProcessState().isCompleted());

    }

}
