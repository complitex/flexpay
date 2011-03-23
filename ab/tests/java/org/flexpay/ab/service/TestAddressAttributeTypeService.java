package org.flexpay.ab.service;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestAddressAttributeTypeService extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressAttributeTypeService service;

	@Test
	public void testGetSortedTypes() {

		List<AddressAttributeType> types = service.getAttributeTypes();
		assertTrue("Not enough data fetched", types.size() >= 3);

		assertEquals("Building number should go 1",
				ApplicationConfig.getBuildingAttributeTypeNumber(), types.get(0));
		assertEquals("Bulk number should go 2",
				ApplicationConfig.getBuildingAttributeTypeBulk(), types.get(1));
		assertEquals("Part number should go 3",
				ApplicationConfig.getBuildingAttributeTypePart(), types.get(2));
	}
}
