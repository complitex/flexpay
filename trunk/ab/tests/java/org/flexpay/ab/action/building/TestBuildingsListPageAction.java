package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingsListPageAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingsListPageAction action;

	@Test
	public void testAction1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of streetFilter in user preferences", new Long(0), up.getStreetFilter());

	}

	@Test
	public void testAction2() throws Exception {

		action.setStreetFilter(TestData.IVANOVA.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of streetFilter in user preferences", TestData.IVANOVA.getId(), up.getStreetFilter());

	}

	@Test
	public void testAction3() throws Exception {

		action.setStreetFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of streetFilter in user preferences", new Long(0), up.getStreetFilter());

	}

}
