package org.flexpay.eirc.service;

import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.persistence.TestData.IVANOVA_27;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.dao.support.DataAccessUtils.intResult;

public class TestBuildingService extends EircSpringBeanAwareTestCase {

	@Autowired
	private BuildingService buildingService;

	@Test
	public void testUpdateServedBuilding() throws FlexPayExceptionContainer {

		ServedBuilding building = (ServedBuilding) buildingService.readFull(IVANOVA_27);
		assertNotNull("Building IVANOVA_27 not found", building);

		buildingService.update(building);
	}

	@Test
	public void testAllBuildingsAreValid() throws Throwable {
		Number nBuildings = intResult(jpaTemplate.find("select count(*) from Building"));
		Number nEircBuildings = intResult(jpaTemplate.find("select count(*) from ServedBuilding"));

		assertEquals("All buildings should be the ServedBuilding's", nBuildings.longValue(), nEircBuildings.longValue());
	}
}
