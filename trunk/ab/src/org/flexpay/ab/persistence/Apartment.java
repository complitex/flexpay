package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.DateIntervalUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collections;
import java.util.Set;
import java.util.Date;

/**
 * Apartment
 */
public class Apartment extends DomainObjectWithStatus {

	private Building building;
	private Set<ApartmentNumber> apartmentNumbers = Collections.emptySet();
	private Set<Person> persons = Collections.emptySet();

	public Apartment() {
	}

	public Apartment(Long id) {
		super(id);
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Set<ApartmentNumber> getApartmentNumbers() {
		return this.apartmentNumbers;
	}

	public void setApartmentNumbers(Set<ApartmentNumber> apartmentNumbers) {
		this.apartmentNumbers = apartmentNumbers;
	}

	public Set<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
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
			if (begin.compareTo(dt) <=0 && dt.compareTo(end) <= 0) {
				return number.getValue();
			}
		}

		return "unknown";
	}

	/**
	 * Get current apartment number
	 *
	 * @return apartment number
	 */
	public String getNumber() {
		return getNumberForDate(DateIntervalUtil.now());
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("numbers", apartmentNumbers.toArray())
				.toString();
	}
}
