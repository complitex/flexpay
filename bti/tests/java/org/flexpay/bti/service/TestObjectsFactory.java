package org.flexpay.bti.service;

import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.test.BtiSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class TestObjectsFactory extends BtiSpringBeanAwareTestCase {

	@Autowired
	private ObjectsFactory factory;

	@Test
	public void testFactory() {

		assertTrue("Invalid object type", factory.newApartment() instanceof BtiApartment);
		assertTrue("Invalid object type", factory.newBuilding() instanceof BtiBuilding);
	}
}
