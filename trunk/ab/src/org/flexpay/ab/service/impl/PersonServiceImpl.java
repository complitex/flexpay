package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.PersonAttributeDao;
import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.dao.PersonRegistrationDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.AttributeCopier;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.flexpay.common.persistence.Stub.stub;

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
	 * Read person
	 *
	 * @param personStub Person stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Person readFull(@NotNull Stub<Person> personStub) {

		Person person = personDao.readFull(personStub.getId());
		if (person == null) {
			return null;
		}

		log.debug("Read attributes for person");
		List<PersonAttribute> attributes = personAttributeDao.listAttributes(personStub.getId());
		person.setPersonAttributes(attributes);
		sessionUtils.evict(attributes);

		log.debug("Read registrations for person");
		List<PersonRegistration> registrations = personRegistrationDao.listRegistrations(personStub.getId());
		person.setPersonRegistrations(registrations);
		sessionUtils.evict(registrations);

		log.debug("Setting identity types for person");
		// setup identity types
		for (PersonIdentity identity : person.getPersonIdentities()) {
			IdentityType type = identityTypeService.readFull(identity.getIdentityTypeStub());
			identity.setIdentityType(type);
		}

		return person;
	}

	@NotNull
	@Override
	public List<Person> readFull(@NotNull Collection<Long> personIds, boolean preserveOrder) {

		List<Person> persons = personDao.readFullCollection(personIds, preserveOrder);

		// set person attributes
		List<Person> personAttributes = personDao.findPersonsWithAttributes(personIds);
		CollectionUtils.copyAttributes(personAttributes, persons, new AttributeCopier<Person>() {
			@Override
			public void copy(Person from, Person to) {
				to.setPersonAttributes(from.getPersonAttributes());
			}
		});

		// set person registrations
		List<Person> personRegistrations = personDao.findPersonsWithRegistrations(personIds);
		CollectionUtils.copyAttributes(personRegistrations, persons, new AttributeCopier<Person>() {
			@Override
			public void copy(Person from, Person to) {
				to.setPersonRegistrations(from.getPersonRegistrations());
			}
		});

		return persons;
	}

	/**
	 * Disable persons
	 *
	 * @param personIds IDs of persons to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> personIds) {
		for (Long id : personIds) {
			if (id == null) {
				log.warn("Null id in collection of person ids for disable");
				continue;
			}
			Person person = personDao.read(id);
			if (person == null) {
				log.warn("Can't get person with id {} from DB", id);
				continue;
			}
			person.disable();
			personDao.update(person);

			modificationListener.onDelete(person);
			log.debug("Person disabled: {}", person);
		}
	}

	/**
	 * Create person
	 *
	 * @param person Person to save
	 * @return Saved instance of person
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Person create(@NotNull Person person) throws FlexPayExceptionContainer {

		validate(person);
		person.setId(null);
		personDao.create(person);

		modificationListener.onCreate(person);

		return person;

	}

	/**
	 * Update or create person
	 *
	 * @param person Person to update
	 * @return Saved instance of person
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Person update(@NotNull Person person) throws FlexPayExceptionContainer {

		validate(person);

		Person old = readFull(stub(person));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No person found to update " + person));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, person);

		personDao.update(person);

		return person;
	}

	/**
	 * Validate person before save
	 *
	 * @param person Person object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Person person) throws FlexPayExceptionContainer {
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

	/**
	 * Validate person identity before save
	 *
	 * @param identity Person identity object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull PersonIdentity identity) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (identity.getIdentityType() == null) {
			container.addException(new FlexPayException("No type", "ab.error.person.identity.no_type"));
		}

		if (isBlank(identity.getLastName())) {
			container.addException(new FlexPayException("No last name", "ab.error.person.identity.no_last_name"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * List persons
	 *
	 * @param filters Stack of filters to apply
	 * @param pager   Paging filter
	 * @return List of persons
	 */
	@NotNull
	@Override
	public List<Person> find(@Nullable ArrayStack filters, Page<Person> pager) {
		return personDao.findObjects(DomainObjectWithStatus.STATUS_ACTIVE, pager);
	}

	/**
	 * List persons
	 *
	 * @param apartmentStub Apartment stub
	 * @param pager Paging filter
	 * @return List of persons
	 */
	@NotNull
	@Override
	public List<Person> find(@NotNull Stub<Apartment> apartmentStub, Page<Person> pager) {
		return personDao.findPersonsInApartment(apartmentStub.getId(), DomainObjectWithStatus.STATUS_ACTIVE, pager);
	}

	/**
	 * List persons
	 *
	 * @param searchString searching string
	 * @param pager Paging filter
	 * @return List of persons
	 */
	@NotNull
	@Override
	public List<Person> findByFIO(@NotNull String searchString, Page<Person> pager) {
		return personDao.findByFIO(searchString, pager);
	}

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persitent person matches specified identity
	 */
	@Nullable
	@Override
	public Stub<Person> findPersonStub(@NotNull Person person) {
		return personDaoExt.findPersonStub(person);
	}

	/**
	 * List persons with identities
	 *
	 * @param range FetchRange
	 * @return List of persons
	 */
	@NotNull
	@Override
	public List<Person> listPersonsWithIdentities(@NotNull FetchRange range) {
		return personDaoExt.listPersonsWithIdentities(range);
	}

	/**
	 * List persons with registrations
	 *
	 * @param range FetchRange
	 * @return List of persons
	 */
	@NotNull
	@Override
	public List<Person> listPersonsWithRegistrations(@NotNull FetchRange range) {
		return personDaoExt.listPersonsWithRegistrations(range);
	}

	@NotNull
	@Override
	public List<Person> findSimple(FetchRange range) {
		return personDao.findSimple(range);
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
