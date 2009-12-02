package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionDeleteAction;
import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleRegion;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegionDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionDeleteAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		
	}

	@Test
	public void testDeleteRegions() throws Exception {

		Region region = createSimpleRegion("testName");

		regionDao.create(region);

		action.setObjectIds(set(region.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		region = regionDao.read(region.getId());
		assertTrue("Invalid status for region. Must be disabled", region.isNotActive());

		regionDao.delete(region);
	}

}