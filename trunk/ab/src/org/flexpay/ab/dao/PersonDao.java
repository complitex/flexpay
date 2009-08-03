package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PersonDao extends GenericDao<Person, Long>{

	/**
	 * Get page of children for parent
	 *
	 * @param pager	Number of children filter
	 * @param status   Object status to retrive
	 * @return List of children
	 */
	List<Person> findObjects(Page pager, int status);

	@NotNull
	List<Person> findPersonsInApartment(@NotNull Long apartmentId, int status, Page<Person> pager);

	List<Person> findByFIO(Page pager, String searchString);
}
