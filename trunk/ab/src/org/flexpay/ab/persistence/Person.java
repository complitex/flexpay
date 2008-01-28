package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Person
 */
public class Person extends DomainObjectWithStatus {

	//     private Apartment apartment;
	//     private Set<Personact> personacts = new HashSet<Personact>(0);
	private Set<PersonAttribute> personAttributes = new HashSet<PersonAttribute>(0);
	private Set<PersonIdentity> personIdentities = Collections.emptySet();
//     private Set<ApartmentRelation> apartmentRelations = new HashSet<ApartmentRelation>(0);

	/**
	 * Constructs a new Person.
	 */
	public Person() {
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
