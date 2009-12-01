package org.flexpay.ab.action.measureunit;

import org.flexpay.ab.actions.measureunit.MeasureUnitsListAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMeasureUnitsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private MeasureUnitsListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid town types list size", action.getUnits().size() > 0);

	}

}
