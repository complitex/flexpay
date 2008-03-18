package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Person;

public interface PersonDaoExt {

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	Person findPersonStub(Person person);
}
