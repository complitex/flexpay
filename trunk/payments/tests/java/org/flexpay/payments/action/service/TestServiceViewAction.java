package org.flexpay.payments.action.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.service.ServiceViewAction;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
