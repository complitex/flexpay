package org.flexpay.ab.action.identity;

import org.flexpay.ab.actions.identity.IdentityTypesListAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestIdentityTypesListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private IdentityTypesListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid identity types list size", action.getIdentityTypes().size() > 0);

	}

}