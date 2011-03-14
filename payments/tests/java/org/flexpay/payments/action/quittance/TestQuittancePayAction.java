package org.flexpay.payments.action.quittance;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestQuittancePayAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private QuittancePayAction action;

	@Test
	public void testExecute() throws Exception {

		// test no operation id
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());

		// test invalid operation id
		action.setOperationId(9999L);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());
	}
}
