package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import static org.apache.commons.lang.StringUtils.isBlank;
import org.flexpay.ab.dao.PersonAttributeDao;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.dao.PersonRegistrationDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class PersonServiceImpl implements PersonService {

	private PersonDao personDao;
	private PersonDaoExt personDaoExt;
	private PersonAttributeDao personAttributeDao;
	private PersonRegistrationDao personRegistrationDao;

	private IdentityTypeService identityTypeService;

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public void setPersonDaoExt(PersonDaoExt personDaoExt) {
		this.personDaoExt = personDaoExt;
	}

	public void setPersonAttributeDao(PersonAttributeDao personAttributeDao) {
		this.personAttributeDao = personAttributeDao;
	}

	public void setPersonRegistrationDao(PersonRegistrationDao personRegistrationDao) {
		this.personRegistrationDao = personRegistrationDao;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
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
	@Nullable
	public Person read(@NotNull Stub<Person> stub) {

		Person persistent = personDao.readFull(stub.getId());
		if (persistent != null) {
			persistent.setPersonAttributes(personAttributeDao.listAttributes(stub.getId()));
			persistent.setPersonRegistrations(personRegistrationDao.listRegistrations(stub.getId()));

			// setup identity types
			for (PersonIdentity identity : persistent.getPersonIdentities()) {
				IdentityType type = identityTypeService.read(identity.getIdentityType().getId());
				identity.setIdentityType(type);
			}
		}

		return persistent;
	}

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	@Nullable
	public Stub<Person> findPersonStub(Person person) {
		return personDaoExt.findPersonStub(person);
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
	@Transactional (readOnly = false)
	public void save(Person person) throws FlexPayExceptionContainer {
		validate(person);
		if (person.isNew()) {
			person.setId(null);
			personDao.create(person);
		} else {
			personDao.update(person);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
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

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(PersonIdentity identity) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (identity.getIdentityType() == null) {
			container.addException(new FlexPayException("No type", "error.ab.person.identity.no_type"));
		}

//		if (isBlank(identity.getFirstName())) {
//			container.addException(new FlexPayException("No first name", "error.ab.person.identity.no_first_name"));
//		}
		if (isBlank(identity.getLastName())) {
			container.addException(new FlexPayException("No last name", "error.ab.person.identity.no_last_name"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}
}
