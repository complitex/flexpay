package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

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
	@Nullable
	Person read(@NotNull Stub<Person> stub);

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	@Nullable
	Stub<Person> findPersonStub(Person person);

	/**
	 * Find persons registered in apartment
	 *
	 * @param stub Apartment
	 * @return Persons list, empty if no persons found
	 */
	@NotNull
	List<Person> findRegisteredPersons(@NotNull Stub<Apartment> stub);

	List<Person> findByFIO(Page pager, String searchString);

	/**
	 * Create or update person
	 *
	 * @param person Person to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(Person person) throws FlexPayExceptionContainer;
}
