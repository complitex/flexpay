package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestServiceTypeEditAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceTypeEditAction serviceTypeEditAction;

	@Test
	public void testExecute() throws Exception {

		// test not submit
		assertEquals("Invalid action result", FPActionSupport.INPUT, serviceTypeEditAction.execute());

		// test unknown service type
		ServiceType badServiceType = new ServiceType();
		badServiceType.setId(9999L);
		serviceTypeEditAction.setServiceType(badServiceType);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, serviceTypeEditAction.execute());
		assertTrue("Action must give an error", serviceTypeEditAction.hasActionErrors());
	}
}
