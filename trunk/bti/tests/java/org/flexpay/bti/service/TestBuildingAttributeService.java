package org.flexpay.bti.service;

import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttribute;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestBuildingAttributeService extends SpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeService attributeService;

	@Test
	public void testGetAttributes() {

		List<BuildingAttribute> attributes = attributeService.listAttributes(
				new Stub<BtiBuilding>(1L), new Page<BuildingAttribute>());
		assertFalse("No attributes found", attributes.isEmpty());

		for (BuildingAttribute attr : attributes) {
			assertNotNull("No attribute value", attr.getStringValue());
		}
	}

	@Test
	public void testGetAttributes2() {

		List<BuildingAttribute> attributes = attributeService.listAttributes(new Stub<BtiBuilding>(1L));
		assertFalse("No attributes found", attributes.isEmpty());

		for (BuildingAttribute attr : attributes) {
			assertNotNull("No attribute value", attr.getStringValue());
		}
	}
}
