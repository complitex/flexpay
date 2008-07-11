package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.DateIntervalUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Apartment
 */
public class Apartment extends DomainObjectWithStatus {

	public static final String NUMBER_UNKNOWN = "unknown";

	private Building building;
	private Set<ApartmentNumber> apartmentNumbers = Collections.emptySet();
	private Set<PersonRegistration> personRegistrations = Collections.emptySet();

	public Apartment() {
	}

	public Apartment(Long id) {
		super(id);
	}

	@NotNull
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(@NotNull Building building) {
		this.building = building;
	}

	public Set<ApartmentNumber> getApartmentNumbers() {
		return this.apartmentNumbers;
	}

	public void setApartmentNumbers(Set<ApartmentNumber> apartmentNumbers) {
		this.apartmentNumbers = apartmentNumbers;
	}

	/**
	 * Get apartment number for particular date
	 *
	 * @param dt Date to get apartment number for
	 * @return apartment number
	 */
	public String getNumberForDate(Date dt) {
		for (ApartmentNumber number : apartmentNumbers) {
			Date begin = number.getBegin();
			Date end = number.getEnd();
			if (begin.compareTo(dt) <= 0 && dt.compareTo(end) < 0) {
				return number.getValue();
			}
		}

		return NUMBER_UNKNOWN;
	}

	/**
	 * Get current apartment number
	 *
	 * @return apartment number
	 */
	public String getNumber() {
		return getNumberForDate(DateIntervalUtil.now());
	}

	public void setNumber(String number) throws ObjectAlreadyExistException {
		Date nowDate = DateIntervalUtil.now();
		if (number == null || number.equals("")
			|| number.equals(getNumberForDate(nowDate))) {
			// nothing to do
			return;
		}

		Building building = getBuilding();
		Set<Apartment> apartmentSet = building.getApartments();
		for (Apartment a : apartmentSet) {
			if (number.equals(a.getNumber())) {
				throw new ObjectAlreadyExistException();
			}
		}

		// Check if apartment numbers is not empty (Collections.EMPTY_SET)
		if (getApartmentNumbers().isEmpty()) {
			setApartmentNumbers(new HashSet<ApartmentNumber>());
		}

		// set up previous numbers to end at the record's operation date
		for (ApartmentNumber apartmentNumber : getApartmentNumbers()) {
			if (apartmentNumber.getEnd().after(nowDate)) {
				apartmentNumber.setEnd(nowDate);
			}
		}

		// Create a new apartment number and setup its properties
		ApartmentNumber apartmentNumber = new ApartmentNumber();
		apartmentNumber.setBegin(nowDate);
		apartmentNumber.setEnd(ApplicationConfig.getInstance()
				.getFutureInfinite());
		apartmentNumber.setValue(number);
		apartmentNumber.setApartment(this);

		// Add number to apartment numbers set
		getApartmentNumbers().add(apartmentNumber);
	}


	public Set<Person> getPersons() {
		return getPersons(DateIntervalUtil.now());
	}

	public Set<Person> getPersons(Date date) {
		Set<Person> persons = new HashSet<Person>();
		for (PersonRegistration reg : personRegistrations) {
			if (reg.isValid(date)) {
				persons.add(reg.getPerson());
			}
		}

		return persons;
	}

	public Set<PersonRegistration> getValidPersonRegistrations() {
		return getValidPersonRegistrations(DateIntervalUtil.now());
	}

	public Set<PersonRegistration> getValidPersonRegistrations(Date date) {
		Set<PersonRegistration> result = new HashSet<PersonRegistration>();
		for (PersonRegistration reg : personRegistrations) {
			if (reg.isValid(date)) {
				result.add(reg);
			}
		}

		return result;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append(
				"id", getId()).append("numbers", apartmentNumbers.toArray())
				.toString();
	}

	/**
	 * @return the personRegistrations
	 */
	public Set<PersonRegistration> getPersonRegistrations() {
		return personRegistrations;
	}

	/**
	 * @param personRegistrations the personRegistrations to set
	 */
	public void setPersonRegistrations(Set<PersonRegistration> personRegistrations) {
		this.personRegistrations = personRegistrations;
	}
}
