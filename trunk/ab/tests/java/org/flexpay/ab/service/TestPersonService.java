package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestPersonService extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonService personService;
	@Autowired
	private IdentityTypeService typeService;
	@Autowired
	private ApartmentService apartmentService;

	private Date DT_1990_12_01 = new GregorianCalendar(1990, Calendar.DECEMBER, 1).getTime();
	private Date DT_2009_12_01 = new GregorianCalendar(2009, Calendar.DECEMBER, 1).getTime();

	@Test (expected = FlexPayExceptionContainer.class)
	public void testCreateEmptyPerson() throws Exception {

		Person person = new Person();
		personService.create(person);
	}

	@Test
	public void testCreatePerson() throws Exception {

/*
		Person person = new Person();
		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(typeService.findTypeByName(IdentityType.TYPE_FIO));
		identity.setFirstName("TEST FIRST NAME");
		identity.setMiddleName("TEST MIDDLE NAME");
		identity.setLastName("TEST LAST NAME");
		identity.setBeginDate(DT_1990_12_01);
		identity.setBirthDate(new GregorianCalendar(1970, Calendar.OCTOBER, 14).getTime());
		identity.setEndDate(ApplicationConfig.getFutureInfinite());
		identity.setOrganization("");
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setDefault(true);
		person.addIdentity(identity);

		personService.create(person);
*/
	}

	@Test
	public void testUpdatePerson() throws Exception {

/*
		Person person = personService.read(new Stub<Person>(1L));
		assertNotNull("No person found", person);

		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(typeService.findTypeByName(IdentityType.TYPE_FIO));
		identity.setFirstName("TEST FIRST NAME");
		identity.setMiddleName("TEST MIDDLE NAME");
		identity.setLastName("TEST LAST NAME");
		identity.setBeginDate(DT_1990_12_01);
		identity.setBirthDate(new GregorianCalendar(1970, Calendar.OCTOBER, 14).getTime());
		identity.setEndDate(ApplicationConfig.getFutureInfinite());
		identity.setOrganization("");
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setDefault(true);
		person.addIdentity(identity);

		personService.update(person);
*/
	}

	@Test
	public void testUpdatePersonRegistration() throws Exception {

		Person person = personService.readFull(new Stub<Person>(1L));
		assertNotNull("No person found", person);

		Apartment ap = apartmentService.readFull(new Stub<Apartment>(1L));
		person.setPersonRegistration(ap, DT_2009_12_01);
		personService.update(person);

		assertEquals("Invalid registration", ap, person.getRegistrationApartment(DT_2009_12_01));
	}
}
