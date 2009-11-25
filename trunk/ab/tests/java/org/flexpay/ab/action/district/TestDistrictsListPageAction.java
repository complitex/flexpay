package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictsListPageAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictsListPageAction action;

	@Test
	public void testAction1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

}
