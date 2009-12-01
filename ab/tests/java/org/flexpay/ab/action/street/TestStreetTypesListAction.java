package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetTypesListAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetTypesListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetTypesListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid street types list size", action.getStreetTypes().size() > 0);

	}

}