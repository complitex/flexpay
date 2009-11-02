package org.flexpay.ab.service;

import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonDao extends AbSpringBeanAwareTestCase {

	@Autowired
	protected IdentityTypeService identityTypeService;
	@Autowired
	protected PersonDaoExt personDaoExt;

	@Test
	public void testFindPersonStub() throws Throwable {

/*
		PersonIdentity identity = new PersonIdentity();
		identity.setFirstName("test");
		identity.setMiddleName("test");
		identity.setLastName("test");
		identity.setDefault(true);
		identity.setIdentityType(identityTypeService.findTypeByName(IdentityType.TYPE_PASSPORT));

		Person person = new Person();
		Set<PersonIdentity> identitySet = new HashSet<PersonIdentity>();
		identitySet.add(identity);
		person.setPersonIdentities(identitySet);

		Stub<Person> stub = personDaoExt.findPersonStub(person);

		assertNull("Unknown identity found", stub);
*/
	}
}
