package org.flexpay.payments.export;

import static junit.framework.Assert.assertTrue;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.jbpm.JbpmConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:WEB-INF/applicationContext.xml"})
public class TestTradingDay {
    private Logger log = Logger.getLogger(TestTradingDay.class);

    @Resource(type = org.flexpay.common.process.ProcessManagerImpl.class, name = "processManager")
    private ProcessManager testProcessManager;
	
    @Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {

		testProcessManager.deployProcessDefinition("TradingDay", true);

        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
        long processId = testProcessManager.createProcess("TradingDay", parameters);
        assertTrue(processId > 0);

        Process process;
        log.debug("Process started");

        do {
            Thread.sleep(1000);
            process = testProcessManager.getProcessInstanceInfo(processId);
            log.debug("Process work: " + process.getParameters().get("PROCESS_STATUS"));
        } while (!process.getProcessState().isCompleted());

    }

}
