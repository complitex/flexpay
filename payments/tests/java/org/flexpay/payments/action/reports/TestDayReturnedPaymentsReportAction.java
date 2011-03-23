package org.flexpay.payments.action.reports;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class TestDayReturnedPaymentsReportAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private DayReturnedPaymentsReportAction action;

	@Test
	public void testExecute() throws Exception {

		// test not submit
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
	}
}