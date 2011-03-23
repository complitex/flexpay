package org.flexpay.bti.service;

import org.flexpay.bti.dao.BuildingAttributeDao;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttribute;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeSimple;
import org.flexpay.bti.test.BtiSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.dao.support.DataAccessUtils.intResult;

public class TestBuildingService extends BtiSpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeService attributeService;
	@Autowired
	private BuildingAttributeDao attributeDao;
	@Autowired
	private BtiBuildingService buildingService;
	@Autowired
	private BuildingAttributeTypeService attributeTypeService;

	public static final Stub<BtiBuilding> BUILDING_STUB = new Stub<BtiBuilding>(1L);
	public static final Stub<BuildingAttributeType> ATTRIBUTE_TYPE_STUB = new Stub<BuildingAttributeType>(4L);

	@Test
	public void testAllBuildingsAreValid() throws Throwable {
		int nBuildings = intResult(hibernateTemplate.find("select count(*) from Building"));
		int nBtiBuildings = intResult(hibernateTemplate.find("select count(*) from BtiBuilding"));
		assertEquals("All building should be of the same type", nBuildings, nBtiBuildings);
	}

	@Test
	public void testListAttributes() {
		List<BuildingAttribute> attributes = attributeService.listAttributes(
				BUILDING_STUB, new Page<BuildingAttribute>());

		assertFalse("No attributes found", attributes.isEmpty());
	}

	@Test
	public void testCreateAttribute() {

		BuildingAttribute attribute = new BuildingAttribute();

		attribute.setBuilding(new BtiBuilding(BUILDING_STUB));
		attribute.setAttributeType(new BuildingAttributeTypeSimple(ATTRIBUTE_TYPE_STUB));
		attribute.setStringValue("Test attribute value");
		attribute.setTemporal(1);
		attribute.setBegin(ApplicationConfig.getPastInfinite());
		attribute.setEnd(ApplicationConfig.getPastInfinite());

		try {
			attributeDao.create(attribute);
		} finally {
			attributeDao.delete(attribute);
		}
	}

	@Test
	public void setSimpleAttribute() {

		BtiBuilding building = buildingService.readWithAttributes(BUILDING_STUB);
		assertNotNull("Building not found", building);

		BuildingAttributeType type = attributeTypeService.findTypeByName("Habitans count");
		BuildingAttribute attribute = new BuildingAttribute();
		attribute.setAttributeType(type);
		attribute.setStringValue("4");
		building.setNormalAttribute(attribute);

		buildingService.updateAttributes(building);
	}

	@Test
	public void setTemporalAttribute() {

		BtiBuilding building = buildingService.readWithAttributes(BUILDING_STUB);
		assertNotNull("Building not found", building);

		BuildingAttributeType type = attributeTypeService.findTypeByName("Habitans count");
		BuildingAttribute attribute = new BuildingAttribute();
		attribute.setAttributeType(type);
		attribute.setStringValue("4");
		building.setCurrentTmpAttribute(attribute);

		buildingService.updateAttributes(building);
	}
}
