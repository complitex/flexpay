package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import static org.apache.commons.lang.StringUtils.isBlank;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PersonServiceImpl implements PersonService {

	private PersonDao personDao;
	private PersonDaoExt personDaoExt;

	/**
	 * Setter for property 'personDao'.
	 *
	 * @param personDao Value to set for property 'personDao'.
	 */
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	/**
	 * Setter for property 'personDaoExt'.
	 *
	 * @param personDaoExt Value to set for property 'personDaoExt'.
	 */
	public void setPersonDaoExt(PersonDaoExt personDaoExt) {
		this.personDaoExt = personDaoExt;
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
	 * @param stub Person stub
	 * @return Person instance, or <code>null</code> if not found
	 */
	public Person read(Person stub) {
		if (stub.isNotNew()) {
			return personDao.readFull(stub.getId());
		}

		return null;
	}

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	public Person findPersonStub(Person person) {
		return personDaoExt.findPersonStub(person);
	}

	/**
	 * Update person
	 *
	 * @param person Person
	 */
	@Transactional(readOnly = false)
	public void update(Person person) {
		personDao.update(person);
	}

	public List<Person> findByFIO(Page pager, String searchString) {
		return personDao.findByFIO(pager, searchString);
	}

	/**
	 * Create or update person
	 *
	 * @param person Person to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional(readOnly = false)
	public void save(Person person) throws FlexPayExceptionContainer {
		validate(person);
		if (person.isNew()) {
			person.setId(null);
			personDao.create(person);
		} else {
			personDao.update(person);
		}
	}

	@SuppressWarnings({"ThrowableInstanceNeverThrown"})
	private void validate(Person person) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (person.getPersonIdentities().isEmpty()) {
			container.addException(new FlexPayException("No identities",
					"error.ab.person.no_identities"));
		}

		for (PersonIdentity identity : person.getPersonIdentities()) {
			try {
				validate(identity);
			} catch (FlexPayExceptionContainer e) {
				container.addExceptions(e);
			}
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	@SuppressWarnings({"ThrowableInstanceNeverThrown"})
	private void validate(PersonIdentity identity) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (identity.getIdentityType() == null) {
			container.addException(new FlexPayException("No type", "error.ab.person.identity.no_type"));
		}

		if (isBlank(identity.getFirstName())) {
			container.addException(new FlexPayException("No first name", "error.ab.person.identity.no_first_name"));
		}
		if (isBlank(identity.getLastName())) {
			container.addException(new FlexPayException("No last name", "error.ab.person.identity.no_last_name"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}
}
