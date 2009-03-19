package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.List;

public interface PersonService {

	/**
	 * List persons
	 *
	 * @param filters Stack of filters to apply
	 * @param pager   Paging filter
	 * @return List of persons
	 */
	@Secured (Roles.PERSON_READ)
	List<Person> findPersons(ArrayStack filters, Page<Person> pager);

	/**
	 * Read person information
	 *
	 * @param stub Person stub
	 * @return Person instance, or <code>null</code> if not found
	 */
	@Secured (Roles.PERSON_READ)
	@Nullable
	Person read(@NotNull Stub<Person> stub);

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	@Secured (Roles.PERSON_READ)
	@Nullable
	Stub<Person> findPersonStub(Person person);

	/**
	 * Find persons registered in apartment
	 *
	 * @param stub Apartment
	 * @return Persons list, empty if no persons found
	 */
	@Secured (Roles.PERSON_READ)
	@NotNull
	List<Person> findRegisteredPersons(@NotNull Stub<Apartment> stub);

	@Secured (Roles.PERSON_READ)
	List<Person> findByFIO(Page pager, String searchString);

	/**
	 * Create person
	 *
	 * @param person Person to save
	 * @return saved person back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.PERSON_ADD)
	Person create(@NotNull Person person) throws FlexPayExceptionContainer;

	/**
	 * Update person
	 *
	 * @param person Person to update
	 * @return Saved person back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured ({Roles.PERSON_ADD, Roles.PERSON_CHANGE})
	Person update(@NotNull Person person) throws FlexPayExceptionContainer;
}
