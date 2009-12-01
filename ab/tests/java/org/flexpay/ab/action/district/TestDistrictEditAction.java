package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictEditAction;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleDistrict;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictEditAction action;
	@Autowired
	private DistrictDao districtDao;

	@Test
	public void testNullDistrict() throws Exception {

		action.setDistrict(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullDistrictId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullNames() throws Exception {

		action.setDistrict(new District(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testNullBeginDateFilter() throws Exception {

		action.setDistrict(new District(0L));
		action.setBeginDateFilter(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setDistrict(new District(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setDistrict(new District(TestData.DISTRICT_SOVETSKII));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", action.getDistrict().getCurrentNameTemporal().getBegin(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid town filter", TestData.TOWN_NSK.getId(), action.getTownFilter());
		assertEquals("Invalid region filter", TestData.REGION_NSK.getId(), action.getRegionFilter());
		assertEquals("Invalid country filter", TestData.COUNTRY_RUS.getId(), action.getCountryFilter());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setDistrict(new District(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setDistrict(new District(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setBeginDateFilter(null);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData3() throws Exception {

		action.setDistrict(new District(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(-10L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctDistrict() throws Exception {

		action.setDistrict(new District(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledDistrict() throws Exception {

		District district = createSimpleDistrict("testName");
		district.disable();

		districtDao.create(district);

		action.setDistrict(district);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		districtDao.delete(district);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setDistrict(new District(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid district id", action.getDistrict().getId() > 0);

		districtDao.delete(action.getDistrict());
	}

	@Test
	public void testEditSubmit() throws Exception {

		District district = createSimpleDistrict("testName");

		districtDao.create(district);

		action.setDistrict(district);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.next(DateUtil.now())));
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String name = action.getDistrict().getNameForDate(DateUtil.next(DateUtil.now())).getDefaultNameTranslation();
		assertEquals("Invalid district name value", "123", name);

		districtDao.delete(action.getDistrict());
	}

}
