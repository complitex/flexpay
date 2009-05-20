package org.flexpay.bti.service;

import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttributeBase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestBuildingAttributeService extends SpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeService attributeService;

	@Test
	public void testGetAttributes() {

		List<BuildingAttributeBase> attributes = attributeService.listAttributes(
				new Stub<BtiBuilding>(1L), new Page<BuildingAttributeBase>());
		assertFalse("No attributes found", attributes.isEmpty());

		for (BuildingAttributeBase attr : attributes) {
			assertNotNull("No attribute value", attr.getCurrentValue());
		}
	}

	@Test
	public void testGetAttributes2() {

		List<BuildingAttributeBase> attributes = attributeService.listAttributes(new Stub<BtiBuilding>(1L));
		assertFalse("No attributes found", attributes.isEmpty());

		for (BuildingAttributeBase attr : attributes) {
			assertNotNull("No attribute value", attr.getCurrentValue());
		}
	}
}
