package org.flexpay.ab.action.district;

import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleTown;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictsListAction action;
	@Autowired
	private TownDao townDao;

	@Test
	public void testAction() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid districts list size", action.getDistricts().isEmpty());

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setDistrictSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid districts list size", action.getDistricts().isEmpty());

	}

	@Test
	public void testIncorrectTownFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Districts list size must be 0", action.getDistricts().isEmpty());

	}

	@Test
	public void testIncorrectTownFilter2() throws Exception {

		action.setTownFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Districts list size must be 0", action.getDistricts().isEmpty());

	}

	@Test
	public void testIncorrectTownFilter3() throws Exception {

		action.setTownFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Districts list size must be 0", action.getDistricts().isEmpty());

	}

	@Test
	public void testDefunctTown() throws Exception {

		action.setTownFilter(234334L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Districts list size must be 0", action.getDistricts().isEmpty());

	}

	@Test
	public void testDisabledTown() throws Exception {

		Town town = createSimpleTown("123");
		town.disable();
		townDao.create(town);

		action.setTownFilter(town.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Districts list size must be 0", action.getDistricts().isEmpty());

		townDao.delete(town);

	}

}
