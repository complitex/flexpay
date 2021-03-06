package org.flexpay.ab.action.person;

import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimplePerson;
import static org.junit.Assert.*;

public class TestPersonEditPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonEditPageAction action;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PersonDaoExt personDaoExt;

	@Test
	public void testNullPerson() throws Exception {

		action.setPerson(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullPersonId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNewPersonAction() throws Exception {

		action.setPerson(new Person(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectPersonId() throws Exception {

		action.setPerson(new Person(-10L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctPerson() throws Exception {

		action.setPerson(new Person(101010L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledPerson() throws Exception {

		Person person = createSimplePerson("simple21");
		person.disable();
		personDao.create(person);

		action.setPerson(person);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		personDaoExt.deletePerson(person);

	}

	@Test
	public void testNotNewPerson() throws Exception {

		action.setPerson(new Person(TestData.PERSON_1));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid person id", TestData.PERSON_1.getId(), action.getPerson().getId());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

}
