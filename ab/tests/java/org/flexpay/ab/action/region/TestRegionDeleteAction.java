package org.flexpay.ab.action.region;

import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleRegion;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;

public class TestRegionDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionDeleteAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDeleteRegions() throws Exception {

		Region region = createSimpleRegion("testName");
		regionDao.create(region);

		action.setObjectIds(set(region.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		region = regionDao.read(region.getId());
		assertTrue("Invalid status for region. Must be disabled", region.isNotActive());

		regionDao.delete(region);
	}

}
