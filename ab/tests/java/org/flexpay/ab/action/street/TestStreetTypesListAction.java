package org.flexpay.ab.action.street;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetTypesListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetTypesListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid street types list size", action.getStreetTypes().isEmpty());

	}

}