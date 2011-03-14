package org.flexpay.ab.action.person;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleApartment;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonEditRegistrationFormAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonEditRegistrationFormAction action;
	@Autowired
	private ApartmentDao apartmentDao;

	@Test
	public void testNullApartmentFilter() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectApartmentFilter1() throws Exception {

		action.setApartmentFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectApartmentFilter2() throws Exception {

		action.setApartmentFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctApartmentFilter() throws Exception {

		action.setApartmentFilter(1212120L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledApartmentFilter() throws Exception {

		Apartment apartment = createSimpleApartment("222222");
		apartment.disable();
		apartmentDao.create(apartment);

		action.setApartmentFilter(apartment.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		apartmentDao.delete(apartment);

	}

	@Test
	public void testAction() throws Exception {

		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertNotNull("Invalid country filter value", action.getCountryFilter());
		assertNotNull("Invalid region filter value", action.getRegionFilter());
		assertNotNull("Invalid town filter value", action.getTownFilter());
		assertNotNull("Invalid street filter value", action.getStreetFilter());
		assertNotNull("Invalid building filter value", action.getBuildingFilter());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

}
