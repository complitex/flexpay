package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class PersonRegistration extends DomainObject implements Comparable<PersonRegistration> {

	private Person person;
	private Apartment apartment;
	private Date beginDate;
	private Date endDate;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isValid(@NotNull Date date) {
		return beginDate.compareTo(date) <= 0 && date.compareTo(endDate) <= 0;
	}

	@Override
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("beginDate", beginDate).
				append("endDate", endDate).
				append("personId", person == null ? null : person.getId()).
				append("apartmentId", apartment == null ? null : apartment.getId()).
				toString();
	}

}
