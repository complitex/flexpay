package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestServiceEditAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceEditAction serviceEditAction;

	@Test
	public void testExecute() throws Exception{

		// test not submit
		assertEquals("Invalid action result", FPActionSupport.INPUT, serviceEditAction.execute());

		// test bad service id
		Service badService = new Service();
		badService.setId(9999L);
		serviceEditAction.setService(badService);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, serviceEditAction.execute());
		assertTrue("Action must give an error", serviceEditAction.hasActionErrors());
	}
}
