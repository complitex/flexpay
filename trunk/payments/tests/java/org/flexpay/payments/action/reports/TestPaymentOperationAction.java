package org.flexpay.payments.action.reports;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.action.PaymentOperationAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;

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
