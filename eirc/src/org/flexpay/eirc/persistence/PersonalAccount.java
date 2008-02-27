package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Apartment;

import java.util.Date;

public class PersonalAccount extends DomainObject {

	private Person responsiblePerson;
	private Apartment apartment;
	private Date creationDate;
	private AccountStatus status;
	private String accountNumber;

	/**
	 * Constructs a new DomainObject.
	 */
	public PersonalAccount() {
	}

	public PersonalAccount(Long id) {
		super(id);
	}

	public Person getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(Person responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
