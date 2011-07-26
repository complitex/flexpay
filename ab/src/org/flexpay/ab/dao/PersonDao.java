package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface PersonDao extends GenericDao<Person, Long> {

	/**
	 * Get page of children for parent
	 *
	 * @param pager	Number of children filter
	 * @param status   Object status to retrive
	 * @return List of children
	 */
	@NotNull
	List<Person> findObjects(int status, Page<Person> pager);

	@NotNull
	List<Person> findPersonsInApartment(@NotNull Long apartmentId, int status, Page<Person> pager);

	@NotNull
	List<Person> findByFIO(@NotNull String searchString, Page<Person> pager);

	List<Person> findPersonsWithAttributes(Collection<Long> personIds);

	List<Person> findPersonsWithRegistrations(Collection<Long> personIds);

	List<Person> findSimple(FetchRange range);
}

