package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetsListPageAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetsListPageAction action;

	@Test
	public void testAction1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", new Long(0), up.getTownFilter());

	}

	@Test
	public void testAction2() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", TestData.TOWN_NSK.getId(), up.getTownFilter());

	}

	@Test
	public void testAction3() throws Exception {

		action.setTownFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", new Long(0), up.getTownFilter());

	}

}
