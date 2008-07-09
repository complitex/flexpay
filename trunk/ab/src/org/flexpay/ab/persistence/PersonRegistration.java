package org.flexpay.ab.persistence;

import java.util.Date;

import org.flexpay.common.persistence.DomainObject;

public class PersonRegistration extends DomainObject {
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
	public Apartment getApartment() {
		return apartment;
	}
	/**
	 * @param apartment the apartment to set
	 */
	public void setApartment(Apartment apartment) {
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
	
	public boolean isValid(Date date) {
		return endDate.after(beginDate) && !date.before(beginDate) && date.before(endDate) ? true : false;
	}

}
