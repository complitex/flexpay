package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictViewAction;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleDistrict;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictViewAction action;
	@Autowired
	private DistrictDao districtDao;

	@Test
	public void testCorrectData() throws Exception {

		action.setObject(new District(TestData.DISTRICT_SOVETSKIY));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setObject(new District(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setObject(new District(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullDistrict() throws Exception {

		action.setObject(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctDistrict() throws Exception {

		action.setObject(new District(1090772L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledDistrict() throws Exception {

		District district = createSimpleDistrict("testName");
		district.disable();

		districtDao.create(district);

		action.setObject(district);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		districtDao.delete(action.getObject());

	}

}
