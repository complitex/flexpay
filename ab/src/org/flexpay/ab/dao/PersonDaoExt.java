package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.JpaSetDao;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;

import java.util.List;

public interface PersonDaoExt extends JpaSetDao {

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	Stub<Person> findPersonStub(Person person);

	List<Person> listPersonsWithIdentities(FetchRange range);

	List<Person> listPersonsWithRegistrations(FetchRange range);

	void deletePerson(Person person);

}
