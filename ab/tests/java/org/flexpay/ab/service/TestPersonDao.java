package org.flexpay.ab.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;

import java.util.Set;
import java.util.HashSet;

public class TestPersonDao extends SpringBeanAwareTestCase {

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	protected void runTest() throws Throwable {
		testFindPersonStub();
	}

	public void testFindPersonStub() throws Throwable {
		IdentityTypeService identityTypeService = (IdentityTypeService)
				applicationContext.getBean("identityTypeService");

		PersonIdentity identity = new PersonIdentity();
		identity.setFirstName("test");
		identity.setMiddleName("test");
		identity.setLastName("test");
		identity.setDefault(true);
		identity.setIdentityType(identityTypeService.getType(IdentityType.TYPE_PASSPORT));

		PersonDaoExt personDaoExt = (PersonDaoExt)
				applicationContext.getBean("personDaoExt");

		Person person = new Person();
		Set<PersonIdentity> identitySet = new HashSet<PersonIdentity>();
		identitySet.add(identity);
		person.setPersonIdentities(identitySet);

		Person stub = personDaoExt.findPersonStub(person);

		assertNull("Unknown identity found", stub);
	}
}
