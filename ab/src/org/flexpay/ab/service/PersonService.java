package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

public interface PersonService {

	/**
	 * Read person
	 *
	 * @param personStub Person stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.PERSON_READ)
	@Nullable
	Person readFull(@NotNull Stub<Person> personStub);

	/**
	 * Read persons collection by theirs ids
	 *
 	 * @param personIds Person ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found persons
	 */
	@Secured ({Roles.PERSON_READ})
	@NotNull
	List<Person> readFull(@NotNull Collection<Long> personIds, boolean preserveOrder);


	/**
	 * Disable persons
	 *
	 * @param personIds IDs of persons to disable
	 */
	@Secured (Roles.PERSON_DELETE)
	void disable(@NotNull Collection<Long> personIds);

	/**
	 * Create person
	 *
	 * @param person Person to save
	 * @return Saved instance of person
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.PERSON_ADD)
	@NotNull
	Person create(@NotNull Person person) throws FlexPayExceptionContainer;

	/**
	 * Update or create person
	 *
	 * @param person Person to update
	 * @return Saved instance of person
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.PERSON_CHANGE)
	@NotNull
	Person update(@NotNull Person person) throws FlexPayExceptionContainer;

	/**
	 * List persons
	 *
	 * @param filters Stack of filters to apply
	 * @param pager   Paging filter
	 * @return List of persons
	 */
	@Secured (Roles.PERSON_READ)
	@NotNull
	List<Person> find(@Nullable ArrayStack filters, Page<Person> pager);

	/**
	 * List persons
	 *
	 * @param apartmentStub Apartment stub
	 * @param pager Paging filter
	 * @return List of persons
	 */
	@Secured (Roles.PERSON_READ)
	@NotNull
	List<Person> find(@NotNull Stub<Apartment> apartmentStub, Page<Person> pager);

	/**
	 * List persons
	 *
	 * @param searchString searching string
	 * @param pager Paging filter
	 * @return List of persons
	 */
	@Secured (Roles.PERSON_READ)
	@NotNull
	List<Person> findByFIO(@NotNull String searchString, Page<Person> pager);

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	@Secured (Roles.PERSON_READ)
	@Nullable
	Stub<Person> findPersonStub(@NotNull Person person);

	/**
	 * List persons with identities
	 *
	 * @param range FetchRange 
	 * @return List of persons
	 */
	@Secured (Roles.PERSON_READ)
	@NotNull
	List<Person> listPersonsWithIdentities(@NotNull FetchRange range);

	/**
	 * List persons with registrations
	 *
	 * @param range FetchRange
	 * @return List of persons
	 */
	@Secured (Roles.PERSON_READ)
	@NotNull
	List<Person> listPersonsWithRegistrations(@NotNull FetchRange range);

	/**
	 * Find persons ids in range
	 *
	 * @param range Fetch range
	 * @return List of persons
	 */
	@Secured (Roles.PERSON_READ)
	@NotNull
	List<Person> findSimple(FetchRange range);
}
