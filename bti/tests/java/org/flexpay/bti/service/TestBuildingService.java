package org.flexpay.bti.service;

import org.flexpay.bti.dao.BuildingAttributeDao;
import org.flexpay.bti.persistence.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.dao.support.DataAccessUtils.intResult;

import java.util.List;

public class TestBuildingService extends SpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeService attributeService;
	@Autowired
	private BuildingAttributeDao attributeDao;

	public static final Stub<BtiBuilding> BUILDING_STUB = new Stub<BtiBuilding>(1L);
	public static final Stub<BuildingAttributeType> ATTRIBUTE_TYPE_STUB = new Stub<BuildingAttributeType>(1L);

	@Test
	public void testAllBuildingsAreValid() throws Throwable {
		int nBuildings = intResult(hibernateTemplate.find("select count(*) from Building"));
		int nBtiBuildings = intResult(hibernateTemplate.find("select count(*) from BtiBuilding"));
		assertEquals("All building should be of the same type", nBuildings, nBtiBuildings);
	}

	@Test
	public void testListAttributes() {
		List<BuildingAttributeBase> attributes = attributeService.listAttributes(
				BUILDING_STUB, new Page<BuildingAttributeBase>());

		assertFalse("No attributes found", attributes.isEmpty());
	}

	@Test
	public void testCreateAttribute() {

		BuildingTempAttribute attribute = new BuildingTempAttribute();

		attribute.setBuilding(new BtiBuilding(BUILDING_STUB));
		attribute.setAttributeType(new BuildingAttributeTypeSimple(ATTRIBUTE_TYPE_STUB));
		attribute.setValueForDates("Test attribute value",
				ApplicationConfig.getPastInfinite(), ApplicationConfig.getPastInfinite());

		try {
			attributeDao.create(attribute);
		} finally {
			attributeDao.delete(attribute);
		}
	}
}
