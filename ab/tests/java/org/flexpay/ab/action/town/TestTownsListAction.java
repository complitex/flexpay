package org.flexpay.ab.action.town;

import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleRegion;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownsListAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testAction() throws Exception {

		action.setRegionFilter(TestData.REGION_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid towns list size", action.getTowns().isEmpty());

	}

	@Test
	public void testIncorrectSorterByName() throws Exception {

		action.setRegionFilter(TestData.REGION_NSK.getId());
		action.setTownSorterByName(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid towns list size", action.getTowns().isEmpty());

	}

	@Test
	public void testIncorrectSorterByType() throws Exception {

		action.setRegionFilter(TestData.REGION_NSK.getId());
		action.setTownSorterByType(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid towns list size", action.getTowns().isEmpty());

	}

	@Test
	public void testIncorrectRegionFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Towns list size must be 0", action.getTowns().isEmpty());

	}

	@Test
	public void testIncorrectRegionFilter2() throws Exception {

		action.setRegionFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Towns list size must be 0", action.getTowns().isEmpty());

	}

	@Test
	public void testIncorrectRegionFilter3() throws Exception {

		action.setRegionFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Towns list size must be 0", action.getTowns().isEmpty());

	}

	@Test
	public void testDefunctRegion() throws Exception {

		action.setRegionFilter(234334L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Towns list size must be 0", action.getTowns().isEmpty());

	}

	@Test
	public void testDisabledRegion() throws Exception {

		Region region = createSimpleRegion("123");
		region.disable();
		regionDao.create(region);

		action.setRegionFilter(region.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Towns list size must be 0", action.getTowns().isEmpty());

		regionDao.delete(region);

	}

}
