package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Internal EIRC tracker for personal accounts
 */
public class EircAccount extends DomainObjectWithStatus {

    private String accountNumber;

	private Person person;
	private Apartment apartment;
	private ConsumerInfo consumerInfo;

	private Set<Consumer> consumers = set();

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

	public Set<Consumer> getConsumers() {
		return consumers;
	}

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("id", id).
                append("status", status).
                append("accountNumber", accountNumber).
                toString();
    }
}
