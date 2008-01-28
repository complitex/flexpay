package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.Page;

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
	 * @param id Person id
	 * @return Person instance, or <code>null</code> if not found
	 */
	Person read(Long id);
}
