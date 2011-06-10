package org.flexpay.payments.action.tradingday;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.flexpay.common.service.Roles.*;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;
import static org.flexpay.payments.service.Roles.*;
import static org.junit.Assert.assertFalse;

public class TestPaymentPointTradingDay extends SpringBeanAwareTestCase {

	@Autowired
    private ProcessManager processManager;

	@Autowired
	private ProcessDefinitionManager processDefinitionManager;

	@Autowired
	private PaymentPointService paymentPointService;

	@Autowired
	private CashboxService cashboxService;

	@Before
    @Override
	public void authenticateTestUser() {
		List<GrantedAuthority> authorities = SecurityUtil.auths(
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
		processDefinitionManager.deployProcessDefinition("PaymentPointTradingDay", true);
		processDefinitionManager.deployProcessDefinition("CashboxTradingDay", true);

		final Long currentPaymentPointId = 1L;

        Map<String, Object> parameters = map();
		parameters.put(PAYMENT_POINTS, new Long[]{currentPaymentPointId, 2L});
		parameters.put(CURRENT_INDEX_PAYMENT_POINT, 0);
		ProcessInstance processInstance = processManager.startProcess("PaymentPointTradingDay", parameters);

		assertNotNull("Process did not start: Object is null", processInstance);
		assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

		Thread.sleep(10000);

		assertFalse("ProcessInstance completed", isProcessCompleted(processInstance.getId()));

		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(currentPaymentPointId));

		assertNotNull("Payment point did not find", paymentPoint);

		assertEquals("Trading day for payment point did not start", processInstance.getId(), paymentPoint.getTradingDayProcessInstanceId().longValue());

		List<Cashbox> cashboxes = cashboxService.findCashboxesForPaymentPoint(currentPaymentPointId);

		ProcessInstance process = processManager.getProcessInstance(processInstance.getId());
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
			log.debug("Wait process " + processInstance.getId());
			getState(cashboxes);
		} while (!isProcessCompleted(processInstance.getId()));
	}

	private void getState(List<Cashbox> cashboxes) {
		for (Cashbox cashbox : cashboxes) {
			getCashboxState(cashbox, AccounterAssignmentHandler.ACCOUNTER);
			getCashboxState(cashbox, PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR);
		}
	}

	private void getCashboxState(Cashbox cashbox, String user) {
		/*
		Set<?> transitions = TaskHelper.getTransitions(processManager, user, cashbox.getTradingDayProcessInstanceId(), null, log);

		StringBuilder transitionNames = new StringBuilder();
		transitionNames.append("Cashbox ").append(cashbox.getId()).append(" user ").append(user).append(":");
		for (Object transition : transitions) {
			transitionNames.append(((Transition) transition).getName()).append(",");
		}
		log.debug(transitionNames.toString());
		*/
	}

	private void sendSignal(List<Cashbox> cashboxes, String user, String action) throws InterruptedException {
		/*
		for (Cashbox cashbox : cashboxes) {
			TaskHelper.getTransitions(processManager, user, cashbox.getTradingDayProcessInstanceId(), action, log);
			Thread.sleep(5000);
		}
		*/
	}

	private boolean isProcessCompleted(long processId) {
		ProcessInstance process = processManager.getProcessInstance(processId);
		return process.hasEnded();
	}
}
