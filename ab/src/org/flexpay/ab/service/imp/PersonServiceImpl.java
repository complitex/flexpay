package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import static org.apache.commons.lang.StringUtils.isBlank;
import org.flexpay.ab.dao.PersonAttributeDao;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.dao.PersonRegistrationDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class PersonServiceImpl implements PersonService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private PersonDao personDao;
	private PersonDaoExt personDaoExt;
	private PersonAttributeDao personAttributeDao;
	private PersonRegistrationDao personRegistrationDao;

	private IdentityTypeService identityTypeService;

	private SessionUtils sessionUtils;
	private ModificationListener<Person> modificationListener;

	/**
	 * List persons
	 *
	 * @param filters Stack of filters to apply
	 * @param pager   Paging filter
	 * @return List of persons
	 */
	public List<Person> findPersons(ArrayStack filters, Page<Person> pager) {
		return personDao.findObjects(pager, DomainObjectWithStatus.STATUS_ACTIVE);
	}

	/**
	 * List persons with identities
	 *
	 * @param range FetchRange
	 * @return List of persons
	 */
	public List<Person> listPersonsWithIdentities(FetchRange range) {
		return personDaoExt.listPersonsWithIdentities(range);
	}

	/**
	 * List persons with registrations
	 *
	 * @param range FetchRange
	 * @return List of persons
	 */
	public List<Person> listPersonsWithRegistrations(FetchRange range) {
		return personDaoExt.listPersonsWithRegistrations(range);
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
			log.debug("READ ATTRIBUTES");
			List<PersonAttribute> attributes = personAttributeDao.listAttributes(stub.getId());
			persistent.setPersonAttributes(attributes);
			sessionUtils.evict(attributes);

			log.debug("READ REGISTRATIONS");
			List<PersonRegistration> registrations = personRegistrationDao.listRegistrations(stub.getId());
			persistent.setPersonRegistrations(registrations);
			sessionUtils.evict(registrations);

			log.debug("SETTING IDENTITY TYPES");
			// setup identity types
			for (PersonIdentity identity : persistent.getPersonIdentities()) {
				IdentityType type = identityTypeService.read(identity.getIdentityTypeStub());
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

	public List<Person> findByFIO(Page<Person> pager, String searchString) {
		return personDao.findByFIO(pager, searchString);
	}

	/**
	 * Create or update person
	 *
	 * @param person Person to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	public Person update(@NotNull Person person) throws FlexPayExceptionContainer {

		validate(person);

		log.debug("ReadFULL UPDATE");
		Person old = read(stub(person));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + person));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, person);

		log.debug("UDATING");
		personDao.update(person);

		log.debug("UDATED");

		return person;
	}

    /**
     * Disable persons
     *
     * @param objectIds Person identifiers
     */
    @Transactional (readOnly = false)
    public void disable(@NotNull Set<Long> objectIds) {
        for (Long id : objectIds) {
			Person person = personDao.read(id);
			if (person != null) {
				person.disable();
				personDao.update(person);

				modificationListener.onDelete(person);
			}
		}
    }

    /**
	 * Create person
	 *
	 * @param person Person to save
	 * @return saved person back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	public Person create(@NotNull Person person) throws FlexPayExceptionContainer {

		validate(person);
		person.setId(null);
		personDao.create(person);

		modificationListener.onCreate(person);

		return person;

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

		if (isBlank(identity.getLastName())) {
			container.addException(new FlexPayException("No last name", "error.ab.person.identity.no_last_name"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Find persons registered in apartment
	 *
	 * @param stub Apartment
	 * @return Persons list, empty if no persons found
	 */
	@NotNull
	public List<Person> findRegisteredPersons(@NotNull Stub<Apartment> stub) {
		return personRegistrationDao.listRegistrants(stub.getId());
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<Person> modificationListener) {
		this.modificationListener = modificationListener;
	}

	@Required
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	@Required
	public void setPersonDaoExt(PersonDaoExt personDaoExt) {
		this.personDaoExt = personDaoExt;
	}

	@Required
	public void setPersonAttributeDao(PersonAttributeDao personAttributeDao) {
		this.personAttributeDao = personAttributeDao;
	}

	@Required
	public void setPersonRegistrationDao(PersonRegistrationDao personRegistrationDao) {
		this.personRegistrationDao = personRegistrationDao;
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
