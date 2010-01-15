package org.flexpay.payments.action.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.payments.actions.registry.RegistryAnnotationAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegistryAnnotationAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private RegistryAnnotationAction action;

	@Test
	public void testExecute() throws Exception {

		// test cancel
		action.setCancel("cancel");
		assertEquals("Invalid action result", FPActionSupport.NONE, action.execute());
		action.setCancel(null);

		// test registry with no id
		action.setRegistry(new Registry());
		assertEquals("Invalid action result", FPActionSupport.ERROR, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());

		// test bad registry id
		Registry badRegistry = new Registry();
		badRegistry.setId(9999L);
		action.setRegistry(badRegistry);
		assertEquals("Invalid action result", FPActionSupport.ERROR, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());		
	}
}
