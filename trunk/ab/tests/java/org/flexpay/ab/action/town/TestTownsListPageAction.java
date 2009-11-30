package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownsListPageAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownsListPageAction action;

	@Test
	public void testAction1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", new Long(0), up.getRegionFilter());

	}

	@Test
	public void testAction2() throws Exception {

		action.setRegionFilter(TestData.REGION_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", TestData.REGION_NSK.getId(), up.getRegionFilter());

	}

	@Test
	public void testAction3() throws Exception {

		action.setRegionFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", new Long(0), up.getRegionFilter());

	}

}
