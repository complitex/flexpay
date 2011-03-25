package org.flexpay.payments.action.tradingday;

import org.flexpay.common.exception.FlexPayExceptionContainer;
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
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler;
import org.jbpm.graph.def.Transition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.*;
import static org.flexpay.common.service.Roles.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_COLLECTOR_ID;
import static org.flexpay.payments.service.Roles.*;
import static org.junit.Assert.assertFalse;

public class TestPaymentCollectorTradingDay extends SpringBeanAwareTestCase {

	@Autowired
    private ProcessManager processManager;

	@Autowired
	private PaymentCollectorService paymentCollectorService;

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
    public void testStartTradingDay() throws ProcessInstanceException, ProcessDefinitionException, InterruptedException, FlexPayExceptionContainer {
		processManager.deployProcessDefinition("PaymentCollectorTradingDay", true);
		processManager.deployProcessDefinition("PaymentPointTradingDay", true);
		processManager.deployProcessDefinition("CashboxTradingDay", true);

		final Long currentPaymentCollectorId = 1L;

        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		parameters.put(PAYMENT_COLLECTOR_ID, currentPaymentCollectorId);
		long processId = processManager.createProcess("PaymentCollectorTradingDay", parameters);

		assertTrue("Error", processId > 0);

		Thread.sleep(30000);

		assertFalse("Process completed", isProcessCompleted(processId));

		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(currentPaymentCollectorId));

		assertNotNull("Payment collector did not find", paymentCollector);

		assertEquals("Trading day for payment collector did not start", processId, paymentCollector.getTradingDayProcessInstanceId().longValue());

//		paymentCollector.setTradingDayProcessInstanceId(processId);
//		paymentCollectorService.update(paymentCollector);

		List<Cashbox> cashboxes = list();
		for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {
			assertNotNull("Process instance id did not find for payment point " + paymentPoint.getId(), paymentPoint.getTradingDayProcessInstanceId());
			cashboxes.addAll(cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId()));
		}
		for (Cashbox cashbox : cashboxes) {
			assertNotNull("Process instance id did not find for cashbox " + cashbox.getId(), cashbox.getTradingDayProcessInstanceId());
		}

		sendSignal(cashboxes, AccounterAssignmentHandler.ACCOUNTER, "Пометить на закрытие");
		sendSignal(cashboxes, AccounterAssignmentHandler.ACCOUNTER, "Подтвердить закрытие");
		sendSignal(cashboxes, PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR, "Принять сообщение");

		do {
			Thread.sleep(30000);
			log.debug("Wait process " + processId);
//			getState(cashboxes);
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
//			Thread.sleep(5000);
		}
	}

	private boolean isProcessCompleted(long processId) {
		Process process = processManager.getProcessInstanceInfo(processId);
		Object status = process.getParameters().get("PROCESS_STATUS");
		log.debug("Status: {} ({})", status, status.getClass());
		return process.getProcessState().isCompleted();
	}
}
