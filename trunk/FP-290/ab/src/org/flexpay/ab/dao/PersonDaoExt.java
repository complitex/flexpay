package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.Stub;

public interface PersonDaoExt {

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	Stub<Person> findPersonStub(Person person);
}
