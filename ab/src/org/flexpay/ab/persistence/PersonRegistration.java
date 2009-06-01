package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class PersonRegistration extends DomainObject implements Comparable<PersonRegistration> {

	private Person person;
	private Apartment apartment;
	private Date beginDate;
	private Date endDate;

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the apartment
	 */
	@NotNull
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * @param apartment the apartment to set
	 */
	public void setApartment(@NotNull Apartment apartment) {
		this.apartment = apartment;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isValid(@NotNull Date date) {
		return beginDate.compareTo(date) <= 0 && date.compareTo(endDate) <= 0;
	}

	public int compareTo(PersonRegistration o) {
		return beginDate.compareTo(o.beginDate);
	}

	public PersonRegistration copy() {
		PersonRegistration result = new PersonRegistration();
		result.setPerson(person);
		result.setApartment(apartment);
		result.setBeginDate(beginDate);
		result.setEndDate(endDate);
		return result;
	}
}
