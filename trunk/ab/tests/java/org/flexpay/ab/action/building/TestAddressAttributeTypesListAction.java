package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.AddressAttributeTypesListAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAddressAttributeTypesListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressAttributeTypesListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid address attribute types list size", action.getTypes().size() > 0);

	}

}