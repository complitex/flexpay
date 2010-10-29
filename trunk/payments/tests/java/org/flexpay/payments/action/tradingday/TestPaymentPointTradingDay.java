package org.flexpay.payments.action.tradingday;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.flexpay.common.service.Roles.*;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;
import static org.flexpay.payments.service.Roles.*;
import static org.junit.Assert.assertFalse;

public class TestPaymentPointTradingDay extends SpringBeanAwareTestCase {

	@Autowired
    private ProcessManager processManager;

	@Autowired
	private PaymentPointService paymentPointService;

	@Autowired
	private CashboxService cashboxService;

	@Before
	public void authenticateTestUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				org.flexpay.common.service.Roles.BASIC,
				org.flexpay.orgs.service.Roles.BASIC,
				PROCESS_READ,
				PROCESS_DELETE,
				PROCESS_DEFINITION_UPLOAD_NEW,

				PROCESS_DEFINITION_UPLOAD_NEW,
				PROCESS_DEFINITION_UPLOAD_NEW,

				PAYMENT_COLLECTOR_READ,
				PAYMENT_COLLECTOR_CHANGE,
				PAYMENT_POINT_READ,
				PAYMENT_POINT_CHANGE,
				ORGANIZATION_READ,
				CASHBOX_READ,
				CASHBOX_CHANGE,

				DOCUMENT_READ,
				DOCUMENT_CHANGE,
				OPERATION_READ,
				OPERATION_CHANGE,
				SERVICE_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		UserPreferences preferences = new UserPreferences();
		preferences.setTargetDetails(user);
		Authentication auth = new AnonymousAuthenticationToken("key", preferences, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException {
		processManager.deployProcessDefinition("PaymentPointTradingDay", true);
		processManager.deployProcessDefinition("CashboxTradingDay", true);

		final Long currentPaymentPointId = 1L;

        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		parameters.put(PAYMENT_POINTS, new Long[]{currentPaymentPointId, 2L});
		parameters.put(CURRENT_INDEX_PAYMENT_POINT, 0);
		long processId = processManager.createProcess("PaymentPointTradingDay", parameters);

		assertTrue(processId > 0);

		Thread.sleep(10000);

		assertFalse("Process completed", isProcessCompleted(processId));

		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(currentPaymentPointId));

		assertNotNull("Payment point did not find", paymentPoint);

		assertEquals("Trading day for payment point did not start", processId, paymentPoint.getTradingDayProcessInstanceId().longValue());

		List<Cashbox> cashboxes = cashboxService.findCashboxesForPaymentPoint(currentPaymentPointId);

		Process process = processManager.getProcessInstanceInfo(processId);
//		Long variable = (Long)process.getParameters().get("variable");
//		log.debug("variable={}", variable);
//		ProcessInstance processInstance = processManager.getProcessInstance(process.getProcessInstaceId());
//		log.debug("sub-process={}", processInstance.getRootToken().getSubProcessInstance());

//		getState(cashboxes);
		sendSignal(cashboxes, AccounterAssignmentHandler.ACCOUNTER, "Пометить на закрытие");
//		Thread.sleep(5000);
//		getState(cashboxes);
		sendSignal(cashboxes, AccounterAssignmentHandler.ACCOUNTER, "Подтвердить закрытие");
//		Thread.sleep(5000);
//		getState(cashboxes);
		sendSignal(cashboxes, PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR, "Принять сообщение");

		do {
//			Thread.sleep(5000);
			log.debug("Wait process " + processId);
			getState(cashboxes);
		} while (!isProcessCompleted(processId));
	}

	private void getState(List<Cashbox> cashboxes) {
		for (Cashbox cashbox : cashboxes) {
			getCashboxState(cashbox, AccounterAssignmentHandler.ACCOUNTER);
			getCashboxState(cashbox, PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR);
		}
	}

	private void getCashboxState(Cashbox cashbox, String user) {
		Set<?> transitions = TaskHelper.getTransitions(processManager, user, cashbox.getTradingDayProcessInstanceId(), null, log);

		StringBuilder transitionNames = new StringBuilder();
		transitionNames.append("Cashbox ").append(cashbox.getId()).append(" user ").append(user).append(":");
		for (Object transition : transitions) {
			transitionNames.append(((Transition) transition).getName()).append(",");
		}
		log.debug(transitionNames.toString());
	}

	private void sendSignal(List<Cashbox> cashboxes, String user, String action) throws InterruptedException {
		for (Cashbox cashbox : cashboxes) {
			TaskHelper.getTransitions(processManager, user, cashbox.getTradingDayProcessInstanceId(), action, log);
			Thread.sleep(5000);
		}
	}

	private boolean isProcessCompleted(long processId) {
		Process process = processManager.getProcessInstanceInfo(processId);
		return process.getProcessState().isCompleted();
	}
}
