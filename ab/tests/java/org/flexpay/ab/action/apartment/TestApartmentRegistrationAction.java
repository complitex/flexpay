package org.flexpay.ab.action.apartment;

import org.flexpay.ab.actions.apartment.ApartmentRegistrationAction;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleApartment;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestApartmentRegistrationAction extends AbSpringBeanAwareTestCase {

	@Autowired
	@Qualifier("apartmentRegistrationAction")
	private ApartmentRegistrationAction action;
	@Autowired
	private ApartmentDao apartmentDao;

	@Test
	public void testNullApartment() throws Exception {

		action.setApartment(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullApartmentId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectApartmentId1() throws Exception {

		action.setApartment(new Apartment(-10L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectApartmentId2() throws Exception {

		action.setApartment(new Apartment(0L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testApartmentWithoutRegistrations() throws Exception {

		Apartment apartment = createSimpleApartment("222222");

		apartmentDao.create(apartment);

		action.setApartment(apartment);
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action messages.", action.hasActionMessages());

		apartmentDao.delete(apartment);

	}

	@Test
	public void testAction() throws Exception {

		action.setApartment(new Apartment(TestData.IVANOVA_27_330.getId()));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

}
