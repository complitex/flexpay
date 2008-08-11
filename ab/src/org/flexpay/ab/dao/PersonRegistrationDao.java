package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.GenericDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PersonRegistrationDao extends GenericDao<Long, PersonRegistration> {

	/**
	 * List active registrations
	 *
	 * @param personId Person identifier
	 * @return List of registrations
	 */
	@NotNull
	List<PersonRegistration> listRegistrations(@NotNull Long personId);

	/**
	 * List all person registrations
	 *
	 * @param personId Person identifier
	 * @return List of registrations
	 */
	@NotNull
	List<PersonRegistration> listRegistrationHistory(@NotNull Long personId);

	/**
	 * List persons registered in apartment
	 *
	 * @param apartmentId Apartment key
	 * @return List of persons
	 */
	@NotNull
	List<Person> listRegistrants(@NotNull Long apartmentId);
}
