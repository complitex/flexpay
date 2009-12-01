package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictDeleteAction;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleDistrict;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictDeleteAction action;
	@Autowired
	private DistrictDao districtDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testDeleteDistricts() throws Exception {

		District district = createSimpleDistrict("testName");

		districtDao.create(district);

		action.setObjectIds(set(district.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		district = districtDao.read(district.getId());
		assertTrue("Invalid status for district. Must be disabled", district.isNotActive());

		districtDao.delete(district);

	}

}
