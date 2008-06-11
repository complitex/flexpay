package org.flexpay.eirc.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
	private Set<Consumer> consumers = Collections.emptySet();;

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

	/**
	 * @return the consumers
	 */
	public Set<Consumer> getConsumers() {
		return consumers;
	}

	/**
	 * @param consumers the consumers to set
	 */
	public void setConsumers(Set<Consumer> consumers) {
		this.consumers = consumers;
	}

	
}
