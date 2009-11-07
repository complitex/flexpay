package org.flexpay.ab.service;

import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAddressService extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressService addressService;

	@Test
	public void testGetBuildingAddress() throws Throwable {

		addressService.getBuildingAddress(TestData.IVANOVA_2, null);
	}

	@Test
	public void testGetBuildingsAddress() throws Throwable {

		addressService.getBuildingsAddress(TestData.ADDR_IVANOVA_2, null);
	}

	@Test
	public void testGetApartmentAddress() throws Throwable {

		addressService.getAddress(TestData.IVANOVA_27_1, null);
	}
}
