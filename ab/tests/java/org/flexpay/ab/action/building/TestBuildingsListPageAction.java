package org.flexpay.ab.action.building;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingsListPageAction action;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetDaoExt streetDaoExt;

	@Test
	public void testIncorrectFilterValue1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getStreetFilter());

	}

	@Test
	public void testIncorrectFilterValue2() throws Exception {

		action.setStreetFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getStreetFilter());

	}

	@Test
	public void testDefunctStreet() throws Exception {

		action.setStreetFilter(123230L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getStreetFilter());

	}

	@Test
	public void testDisabledStreet() throws Exception {

		Street street = createSimpleStreet("123");
		street.disable();
		streetDao.create(street);

		action.setStreetFilter(street.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of streetFilter in user preferences", new Long(0), up.getStreetFilter());

		streetDaoExt.deleteStreet(street);

	}

	@Test
	public void testAction() throws Exception {

		action.setStreetFilter(TestData.IVANOVA.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of streetFilter in user preferences", TestData.IVANOVA.getId(), up.getStreetFilter());

	}

}
