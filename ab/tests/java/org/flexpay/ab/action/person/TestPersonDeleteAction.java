package org.flexpay.ab.action.person;

import org.flexpay.ab.actions.person.PersonDeleteAction;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimplePerson;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonDeleteAction action;
	@Autowired
	private PersonDao personDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testDeletePersons() throws Exception {

		Person person = createSimplePerson("testPerson");
		personDao.create(person);

		action.setObjectIds(set(person.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		person = personDao.read(person.getId());
		assertTrue("Invalid status for town type. Must be disabled", person.isNotActive());

		personDao.delete(person);
	}

}
