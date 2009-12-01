package org.flexpay.ab.action.apartment;

import org.flexpay.ab.actions.apartment.ApartmentsListPageAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestApartmentsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentsListPageAction action;

	@Test
	public void testAction1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getBuildingFilter());

	}

	@Test
	public void testAction2() throws Exception {

		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", TestData.ADDR_IVANOVA_27.getId(), up.getBuildingFilter());

	}

	@Test
	public void testAction3() throws Exception {

		action.setBuildingFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getBuildingFilter());

	}

}
