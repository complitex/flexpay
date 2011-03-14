package org.flexpay.ab.action.person;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimplePerson;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonSaveRegistrationAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonSaveRegistrationAction action;
	@Autowired
	private ApartmentDao apartmentDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PersonDaoExt personDaoExt;

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

/*
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
*/

	@Test
	public void testNullPerson() throws Exception {

		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());
		action.setPerson(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullPersonId() throws Exception {

		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNewPerson() throws Exception {

		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());
		action.setPerson(new Person(0L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectPersonId() throws Exception {

		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());
		action.setPerson(new Person(-10L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctPerson() throws Exception {

		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());
		action.setPerson(new Person(101010L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledPerson() throws Exception {

		Person person = createSimplePerson("simplePerson");
		person.disable();
		personDao.create(person);

		action.setPerson(person);
		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		personDaoExt.deletePerson(person);

	}

	@Test
	public void testSaveRegistration() throws Exception {

		Person person = createSimplePerson("testPerson");
		personDao.create(person);

		action.setPerson(person);
		action.setApartmentFilter(TestData.IVANOVA_27_1.getId());
		action.setBeginDate("2009/01/12");
		action.setEndDate("2019/03/25");

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		Apartment apartment = action.getPerson().getRegistrationApartment();
		assertNotNull("No registration set", apartment);
		assertEquals("Incorrect apartment id", TestData.IVANOVA_27_1.getId(), apartment.getId());

		action.setApartmentFilter(TestData.IVANOVA_27_330.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		apartment = action.getPerson().getRegistrationApartment();
		assertNotNull("No registration set 2", apartment);
		assertEquals("Incorrect update apartment id", TestData.IVANOVA_27_330.getId(), apartment.getId());

		personDaoExt.deletePerson(action.getPerson());

	}

}
