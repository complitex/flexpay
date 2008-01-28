package org.flexpay.ab.service.imp;

import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.apache.commons.collections.ArrayStack;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class PersonServiceImpl implements PersonService {

	private PersonDao personDao;

	/**
	 * Setter for property 'personDao'.
	 *
	 * @param personDao Value to set for property 'personDao'.
	 */
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	/**
	 * List persons
	 *
	 * @param filters Stack of filters to apply
	 * @param pager   Paging filter
	 * @return List of persons
	 */
	public List<Person> findPersons(ArrayStack filters, Page pager) {
		return personDao.findObjects(pager, DomainObjectWithStatus.STATUS_ACTIVE);
	}

	/**
	 * Read person information
	 *
	 * @param id Person id
	 * @return Person instance, or <code>null</code> if not found
	 */
	public Person read(Long id) {
		return personDao.readFull(id);
	}
}
