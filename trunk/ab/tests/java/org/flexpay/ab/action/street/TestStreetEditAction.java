package org.flexpay.ab.action.street;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
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

public class TestStreetEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetEditAction action;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private TownDao townDao;
	@Autowired
	private StreetTypeDao streetTypeDao;

	@Test
	public void testNullNames() throws Exception {

		action.setStreet(new Street(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullBeginDateFilter() throws Exception {

		action.setStreet(new Street(0L));
		action.setBeginDateFilter(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setStreet(new Street(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setStreet(new Street(TestData.IVANOVA));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", action.getStreet().getCurrentNameTemporal().getBegin(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid town filter", TestData.TOWN_NSK.getId(), action.getTownFilter());
		assertEquals("Invalid country filter", TestData.COUNTRY_RUS.getId(), action.getCountryFilter());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectNamesParameters() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		Map<Long, String> names = treeMap();
		names.put(564L, "test");

		action.setSubmitted("");
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setNames(names);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid names map size", getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setBeginDateFilter(null);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectTownId1() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(-10L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectTownId2() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(0L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctTown() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(1212330L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledTown() throws Exception {

		Town town = createSimpleTown("321");
		town.disable();
		townDao.create(town);

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(town.getId());
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		townDao.delete(town);

	}

	@Test
	public void testIncorrectStreetTypeId1() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		StreetTypeFilter streetTypeFilter = new StreetTypeFilter();
		streetTypeFilter.setSelectedId(-10L);
		action.setStreetTypeFilter(streetTypeFilter);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectStreetTypeId2() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		StreetTypeFilter streetTypeFilter = new StreetTypeFilter();
		streetTypeFilter.setSelectedId(0L);
		action.setStreetTypeFilter(streetTypeFilter);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectStreetTypeId3() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		StreetTypeFilter streetTypeFilter = new StreetTypeFilter();
		streetTypeFilter.setSelectedId(null);
		action.setStreetTypeFilter(streetTypeFilter);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid street id", action.getStreet().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		streetDao.delete(action.getStreet());

	}

	@Test
	public void testNullStreetType() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("11111"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setStreetTypeFilter(null);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid street id", action.getStreet().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		streetDao.delete(action.getStreet());
	}

	@Test
	public void testDefunctStreetType() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		StreetTypeFilter streetTypeFilter = new StreetTypeFilter();
		streetTypeFilter.setSelectedId(121210L);
		action.setStreetTypeFilter(streetTypeFilter);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledStreetType() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		StreetType streetType = createSimpleStreetType("ggg");
		streetType.disable();
		streetTypeDao.create(streetType);

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		StreetTypeFilter streetTypeFilter = new StreetTypeFilter();
		streetTypeFilter.setSelectedId(streetType.getId());
		action.setStreetTypeFilter(streetTypeFilter);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		streetTypeDao.delete(streetType);

	}

	@Test
	public void testNullStreet() throws Exception {

		action.setStreet(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullStreetId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectStreetId() throws Exception {

		action.setStreet(new Street(-10L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditDefunctStreet() throws Exception {

		action.setStreet(new Street(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditDisabledStreet() throws Exception {

		Street street = createSimpleStreet("testName");
		street.disable();
		streetDao.create(street);

		action.setStreet(street);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		streetDao.delete(street);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setNames(initNames("123222"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid street id", action.getStreet().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		streetDao.delete(action.getStreet());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Street street = createSimpleStreet("testName");
		streetDao.create(street);

		action.setStreet(street);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.next(DateUtil.now())));
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		String name = action.getStreet().getNameForDate(DateUtil.next(DateUtil.now())).getDefaultNameTranslation();
		assertEquals("Invalid town name value", "123", name);

		streetDao.delete(action.getStreet());
	}

}
