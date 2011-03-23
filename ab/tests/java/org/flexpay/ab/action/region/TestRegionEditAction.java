package org.flexpay.ab.action.region;

import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.flexpay.ab.util.TestUtils.*;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.junit.Assert.*;

public class TestRegionEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionEditAction action;
	@Autowired
	private RegionDao regionDao;
	@Autowired
	private CountryDao countryDao;

	@Test
	public void testNullNames() throws Exception {

		action.setRegion(new Region(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullBeginDateFilter() throws Exception {

		action.setRegion(new Region(0L));
		action.setBeginDateFilter(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setRegion(new Region(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setRegion(new Region(TestData.REGION_NSK));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", action.getRegion().getCurrentNameTemporal().getBegin(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid country filter", TestData.COUNTRY_RUS.getId(), action.getCountryFilter());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectNamesParameters() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		Map<Long, String> names = treeMap();
		names.put(564L, "test");

		action.setSubmitted("");
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setNames(names);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid names map size", getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setBeginDateFilter(null);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectCountryId1() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setCountryFilter(-10L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectCountryId2() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setCountryFilter(0L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctCountry() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setCountryFilter(12324230L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledCountry() throws Exception {

		Country country = createSimpleCountry("123");
		country.disable();
		countryDao.create(country);

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setCountryFilter(country.getId());
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		countryDao.delete(country);

	}

	@Test
	public void testNullRegion() throws Exception {

		action.setRegion(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullRegionId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectRegionId() throws Exception {

		action.setRegion(new Region(-10L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditDefunctRegion() throws Exception {

		action.setRegion(new Region(121212L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditDisabledRegion() throws Exception {

		Region region = createSimpleRegion("testName");
		region.disable();
		regionDao.create(region);

		action.setRegion(region);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		regionDao.delete(region);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid region id", action.getRegion().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		regionDao.delete(action.getRegion());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Region region = createSimpleRegion("testName");
		regionDao.create(region);

		action.setRegion(region);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.next(DateUtil.now())));
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		String name = action.getRegion().getNameForDate(DateUtil.next(DateUtil.now())).getDefaultNameTranslation();
		assertEquals("Invalid region name value", "123", name);

		regionDao.delete(action.getRegion());
	}

}
