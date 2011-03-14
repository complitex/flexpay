package org.flexpay.ab.action.town;

import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleRegion;
import org.flexpay.ab.util.config.AbUserPreferences;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultRegionStub;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownsListPageAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testIncorrectFilterValue1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", getDefaultRegionStub().getId(), up.getRegionFilter());

	}

	@Test
	public void testIncorrectFilterValue2() throws Exception {

		action.setRegionFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", getDefaultRegionStub().getId(), up.getRegionFilter());

	}

	@Test
	public void testDefunctRegion() throws Exception {

		action.setRegionFilter(234334L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", getDefaultRegionStub().getId(), up.getRegionFilter());

	}

	@Test
	public void testDisabledRegion() throws Exception {

		Region region = createSimpleRegion("123");
		region.disable();
		regionDao.create(region);

		action.setRegionFilter(region.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", getDefaultRegionStub().getId(), up.getRegionFilter());

		regionDao.delete(region);

	}

	@Test
	public void testAction() throws Exception {

		action.setRegionFilter(TestData.REGION_TSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of regionFilter in user preferences", TestData.REGION_TSK.getId(), up.getRegionFilter());

	}

}
