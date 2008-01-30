package org.flexpay.ab.persistence;

import org.flexpay.ab.persistence.temp.ApartmentAttribute;
import org.flexpay.ab.persistence.temp.ApartmentRelation;
import org.flexpay.ab.persistence.temp.Rooms;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

/**
 * Apartment
 */
public class Apartment extends DomainObjectWithStatus {

	private Building building;
	private Set<Rooms> roomses = Collections.emptySet();
	private Set<ApartmentNumber> apartmentNumbers = Collections.emptySet();
	private Set<ApartmentAttribute> apartmentAttributes = Collections.emptySet();
	private Set<ApartmentRelation> apartmentRelations = Collections.emptySet();
	private Set<Person> persons = Collections.emptySet();

	public Apartment() {
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Set<Rooms> getRoomses() {
		return this.roomses;
	}

	public void setRoomses(Set<Rooms> roomses) {
		this.roomses = roomses;
	}

	public Set<ApartmentNumber> getApartmentNumbers() {
		return this.apartmentNumbers;
	}

	public void setApartmentNumbers(Set<ApartmentNumber> apartmentNumbers) {
		this.apartmentNumbers = apartmentNumbers;
	}

	public Set<ApartmentAttribute> getApartmentAttributes() {
		return this.apartmentAttributes;
	}

	public void setApartmentAttributes(Set<ApartmentAttribute> apartmentAttributes) {
		this.apartmentAttributes = apartmentAttributes;
	}

	public Set<ApartmentRelation> getApartmentRelations() {
		return this.apartmentRelations;
	}

	public void setApartmentRelations(Set<ApartmentRelation> apartmentRelations) {
		this.apartmentRelations = apartmentRelations;
	}

	public Set<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
}
