package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownEditAction;
import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestNTDUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownEditAction action;
	@Autowired
	private TownDao townDao;

	@Test
	public void testIncorrectId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setTown(new Town(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setTown(new Town(TestData.TOWN_NSK.getId()));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", action.getTown().getCurrentNameTemporal().getBegin(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setTown(new Town(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setTown(new Town(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setRegionFilter(TestData.REGION_NSK.getId());
		action.setBeginDateFilter(null);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData3() throws Exception {

		action.setTown(new Town(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setRegionFilter(TestData.REGION_NSK.getId());
		TownTypeFilter townTypeFilter = new TownTypeFilter();
		townTypeFilter.setSelectedId(-10L);
		action.setTownTypeFilter(townTypeFilter);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
	}

	@Test
	public void testIncorrectData4() throws Exception {

		action.setTown(new Town(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setTown(new Town(121212L));
		action.setNames(initNames("123"));
		action.setRegionFilter(TestData.REGION_NSK.getId());
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setTown(new Town(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setRegionFilter(TestData.REGION_NSK.getId());
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		townDao.delete(action.getTown());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Town town = new Town();
		TownName townName = new TownName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			townName.setTranslation(new TownNameTranslation("testName", lang));
		}
		town.setNameForDate(townName, DateUtil.now());
		town.setParent(new Region(TestData.REGION_NSK.getId()));

		townDao.create(town);

		action.setTown(town);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setRegionFilter(TestData.REGION_NSK.getId());
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.next(DateUtil.now())));
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		townDao.delete(town);
	}

}
