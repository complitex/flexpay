package org.flexpay.ab.action.apartment;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.flexpay.ab.util.TestUtils.createSimpleApartment;
import static org.flexpay.ab.util.TestUtils.createSimplePerson;
import static org.junit.Assert.*;

public class TestApartmentRegistrationAction extends AbSpringBeanAwareTestCase {

	@Autowired
	@Qualifier("apartmentRegistrationAction")
	private ApartmentRegistrationAction action;
	@Autowired
	private ApartmentDao apartmentDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PersonDaoExt personDaoExt;

	@Test
	public void testNullApartment() throws Exception {

		action.setApartment(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullApartmentId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectApartmentId1() throws Exception {

		action.setApartment(new Apartment(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectApartmentId2() throws Exception {

		action.setApartment(new Apartment(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctApartment() throws Exception {

		action.setApartment(new Apartment(121210L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action messages.", action.hasActionMessages());

	}

	@Test
	public void testDisabledApartment() throws Exception {

		Apartment apartment = createSimpleApartment("22222211");
		apartment.disable();
		apartmentDao.create(apartment);
		Person person = createSimplePerson("www");
		person.setRegistrationApartment(apartment);
		personDao.create(person);

		action.setApartment(apartment);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		personDaoExt.deletePerson(person);
		apartmentDao.delete(apartment);

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

		action.setApartment(new Apartment(TestData.IVANOVA_27_330));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

}
