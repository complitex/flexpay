package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.PersonDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.apache.commons.lang.StringUtils;

public class PersonProcessor extends AbstractProcessor<Person> {

	private PersonDao personDao;
	private PersonService personService;
	private IdentityTypeService identityTypeService;

	public PersonProcessor() {
		super(Person.class);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	protected Person doCreateObject() throws Exception {

		Person person = new Person();
		newIdentity(person);

		return person;
	}

	private PersonIdentity newIdentity(Person person) {
		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(identityTypeService.getType(IdentityType.TYPE_NAME_PASSPORT));
		identity.setDefault(true);
		identity.setBirthDate(ApplicationConfig.getPastInfinite());
		identity.setBeginDate(ApplicationConfig.getPastInfinite());
		identity.setEndDate(ApplicationConfig.getFutureInfinite());
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setOrganization("");
		identity.setFirstName("");
		identity.setMiddleName("");
		identity.setLastName("");
		identity.setPerson(person);

		person.addIdentity(identity);
		return identity;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected Person readObject(Stub<Person> stub) {
		return personDao.readFull(stub.getId());
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param object DomainObject to set properties on
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @throws Exception if failure occurs
	 */
	public void setProperty(DomainObject object, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
		Person person = (Person) object;
		switch (record.getFieldType()) {
			case FirstName:
				setFirstName(person, record);
				break;
			case MiddleName:
				setMiddleName(person, record);
				break;
			case LastName:
				setLastName(person, record);
				break;
			case INN:
				setINN(person, record.getCurrentValue());
				break;
			case ResidenceApartmentId:
				setResidenceApartment(person, record.getCurrentValue(), sd, cs);
				break;
		}
	}

	private void setFirstName(Person person, HistoryRecord record) {
		PersonIdentity identity = person.getDefaultIdentity();
		boolean newIsBlank = StringUtils.isBlank(record.getCurrentValue());
		boolean oldIsBlank = StringUtils.isBlank(identity.getFirstName());
		if (newIsBlank && oldIsBlank) {
			// nothing to change
			log.debug("First name is blank, do not updating");
			return;
		}
		if (newIsBlank) {
			// new name is blank, create new identity
			identity.setEndDate(record.getRecordDate());
			identity.setDefault(false);

			newIdentity(person);

			log.debug("New first name is blank");
			return;
		}
		if (!oldIsBlank && !record.getCurrentValue().equals(identity.getFirstName())) {
			// new name differs from the old
			identity.setEndDate(record.getRecordDate());
			identity.setDefault(false);

			identity = newIdentity(person);

			log.debug("Person first name changed, creating a new identity");
		}

		identity.setFirstName(record.getCurrentValue());

		log.debug("Setting person first name");
	}

	private void setMiddleName(Person person, HistoryRecord record) {
		PersonIdentity identity = person.getDefaultIdentity();
		boolean newIsBlank = StringUtils.isBlank(record.getCurrentValue());
		boolean oldIsBlank = StringUtils.isBlank(identity.getMiddleName());
		if (newIsBlank && oldIsBlank) {
			// nothing to change
			log.debug("Middle name is blank, do not updating");
			return;
		}
		if (newIsBlank) {
			// new name is blank, create new identity
			identity.setEndDate(record.getRecordDate());
			identity.setDefault(false);

			newIdentity(person);

			log.debug("New middle name is blank");
			return;
		}
		if (!oldIsBlank && !record.getCurrentValue().equals(identity.getMiddleName())) {
			// new name differs from the old
			identity.setEndDate(record.getRecordDate());
			identity.setDefault(false);

			identity = newIdentity(person);

			log.debug("Person middle name changed, creating a new identity");
		}

		identity.setMiddleName(record.getCurrentValue());

		log.debug("Setting person middle name");
	}

	private void setLastName(Person person, HistoryRecord record) {
		PersonIdentity identity = person.getDefaultIdentity();
		boolean newIsBlank = StringUtils.isBlank(record.getCurrentValue());
		boolean oldIsBlank = StringUtils.isBlank(identity.getLastName());
		if (newIsBlank && oldIsBlank) {
			// nothing to change
			log.debug("Last name is blank, do not updating");
			return;
		}
		if (newIsBlank) {
			// new name is blank, create new identity
			identity.setEndDate(record.getRecordDate());
			identity.setDefault(false);

			newIdentity(person);

			log.debug("New last name is blank");
			return;
		}
		if (!oldIsBlank && !record.getCurrentValue().equals(identity.getLastName())) {
			// new name differs from the old
			identity.setEndDate(record.getRecordDate());
			identity.setDefault(false);

			identity = newIdentity(person);

			log.debug("Person last name changed, creating a new identity");
		}

		identity.setLastName(record.getCurrentValue());

		log.debug("Setting person last name");
	}

	private void setINN(Person person, String value) throws FlexPayException {
		PersonAttribute inn = new PersonAttribute();
		inn.setLang(ApplicationConfig.getDefaultLanguage());
		inn.setValue(value);
		inn.setName("ab.person.attribute.inn");
		inn.setTranslatable(person);

		if (log.isDebugEnabled()) {
			log.debug("Setting person INN: " + value);
		}

		person.setAttribute(inn);
	}

	private void setResidenceApartment(Person person, String apartmentId, DataSourceDescription sd, CorrectionsService cs) throws FlexPayException {
		Stub<Apartment> stub = cs.findCorrection(apartmentId, Apartment.class, sd);
		if (stub == null) {
			log.error("Cannot set residence apartment for person, correction not found: " + apartmentId);
			return;
		}

		person.setRegistrationApartment(new Apartment(stub));
	}

	/**
	 * Try to find persistent object by set properties
	 *
	 * @param object DomainObject
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @return Persistent object stub if exists, or <code>null</code> otherwise
	 */
	protected Stub<Person> findPersistentObject(Person object, DataSourceDescription sd, CorrectionsService cs) {
		return personService.findPersonStub(object);
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	protected void doSaveObject(Person object) {
		if (object.getId() == null) {
			personDao.create(object);
		} else {
			personDao.update(object);
		}
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
