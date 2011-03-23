package org.flexpay.ab.action.person;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimplePerson;
import static org.junit.Assert.*;

public class TestPersonSaveFIOAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonSaveFIOAction action;
	@Autowired
	private ApartmentDao apartmentDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PersonDaoExt personDaoExt;

	@Test
	public void testNullPerson() throws Exception {

		action.setPerson(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctPerson() throws Exception {

		action.setPerson(new Person(101010L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledPerson() throws Exception {

		Person person = createSimplePerson("person");
		person.disable();
		personDao.create(person);

		action.setPerson(person);
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		personDaoExt.deletePerson(person);

	}

	@Test
	public void testAction() throws Exception {

		action.getIdentity().setFirstName("first");
		action.getIdentity().setMiddleName("middle");
		action.getIdentity().setLastName("last");
		action.getIdentity().setBirthDate(null);

		assertEquals("Invalid action result1", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.1", action.hasActionErrors());
		assertTrue("Identity must be default1", action.getIdentity().isDefault());

		action.setIdentity(new PersonIdentity());
		action.getIdentity().setFirstName("first222");
		action.getIdentity().setMiddleName("middle222");
		action.getIdentity().setLastName("last222");
		action.getIdentity().setBirthDate(null);

		assertEquals("Invalid action result2", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.2", action.hasActionErrors());
		assertEquals("Invalid person last name2", "last222", action.getPerson().getDefaultIdentity().getLastName());

	}

}
