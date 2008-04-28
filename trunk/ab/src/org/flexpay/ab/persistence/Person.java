package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Person
 */
public class Person extends DomainObjectWithStatus {

	private Apartment apartment;
	private Set<PersonAttribute> personAttributes = Collections.emptySet();
	private Set<PersonIdentity> personIdentities = Collections.emptySet();

	/**
	 * Constructs a new Person.
	 */
	public Person() {
	}

	public Person(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'personAttributes'.
	 *
	 * @return Value for property 'personAttributes'.
	 */
	public Set<PersonAttribute> getPersonAttributes() {
		return personAttributes;
	}

	/**
	 * Setter for property 'personAttributes'.
	 *
	 * @param personAttributes Value to set for property 'personAttributes'.
	 */
	public void setPersonAttributes(Set<PersonAttribute> personAttributes) {
		this.personAttributes = personAttributes;
	}

	/**
	 * Getter for property 'personIdentities'.
	 *
	 * @return Value for property 'personIdentities'.
	 */
	public Set<PersonIdentity> getPersonIdentities() {
		return personIdentities;
	}

	/**
	 * Setter for property 'personIdentities'.
	 *
	 * @param personIdentities Value to set for property 'personIdentities'.
	 */
	public void setPersonIdentities(Set<PersonIdentity> personIdentities) {
		this.personIdentities = personIdentities;
	}

	/**
	 * Getter for property 'apartment'.
	 *
	 * @return Value for property 'apartment'.
	 */
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * Setter for property 'apartment'.
	 *
	 * @param apartment Value to set for property 'apartment'.
	 */
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	/**
	 * Find person default identity
	 *
	 * @return PersonIdentity
	 */
	public PersonIdentity getDefaultIdentity() {
		for (PersonIdentity identity : getPersonIdentities()) {
			if (identity.isDefault()) {
				return identity;
			}
		}

		return null;
	}

	/**
	 * Set person attribute, create, update or delete attribute operation.
	 *
	 * @param attribute PersonAttribute to set up
	 */
	public void setAttribute(PersonAttribute attribute) {
		boolean needRemove = false;
		for (PersonAttribute attr : personAttributes) {
			if (attr.getName().equals(attribute.getName()) && attr.getLang().equals(attribute.getLang())) {
				// new value is null, need to remove it
				if (attribute.getValue() == null) {
					needRemove = true;
					attribute = attr;
					break;
				}
				attr.setValue(attribute.getValue());
				return;
			}
		}

		if (needRemove) {
			personAttributes.remove(attribute);
		} else {
			if (personAttributes == Collections.EMPTY_SET) {
				personAttributes = new HashSet<PersonAttribute>();
			}

			personAttributes.add(attribute);
		}
	}

	public void addIdentity(PersonIdentity identity) {
		if (personIdentities == Collections.EMPTY_SET) {
			personIdentities = new HashSet<PersonIdentity>();
		}

		personIdentities.add(identity);
	}
}
