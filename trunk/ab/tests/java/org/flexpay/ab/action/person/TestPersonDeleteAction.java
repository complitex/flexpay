package org.flexpay.ab.action.person;

import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimplePerson;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;

public class TestPersonDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonDeleteAction action;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PersonDaoExt personDaoExt;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testDeletePersons() throws Exception {

		Person person = createSimplePerson("testPerson");
		personDao.create(person);

		action.setObjectIds(set(person.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		person = personDao.read(person.getId());
		assertTrue("Invalid status for town type. Must be disabled", person.isNotActive());

		personDaoExt.deletePerson(person);
	}

}
