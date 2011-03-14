package org.flexpay.payments.action.operations;

import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestCreateBlankOperationAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private CreateBlankOperationAction action;

	@Test
	public void doExecute() throws Exception {

		// test logic
		action.setCashboxId(1L);
		action.execute();
		assertEquals("Bad status", action.getStatus(), 0);
		assertTrue("Bad operation id", action.getOperationId() > 0);
	}
}
