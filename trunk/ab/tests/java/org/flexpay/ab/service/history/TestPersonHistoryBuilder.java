package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

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
	@Ignore
	public void testBuildDiff() {

		Diff diff = historyBuilder.diff(new Person(), new Person());
		assertTrue("Diff of two empty persons is not empty", diff.isEmpty());
	}

	@Test
	@Ignore
	public void testBuildDiff2() {

		Diff diff = historyBuilder.diff(null, new Person());
		assertTrue("Diff of two empty persons is not empty", diff.isEmpty());
	}

	@Test
	@Ignore
	public void testBuildPersonDiff() {

		Person person = personService.readFull(PERSON);
		assertNotNull("Person not found: " + PERSON, person);

		@SuppressWarnings ({"ConstantConditions"})
		Diff diff = historyBuilder.diff(null, person);
		assertFalse("Invalid history builder", diff.isEmpty());
	}

	@Test
	@Ignore
	public void testPatchPerson() {

		Person person = personService.readFull(PERSON);
		assertNotNull("Person not found: " + PERSON, person);

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

	@Test
	public void testPatchPerson2() {

		Person person = personService.readFull(PERSON);
		assertNotNull("Person not found: " + PERSON, person);

		Diff diff = historyBuilder.diff(null, person);

		log.debug("\n============================= FIRST DIFF ==================================");

		Person patchedPerson = new Person();
		historyBuilder.patch(patchedPerson, diff);

		PersonIdentity fio = patchedPerson.getFIOIdentity();
		assertNotNull("Invalid patch, fio not set", fio);

		fio.setFirstName("TEST_PATCH_FN");
		fio.setLastName("TEST_PATCH_LN");
		fio.setMiddleName("TEST_PATCH_MN");

		log.debug("\n============================= FIRST PATCH ==================================");

		Diff diff2 = historyBuilder.diff(person, patchedPerson);
		log.debug("\n============================= SECOND DIFF ==================================");
		patchedPerson = new Person();

		historyBuilder.patch(patchedPerson, diff);
		log.debug("\n============================= SECOND PATCH ==================================");
		historyBuilder.patch(patchedPerson, diff2);
		log.debug("\n============================= THIRD PATCH ==================================");

		fio = patchedPerson.getFIOIdentity();
		assertNotNull("Invalid patch, fio not set", fio);
		assertEquals("Invalid second patch, FN", "TEST_PATCH_FN", fio.getFirstName());
		assertEquals("Invalid second patch, MN", "TEST_PATCH_MN", fio.getMiddleName());
		assertEquals("Invalid second patch, LN", "TEST_PATCH_LN", fio.getLastName());
	}

	@Before
	public void generateTypeHistory() {

		List<IdentityType> types = typeService.getEntities();
		for (IdentityType type : types) {
			typeHistoryGenerator.generateFor(type);
		}
	}
}
