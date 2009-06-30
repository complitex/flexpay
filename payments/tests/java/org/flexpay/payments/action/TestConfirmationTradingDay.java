package org.flexpay.payments.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.flexpay.common.process.*;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.actions.ConfirmationTradingDayServlet;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;

public class TestConfirmationTradingDay extends SpringBeanAwareTestCase {

    @Autowired
    private ProcessManager processManager;
/*
	@Autowired
	private JbpmConfiguration jbpmConfiguration;
*/

    @Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {

		processManager.deployProcessDefinition("TradingDay", true);

        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
        long processId = processManager.createProcess("TradingDay", parameters);
        assertTrue(processId > 0);

        Set transitions = TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, processId, "Пометить на закрытие", log);

        org.flexpay.common.process.Process process = processManager.getProcessInstanceInfo(processId);
        String  currentStatus = (String) process.getParameters().get("PROCESS_STATUS");

        assertEquals("Ожидает подтверждения", currentStatus);

        ConfirmationTradingDayServlet confirmationTradingDay = new ConfirmationTradingDayServlet();

//        confirmationTradingDay.service(new HttpServletRequest(), new HttpServletResponse());
    }

}