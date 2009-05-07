package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestPersonHistoryBuilder extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonService personService;
	@Autowired
	private IdentityTypeService typeService;
	@Autowired
	private IdentityTypeHistoryGenerator typeHistoryGenerator;
	@Autowired
	private PersonHistoryBuilder historyBuilder;

	public static final Stub<Person> PERSON = new Stub<Person>(1L);

	@Test
	public void testBuildDiff() {

		Diff diff = historyBuilder.diff(new Person(), new Person());
		assertTrue("Diff of two empty persons is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff2() {

		Diff diff = historyBuilder.diff(null, new Person());
		assertTrue("Diff of two empty persons is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildPersonDiff() {

		Person person = personService.read(PERSON);
		assertNotNull("Person not found: " + PERSON, person);

		@SuppressWarnings ({"ConstantConditions"})
		Diff diff = historyBuilder.diff(null, person);
		assertFalse("Invalid history builder", diff.isEmpty());
	}

	@Test
	public void testPatchPerson() {

		Person person = personService.read(PERSON);
		assertNotNull("Person not found: " + PERSON, person);

		@SuppressWarnings ({"ConstantConditions"})
		Diff diff = historyBuilder.diff(null, person);
		assertFalse("Invalid history builder", diff.isEmpty());

		Person patchedPerson = new Person();
		historyBuilder.patch(patchedPerson, diff);

		assertEquals("Invalid person identities patch",
				person.getPersonIdentities().size(), patchedPerson.getPersonIdentities().size());

		PersonIdentity fio = patchedPerson.getFIOIdentity();
		assertNotNull("Invalid patch, fio not set", fio);
		assertEquals("Invalid first name patch", "Михаил", fio.getFirstName());
		assertEquals("Invalid middle name patch", "Анатольевич", fio.getMiddleName());
		assertEquals("Invalid last name patch", "Федько", fio.getLastName());
	}

	@Before
	public void generateTypeHistory() {

		List<IdentityType> types = typeService.getEntities();
		for (IdentityType type : types) {
			typeHistoryGenerator.generateFor(type);
		}
	}
}
