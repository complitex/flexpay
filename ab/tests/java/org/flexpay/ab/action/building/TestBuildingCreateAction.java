package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingCreateAction;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingCreateAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingCreateAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private AddressAttributeTypeService addressAttributeTypeService;

	@Test
	public void testNullAttributesMap() throws Exception {

		action.setAttributesMap(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid size of attributesMap", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());

	}

	@Test
	public void testNullStreetFilter() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(null);
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectStreetFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(-10L);
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectStreetFilter2() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(0L);
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullDistrictFilter() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

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

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectBuildingNumber() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

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

		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1111");
		action.setSubmitted("");
		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setDistrictFilter(TestData.DISTRICT_SOVETSKIY.getId());

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid building id", action.getBuilding().getId() > 0);

		buildingDao.delete(action.getBuilding());
	}

}
