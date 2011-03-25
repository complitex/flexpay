package org.flexpay.payments.action.operations;

import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCreateBlankOperationAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private CreateBlankOperationAction action;

	@Test
	public void doExecute() throws Exception {

		// test logic
		action.setCashboxId(1L);
		action.execute();
		assertEquals("Bad status", 0, action.getStatus());
		assertTrue("Bad operation id", action.getOperationId() > 0);
	}
}
