package org.flexpay.ab.action.person;

import org.flexpay.ab.actions.person.PersonSaveRegistrationAction;
import org.flexpay.ab.actions.buildings.BuildingDeleteAction;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonSaveRegistrationAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonSaveRegistrationAction action;

	@Test
	public void testSaveRegistration() throws Exception {

		Person person = new Person(1L);
		action.setPerson(person);
		action.setApartmentFilter(1L);
		action.setBeginDate("2009/01/12");
		action.setEndDate("2019/03/25");

		assertEquals("Invalid action result", BuildingDeleteAction.SUCCESS, action.execute());
		assertEquals("Incorrect set apartment id", 1, action.getPerson().getRegistrationApartment().getId().longValue());

		action.setApartmentFilter(3L);

		assertEquals("Invalid action result", BuildingDeleteAction.SUCCESS, action.execute());
		assertEquals("Incorrect update apartment id", 3, action.getPerson().getRegistrationApartment().getId().longValue());

	}

}
