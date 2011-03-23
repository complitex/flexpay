package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestServiceViewAction  extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceViewAction serviceViewAction;

	@Test
	public void testExecute() throws Exception {

		// test new service
		serviceViewAction.setService(new Service());
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, serviceViewAction.execute());
		assertTrue("Action must give an error", serviceViewAction.hasActionErrors());

		// test bad service
		Service badService = new Service();
		badService.setId(9999L);
		serviceViewAction.setService(badService);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, serviceViewAction.execute());
		assertTrue("Action must give an error", serviceViewAction.hasActionErrors());
	}
}
