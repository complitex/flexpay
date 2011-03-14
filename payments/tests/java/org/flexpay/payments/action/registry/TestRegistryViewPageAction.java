package org.flexpay.payments.action.registry;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegistryViewPageAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private RegistryViewPageAction action;

	@Test
	public void testSimple() throws Exception {

		action.setRegistry(new Registry(1L));
		assertEquals("Action failed", FPActionSupport.SUCCESS, action.execute());

	}
}
