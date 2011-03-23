package org.flexpay.ab.action.building;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import static org.junit.Assert.*;

public class TestBuildingsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingsListAction action;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetDaoExt streetDaoExt;

	@Test
	public void testAction() throws Exception {

		action.setStreetFilter(TestData.IVANOVA.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid buildings list size", action.getBuildings().isEmpty());

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setBuildingsSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid buildings list size", action.getBuildings().isEmpty());

	}

	@Test
	public void testIncorrectStreetFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Buildings list size must be 0", action.getBuildings().isEmpty());

	}

	@Test
	public void testIncorrectStreetFilter2() throws Exception {

		action.setStreetFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Buildings list size must be 0", action.getBuildings().isEmpty());

	}

	@Test
	public void testIncorrectStreetFilter3() throws Exception {

		action.setStreetFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Buildings list size must be 0", action.getBuildings().isEmpty());

	}

	@Test
	public void testDefunctStreet() throws Exception {

		action.setStreetFilter(1212330L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Buildings list size must be 0", action.getBuildings().isEmpty());

	}

	@Test
	public void testDisabledStreet() throws Exception {

		Street street = createSimpleStreet("123");
		street.disable();
		streetDao.create(street);

		action.setStreetFilter(street.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Buildings list size must be 0", action.getBuildings().isEmpty());

		streetDaoExt.deleteStreet(street);

	}

}
