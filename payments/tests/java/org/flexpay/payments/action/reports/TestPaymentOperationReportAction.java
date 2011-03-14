package org.flexpay.payments.action.reports;

import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.orgs.persistence.TestData.CASHBOX_1;
import static org.flexpay.orgs.persistence.TestData.CASHBOX_2;
import static org.flexpay.payments.persistence.TestData.OPERATION;
import static org.flexpay.payments.persistence.TestData.OPERATION_2;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPaymentOperationReportAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private PaymentOperationReportAction action;

	@Test
	public void testBuildPrintForm() throws Exception {

		action.setOperationId(OPERATION.getId());
		action.setCashboxId(CASHBOX_1.getId());
		action.setCopy(true);

		assertEquals("Action execute failed", FPActionSupport.FILE, action.execute());
		assertEquals("Invalid per payment point report", "DoubleQuittancePayment_1.pdf", action.getReport().getOriginalName());
	}

	@Test
	public void testBuildPrintForm2() throws Exception {

		action.setOperationId(OPERATION_2.getId());
		action.setCashboxId(CASHBOX_2.getId());
		action.setCopy(true);

		assertEquals("Action execute failed", FPActionSupport.FILE, action.execute());
		assertEquals("Invalid per payment point report", "DoubleQuittancePayment.pdf", action.getReport().getOriginalName());
	}
}
