package org.flexpay.payments.action.tradingday;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.payments.actions.tradingday.ConfirmationTradingDayServlet;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestConfirmationTradingDay extends SpringBeanAwareTestCase {

    @Autowired
    private ProcessManager processManager;
/*
	@Autowired
	private JbpmConfiguration jbpmConfiguration;
*/

    @Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {

		processManager.deployProcessDefinition("CashboxTradingDay", true);

        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		parameters.put(ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT, 0);
        long processId = processManager.createProcess("CashboxTradingDay", parameters);
        assertTrue(processId > 0);

        TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, processId, "Пометить на закрытие", log);

        org.flexpay.common.process.Process process = processManager.getProcessInstanceInfo(processId);
        String  currentStatus = (String) process.getParameters().get(PaymentCollectorTradingDayConstants.PROCESS_STATUS);

        assertEquals("Ожидает подтверждения", currentStatus);

        ConfirmationTradingDayServlet confirmationTradingDay = new ConfirmationTradingDayServlet();

//        confirmationTradingDay.service(new HttpServletRequest(), new HttpServletResponse());
    }

}