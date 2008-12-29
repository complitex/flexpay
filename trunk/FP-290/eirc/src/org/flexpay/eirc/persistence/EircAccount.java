package org.flexpay.eirc.persistence;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Internal EIRC tracker for personal accounts
 */
public class EircAccount extends DomainObjectWithStatus {

	private Person person;
	private Apartment apartment;
	private String accountNumber;
	private ConsumerInfo consumerInfo;
	private Set<Consumer> consumers = Collections.emptySet();

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

	public ConsumerInfo getConsumerInfo() {
		return consumerInfo;
	}

	public void setConsumerInfo(ConsumerInfo consumerInfo) {
		this.consumerInfo = consumerInfo;
	}

	@Nullable
	public Stub<Person> getPersonStub() {
		if (person == null) {
			return null;
		}
		return new Stub<Person>(person);
	}

	@NotNull
	public Stub<Apartment> getApartmentStub() {
		return new Stub<Apartment>(apartment);
	}
}
