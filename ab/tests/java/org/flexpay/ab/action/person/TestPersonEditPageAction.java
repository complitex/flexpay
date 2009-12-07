package org.flexpay.ab.action.person;

import org.flexpay.ab.actions.person.PersonEditPageAction;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonEditPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonEditPageAction action;
	@Autowired
	private PersonDao personDao;

	@Test
	public void testNullPerson() throws Exception {

		action.setPerson(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullPersonId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNewPersonAction() throws Exception {

		action.setPerson(new Person(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setPerson(new Person(-10L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testDefunctPerson() throws Exception {

		action.setPerson(new Person(101010L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	//TODO
	@Test
	public void testDisabledPerson() throws Exception {

		action.setPerson(new Person(101010L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNotNewPerson() throws Exception {

		action.setPerson(new Person(TestData.PERSON_1));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid person id", TestData.PERSON_1.getId(), action.getPerson().getId());

	}

}
