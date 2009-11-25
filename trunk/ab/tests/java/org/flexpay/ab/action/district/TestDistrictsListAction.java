package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

}
