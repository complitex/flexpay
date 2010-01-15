package org.flexpay.payments.action.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.service.ServiceTypeViewAction;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestServiceTypeViewAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceTypeViewAction serviceTypeViewAction;

	@Test
	public void testExecute() throws Exception {

		// test new service type
		serviceTypeViewAction.setServiceType(new ServiceType());
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, serviceTypeViewAction.execute());
		assertTrue("Action must give an error", serviceTypeViewAction.hasActionErrors());

		// test bad service type
		ServiceType badServiceType = new ServiceType();
		badServiceType.setId(9999L);
		serviceTypeViewAction.setServiceType(badServiceType);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, serviceTypeViewAction.execute());
		assertTrue("Action must give an error", serviceTypeViewAction.hasActionErrors());
	}
}
