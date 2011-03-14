package org.flexpay.ab.action.building;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleDistrict;
import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import static org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber;
import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TestBuildingCreateAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingCreateAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetDaoExt streetDaoExt;
	@Autowired
	private DistrictDao districtDao;
	@Autowired
	private AddressAttributeTypeService addressAttributeTypeService;

	@Test
	public void testNullAttributesMap() throws Exception {

		action.setAttributesMap(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid size of attributesMap", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());

	}

	@Test
	public void testIncorrectAttributesParameter() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		Map<Long, String> attributes = treeMap();
		attributes.put(564L, "test");

		action.setSubmitted("");
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setAttributesMap(attributes);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid attributes map size", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid attributes map size", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullStreetFilter() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(null);
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectStreetFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(-10L);
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectStreetFilter2() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(0L);
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctStreet() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(122220L);
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledStreet() throws Exception {

		Street street = createSimpleStreet("321");
		street.disable();
		streetDao.create(street);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(street.getId());
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		streetDaoExt.deleteStreet(street);

	}

	@Test
	public void testNullDistrictFilter() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectDistrictFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectDistrictFilter2() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctDistrict() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(12122220L);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledDistrict() throws Exception {

		District district = createSimpleDistrict("321");
		district.disable();
		districtDao.create(district);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(district.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		districtDao.delete(district);

	}

	@Test
	public void testIncorrectBuildingNumber() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1111");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid building id", action.getBuilding().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		buildingDao.delete(action.getBuilding());
	}

}
