package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionViewAction;
import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestNTDUtils.createSimpleRegion;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegionViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionViewAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testCorrectData() throws Exception {

		action.setObject(new Region(TestData.REGION_NSK.getId()));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setObject(new Region(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setObject(new Region(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullRegion() throws Exception {

		action.setObject(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctRegion() throws Exception {

		action.setObject(new Region(1090772L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledRegion() throws Exception {

		Region region = createSimpleRegion("testName");
		region.setStatus(DomainObjectWithStatus.STATUS_DISABLED);

		regionDao.create(region);

		action.setObject(region);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		regionDao.delete(action.getObject());

	}

}
