package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionEditAction;
import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleRegion;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegionEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionEditAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testNullRegion() throws Exception {

		action.setRegion(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullRegionId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullNames() throws Exception {

		action.setRegion(new Region(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testNullBeginDateFilter() throws Exception {

		action.setRegion(new Region(0L));
		action.setBeginDateFilter(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setRegion(new Region(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setRegion(new Region(TestData.REGION_NSK));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", action.getRegion().getCurrentNameTemporal().getBegin(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid country filter", TestData.COUNTRY_RUS.getId(), action.getCountryFilter());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setBeginDateFilter(null);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData3() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setCountryFilter(-10L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctRegion() throws Exception {

		action.setRegion(new Region(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledRegion() throws Exception {

		Region region = createSimpleRegion("testName");
		region.disable();

		regionDao.create(region);

		action.setRegion(region);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		regionDao.delete(region);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid region id", action.getRegion().getId() > 0);

		regionDao.delete(action.getRegion());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Region region = createSimpleRegion("testName");

		regionDao.create(region);

		action.setRegion(region);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.next(DateUtil.now())));
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String name = action.getRegion().getNameForDate(DateUtil.next(DateUtil.now())).getDefaultNameTranslation();
		assertEquals("Invalid region name value", "123", name);

		regionDao.delete(action.getRegion());
	}

}
