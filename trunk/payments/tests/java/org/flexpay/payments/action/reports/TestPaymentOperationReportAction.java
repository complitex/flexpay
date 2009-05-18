package org.flexpay.payments.action.reports;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.reports.PaymentOperationReportAction;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.TestData;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPaymentOperationReportAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private PaymentOperationReportAction action;

	@Test
	public void testBuildPrintForm() throws Exception {

		action.setOperation(new Operation(TestData.OPERATION));

		assertEquals("Action execute failed", FPActionSupport.FILE, action.execute());

		System.out.println("File: " + action.getReport().getNameOnServer());
	}
}
