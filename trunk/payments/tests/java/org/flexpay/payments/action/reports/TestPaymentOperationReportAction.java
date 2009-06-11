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

//		action.setOperation(new Operation(TestData.OPERATION));

		action.setCashboxId(1L);

		assertEquals("Action execute failed", FPActionSupport.FILE, action.execute());
		assertEquals("Invalid per payment point report", "QuittancePayment_1.pdf", action.getReport().getOriginalName());

		System.out.println("File: " + action.getReport().getNameOnServer());
	}

	@Test
	public void testBuildPrintForm2() throws Exception {

//		action.setOperation(new Operation(TestData.OPERATION_2));

		action.setCashboxId(1L);

		assertEquals("Action execute failed", FPActionSupport.FILE, action.execute());
		assertEquals("Invalid per payment point report", "QuittancePayment.pdf", action.getReport().getOriginalName());

		System.out.println("File: " + action.getReport().getNameOnServer());
	}
}
