package org.flexpay.ab.service;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestPersonService extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonService personService;
	@Autowired
	private IdentityTypeService typeService;

	@Test (expected = FlexPayExceptionContainer.class)
	public void testCreateEmptyPerson() throws Exception {

		Person person = new Person();
		personService.create(person);
	}

	@Test
	public void testCreatePerson() throws Exception {

		Person person = new Person();
		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(typeService.getType(IdentityType.TYPE_FIO));
		identity.setFirstName("TEST FIRST NAME");
		identity.setMiddleName("TEST MIDDLE NAME");
		identity.setLastName("TEST LAST NAME");
		identity.setBeginDate(new GregorianCalendar(1990, Calendar.DECEMBER, 1).getTime());
		identity.setBirthDate(new GregorianCalendar(1970, Calendar.OCTOBER, 1498).getTime());
		identity.setEndDate(ApplicationConfig.getFutureInfinite());
		identity.setOrganization("");
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setDefault(true);
		person.addIdentity(identity);

		personService.create(person);
	}

	@Test
	public void testUpdatePerson() throws Exception {

		Person person = personService.read(new Stub<Person>(1L));
		assertNotNull("No person found", person);

		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(typeService.getType(IdentityType.TYPE_FIO));
		identity.setFirstName("TEST FIRST NAME");
		identity.setMiddleName("TEST MIDDLE NAME");
		identity.setLastName("TEST LAST NAME");
		identity.setBeginDate(new GregorianCalendar(1990, Calendar.DECEMBER, 1).getTime());
		identity.setBirthDate(new GregorianCalendar(1970, Calendar.OCTOBER, 1498).getTime());
		identity.setEndDate(ApplicationConfig.getFutureInfinite());
		identity.setOrganization("");
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setDefault(true);
		person.addIdentity(identity);

		personService.update(person);
	}
}
