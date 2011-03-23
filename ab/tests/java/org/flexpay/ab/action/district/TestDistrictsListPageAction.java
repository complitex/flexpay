package org.flexpay.ab.action.district;

import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleTown;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTownStub;
import static org.junit.Assert.*;

public class TestDistrictsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictsListPageAction action;
	@Autowired
	private TownDao townDao;

	@Test
	public void testIncorrectFilterValue1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", getDefaultTownStub().getId(), up.getTownFilter());

	}

	@Test
	public void testIncorrectFilterValue2() throws Exception {

		action.setTownFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", getDefaultTownStub().getId(), up.getTownFilter());

	}

	@Test
	public void testDefunctTown() throws Exception {

		action.setTownFilter(234334L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", getDefaultTownStub().getId(), up.getTownFilter());

	}

	@Test
	public void testDisabledTown() throws Exception {

		Town town = createSimpleTown("123");
		town.disable();
		townDao.create(town);

		action.setTownFilter(town.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", getDefaultTownStub().getId(), up.getTownFilter());

		townDao.delete(town);

	}

	@Test
	public void testAction() throws Exception {

		action.setTownFilter(TestData.TOWN_BERDSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of townFilter in user preferences", TestData.TOWN_BERDSK.getId(), up.getTownFilter());

	}

}
