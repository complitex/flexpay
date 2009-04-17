package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAddressService extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressService addressService;

	@Test
	public void testGetBuildingAddress() throws Throwable {

		addressService.getBuildingAddress(new Stub<Building>(1L), null);
	}

	@Test
	public void testGetBuildingsAddress() throws Throwable {

		addressService.getBuildingsAddress(new Stub<BuildingAddress>(1L), null);
	}

	@Test
	public void testGetApartmentAddress() throws Throwable {

		addressService.getAddress(new Stub<Apartment>(1L), null);
	}
}
