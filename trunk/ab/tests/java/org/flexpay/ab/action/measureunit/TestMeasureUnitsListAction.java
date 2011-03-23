package org.flexpay.ab.action.measureunit;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestMeasureUnitsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private MeasureUnitsListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid measure units list size", action.getUnits().isEmpty());

	}

}
