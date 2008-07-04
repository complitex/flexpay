package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;

import java.util.List;

public interface PersonService {

	/**
	 * List persons
	 *
	 * @param filters Stack of filters to apply
	 * @param pager   Paging filter
	 * @return List of persons
	 */
	List<Person> findPersons(ArrayStack filters, Page pager);

	/**
	 * Read person information
	 *
	 * @param stub Person stub
	 * @return Person instance, or <code>null</code> if not found
	 */
	Person read(Person stub);

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	Person findPersonStub(Person person);

	/**
	 * Update person
	 *
	 * @param person Person
	 */
	void update(Person person);

	List<Person> findByFIO(Page pager, String searchString);

	/**
	 * Create or update person
	 *
	 * @param person Person to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(Person person) throws FlexPayExceptionContainer;
}
