package org.flexpay.ab.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.config.ApplicationConfig.*;

public class PersonProcessor extends AbstractProcessor<Person> {

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
	@NotNull
    @Override
	protected Person doCreateObject() throws Exception {

		Person person = new Person();
		newIdentity(person);

		return person;
	}

	private PersonIdentity newIdentity(Person person) {
		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(identityTypeService.findTypeByName(IdentityType.TYPE_NAME_PASSPORT));
		identity.setDefault(true);
		identity.setBirthDate(getPastInfinite());
		identity.setBeginDate(getPastInfinite());
		identity.setEndDate(getFutureInfinite());
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setOrganization("");
		identity.setFirstName("");
		identity.setMiddleName("");
		identity.setLastName("");
		identity.setPerson(person);

		person.addIdentity(identity);

		if (person.getDefaultIdentity() == null) {
			log.error("!!!!!!!!!!!!!!! No default identity just set");
		}

		return identity;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	@Nullable
    @Override
	protected Person readObject(@NotNull Stub<Person> stub) {
		return personService.readFull(stub);
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
    @Override
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRec record,
							Stub<DataSourceDescription> sd, CorrectionsService cs)
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

	private void setFirstName(@NotNull Person person, @NotNull HistoryRec record) {
		PersonIdentity identity = person.getDefaultIdentity();
		log.debug("Setting first name, person: {}", person);

		boolean valuesEquals = new EqualsBuilder()
				.append(record.getCurrentValue(), identity.getFirstName())
				.isEquals();
		if (valuesEquals) {
			// nothing to change
			log.debug("Values equals do not updating");
			return;
		}

		identity = copyIdentityIfNotNew(record, identity);

		if (StringUtils.isBlank(record.getCurrentValue())) {
			identity.setFirstName("");
		} else {
			identity.setFirstName(record.getCurrentValue());
		}

		log.debug("Set person first name");
	}

	private void setMiddleName(Person person, HistoryRec record) {
		PersonIdentity identity = person.getDefaultIdentity();
		log.debug("Setting middle name, person: {}", person);

		boolean valuesEquals = new EqualsBuilder()
				.append(record.getCurrentValue(), identity.getMiddleName())
				.isEquals();
		if (valuesEquals) {
			// nothing to change
			log.debug("Values equals do not updating");
			return;
		}

		identity = copyIdentityIfNotNew(record, identity);

		if (StringUtils.isBlank(record.getCurrentValue())) {
			identity.setMiddleName("");
		} else {
			identity.setMiddleName(record.getCurrentValue());
		}

		log.debug("Set person middle name");
	}

	private void setLastName(@NotNull Person person, @NotNull HistoryRec record) {
		PersonIdentity identity = person.getDefaultIdentity();
		log.debug("Setting last name, person: {}", person);

		boolean valuesEquals = new EqualsBuilder()
				.append(record.getCurrentValue(), identity.getLastName())
				.isEquals();
		if (valuesEquals && StringUtils.isNotBlank(record.getCurrentValue())) {
			// nothing to change
			log.debug("Values equals do not updating");
			return;
		}
		identity = copyIdentityIfNotNew(record, identity);


		if (StringUtils.isBlank(record.getCurrentValue())) {
			identity.setLastName("");
		} else {
			identity.setLastName(record.getCurrentValue());
		}

		log.debug("Set person last name");
	}

	private PersonIdentity copyIdentityIfNotNew(@NotNull HistoryRec record, @NotNull PersonIdentity identity) {
		// old identity is not new, create new one
		if (identity.isNotNew()) {
			identity.setEndDate(record.getRecordDate());
			identity.setDefault(false);

			identity = PersonIdentity.newCopy(identity);
			identity.setDefault(true);
		}
		return identity;
	}

	private void setINN(@NotNull Person person, @Nullable String value) {
		PersonAttribute inn = new PersonAttribute();
		inn.setLang(getDefaultLanguage());
		inn.setValue(value == null ? "" : value);
		inn.setName("ab.person.attribute.inn");
		inn.setTranslatable(person);

		log.debug("Setting person INN: {}", value);

		person.setAttribute(inn);
	}

	private void setResidenceApartment(Person person, String apartmentId, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws FlexPayException {

		Stub<Apartment> stub = cs.findCorrection(apartmentId, Apartment.class, sd);
		if (stub == null) {
			log.error("Cannot set residence apartment for person, correction not found: {}", apartmentId);
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
    @Override
	protected Stub<Person> findPersistentObject(Person object, Stub<DataSourceDescription> sd, CorrectionsService cs) {
		// there is not enough info to identify each person, will create new each time
		return null;
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
    @Override
	protected void doSaveObject(Person object, String externalId) throws Exception {
		PersonIdentity identity = object.getDefaultIdentity();
		if (identity != null) {
			if (identity.isFIOEmpty()) {
				log.warn("Person with empty FIO, settion last name to ObjectId: {}", externalId);
				identity.setLastName(externalId);
			}
			if (StringUtils.isBlank(identity.getLastName())) {
				log.error("Do not saving person, empty last name: {}", object);
				return;
			}

			if (object.isNew()) {
				personService.create(object);
			} else {
				personService.update(object);
			}
		}
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
