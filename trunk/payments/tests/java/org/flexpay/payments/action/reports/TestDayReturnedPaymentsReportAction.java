package org.flexpay.payments.action.reports;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.reports.DayReturnedPaymentsReportAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDayReturnedPaymentsReportAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private DayReturnedPaymentsReportAction action;

	@Test
	public void testExecute() throws Exception {

		// test not submit
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
	}
}