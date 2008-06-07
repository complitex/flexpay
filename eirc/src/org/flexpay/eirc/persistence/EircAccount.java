package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Apartment;

/**
 * Internal EIRC tracker for personal accounts
 */
public class EircAccount extends DomainObjectWithStatus {

	private Person person;
	private Apartment apartment;
	private String accountNumber;

	/**
	 * Constructs a new DomainObject.
	 */
	public EircAccount() {
	}

	public EircAccount(Long id) {
		super(id);
	}

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

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
