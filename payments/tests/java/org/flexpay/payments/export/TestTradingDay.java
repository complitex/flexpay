package org.flexpay.payments.export;

import org.junit.Test;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.process.*;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import static junit.framework.Assert.assertTrue;

public class TestTradingDay extends SpringBeanAwareTestCase {
    Logger log = LogManager.getLogger(getClass());

    @Autowired
    private ProcessManager processManager;
    @Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {
        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
        processManager.deployProcessDefinition("TradingDay", true);
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
