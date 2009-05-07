package org.flexpay.ab.persistence;

import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPerson extends AbSpringBeanAwareTestCase {

	@Autowired
	protected IdentityTypeService identityTypeService;

	@Test
	public void testAddIdentity() {

		Person person = new Person();
		PersonIdentity identity = newIdentity(person);
		identity.setDefault(false);

		assertEquals("First Identity not added", 1, person.getPersonIdentities().size());

		identity = PersonIdentity.newCopy(identity);

		assertEquals("Second Identity not added", 2, person.getPersonIdentities().size());

		identity.setDefault(true);
		assertNotNull("No default identity after add", person.getDefaultIdentity());
	}

	private PersonIdentity newIdentity(Person person) {

		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(identityTypeService.getType(IdentityType.TYPE_NAME_PASSPORT));
		identity.setDefault(true);
		identity.setBirthDate(ApplicationConfig.getPastInfinite());
		identity.setBeginDate(ApplicationConfig.getPastInfinite());
		identity.setEndDate(ApplicationConfig.getFutureInfinite());
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setOrganization("");
		identity.setFirstName("");
		identity.setMiddleName("");
		identity.setLastName("");
		identity.setPerson(person);

		person.addIdentity(identity);

		assertNotNull("No default identity", person.getDefaultIdentity());

		return identity;
	}
}
