package org.flexpay.ab.action.person;

import org.flexpay.ab.actions.person.PersonViewAction;
import org.flexpay.ab.actions.person.PersonViewIdentitiesAction;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonViewIdentitiesAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonViewIdentitiesAction action;
	@Autowired
	private PersonDao personDao;

	@Test
	public void testNullPerson() throws Exception {

		action.setPerson(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNewPersonAction() throws Exception {

		action.setPerson(new Person(0L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectPersonId1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectPerson2() throws Exception {

		action.setPerson(new Person(-10L));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctPerson() throws Exception {

		action.setPerson(new Person(101010L));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	//TODO
	@Test
	public void testDisabledPerson() throws Exception {

		action.setPerson(new Person(101010L));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testAction() throws Exception {

		action.setPerson(new Person(TestData.PERSON_1));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

}
