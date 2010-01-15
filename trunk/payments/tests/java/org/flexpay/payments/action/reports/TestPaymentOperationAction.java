package org.flexpay.payments.action.reports;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.PaymentOperationAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestPaymentOperationAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	@Qualifier("paymentOperationReportAction")
	private PaymentOperationAction action;

	@Test
	public void testExecute() throws Exception {

		// test no operation id submitted
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
	}
}
