package org.flexpay.ab.action.person;

import org.flexpay.ab.actions.person.PersonSaveFIOAction;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonSaveFIOAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonSaveFIOAction action;
	@Autowired
	private ApartmentDao apartmentDao;
	@Autowired
	private PersonDao personDao;

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

	//TODO
	@Test
	public void testDisabledPerson() throws Exception {

		action.setPerson(new Person(101010L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertTrue("Identity must be default", action.getIdentity().isDefault());

	}

	@Test
	public void testAction2() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertTrue("Identity must be default", action.getIdentity().isDefault());

	}

}
