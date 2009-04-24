package org.flexpay.ab.service;

import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class TestPersonDao extends AbSpringBeanAwareTestCase {

	@Autowired
	protected IdentityTypeService identityTypeService;
	@Autowired
	protected PersonDaoExt personDaoExt;

	@Test
	public void testFindPersonStub() throws Throwable {

		PersonIdentity identity = new PersonIdentity();
		identity.setFirstName("test");
		identity.setMiddleName("test");
		identity.setLastName("test");
		identity.setDefault(true);
		identity.setIdentityType(identityTypeService.getType(IdentityType.TYPE_PASSPORT));

		Person person = new Person();
		Set<PersonIdentity> identitySet = new HashSet<PersonIdentity>();
		identitySet.add(identity);
		person.setPersonIdentities(identitySet);

		Stub<Person> stub = personDaoExt.findPersonStub(person);

		assertNull("Unknown identity found", stub);
	}
}
