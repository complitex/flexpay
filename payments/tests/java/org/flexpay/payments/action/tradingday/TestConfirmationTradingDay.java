package org.flexpay.payments.action.tradingday;

import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.*;

public class TestConfirmationTradingDay extends SpringBeanAwareTestCase {

    @Autowired
    private ProcessManager processManager;

	@Autowired
	private ProcessDefinitionManager processDefinitionManager;
/*
	@Autowired
	private JbpmConfiguration jbpmConfiguration;
*/

    @Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {

		processDefinitionManager.deployProcessDefinition("CashboxTradingDay", true);

        Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT, 0);
        ProcessInstance processInstance = processManager.startProcess("CashboxTradingDay", parameters);
        assertNotNull("Process did not start: Object is null", processInstance);
		assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

        // TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, processInstance.getId(), "Пометить на закрытие", log);

        ProcessInstance process = processManager.getProcessInstance(processInstance.getId());
        String  currentStatus = (String) process.getParameters().get(PaymentCollectorTradingDayConstants.PROCESS_STATUS);

        assertEquals("Ожидает подтверждения", currentStatus);

        ConfirmationTradingDayServlet confirmationTradingDay = new ConfirmationTradingDayServlet();

//        confirmationTradingDay.service(new HttpServletRequest(), new HttpServletResponse());
    }

}