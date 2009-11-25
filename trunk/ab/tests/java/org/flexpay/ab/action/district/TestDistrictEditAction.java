package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictEditAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictEditAction action;

	@Test
	public void testIncorrectId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

}
