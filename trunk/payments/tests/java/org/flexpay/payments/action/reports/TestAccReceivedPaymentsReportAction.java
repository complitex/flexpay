package org.flexpay.payments.action.reports;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAccReceivedPaymentsReportAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private AccReceivedPaymentsReportAction action;

	@Test
	public void testExecute() throws Exception {

		// test not submit
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		// test bad payment collector
		action.setSubmitted("submitted");
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());
	}
}
