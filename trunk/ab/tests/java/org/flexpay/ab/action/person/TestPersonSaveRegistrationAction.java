package org.flexpay.ab.action.person;

import org.flexpay.ab.actions.person.PersonSaveRegistrationAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;

public class TestPersonSaveRegistrationAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonSaveRegistrationAction action;

	@Test
	@Repeat (5)
	public void testSaveRegistration() throws Exception {

		Person person = new Person(1L);
		action.setPerson(person);
		action.setApartmentFilter(1L);
		action.setBeginDate("2009/01/12");
		action.setEndDate("2019/03/25");

		assertEquals("Invalid action result", PersonSaveRegistrationAction.SUCCESS, action.execute());
		Apartment apartment = action.getPerson().getRegistrationApartment();
		assertNotNull("No registration set", apartment);
		assertEquals("Incorrect set apartment id", Long.valueOf(1L), apartment.getId());

		action.setApartmentFilter(3L);

		assertEquals("Invalid action result", PersonSaveRegistrationAction.SUCCESS, action.execute());
		apartment = action.getPerson().getRegistrationApartment();
		assertNotNull("No registration set 2", apartment);

		assertEquals("Incorrect update apartment id", Long.valueOf(3), apartment.getId());
	}
}
