package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionEditAction;
import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestNTDUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TestRegionEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionEditAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testIncorrectId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

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

		action.setRegion(new Region(TestData.REGION_NSK.getId()));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", action.getRegion().getCurrentNameTemporal().getBegin(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

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
		action.setRegion(new Region(121212L));
		action.setNames(initNames("123"));
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setRegion(new Region(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		regionDao.delete(action.getRegion());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Region region = new Region();
		RegionName regionName = new RegionName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			regionName.setTranslation(new RegionNameTranslation("testName", lang));
		}
		region.setNameForDate(regionName, DateUtil.now());
		region.setParent(new Country(TestData.COUNTRY_RUS.getId()));

		regionDao.create(region);

		action.setRegion(region);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.next(DateUtil.now())));
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		regionDao.delete(action.getRegion());
	}

}
