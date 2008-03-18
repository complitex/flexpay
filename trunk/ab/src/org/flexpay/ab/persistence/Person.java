package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Person
 */
public class Person extends DomainObjectWithStatus {

	private Apartment apartment;
	private Set<PersonAttribute> personAttributes = new HashSet<PersonAttribute>(0);
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
}
