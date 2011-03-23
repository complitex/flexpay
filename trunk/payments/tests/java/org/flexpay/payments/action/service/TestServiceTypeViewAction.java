package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
