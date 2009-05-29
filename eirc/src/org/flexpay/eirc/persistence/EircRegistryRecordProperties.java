package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.Nullable;

public class EircRegistryRecordProperties extends RegistryRecordProperties {

	private Consumer consumer;
	private Apartment apartment;
	private Person person;
	private Service service;

	@Nullable
	public Stub<Person> getPersonStub() {
		if (person == null) {
			return null;
		}
		return new Stub<Person>(person);
	}

	@Nullable
	public Stub<Apartment> getApartmentStub() {
		if (apartment == null) {
			return null;
		}
		return new Stub<Apartment>(apartment);
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	@Nullable
	public Stub<Consumer> getConsumerStub() {
		return stub(consumer);
	}

	@SuppressWarnings ({"UnnecessaryBoxing"})
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("apartment-id", apartment == null ? 0L : apartment.getId())
				.append("person-id", person == null ? 0L : person.getId())
				.append("service-id", service == null ? 0L : service.getId())
				.append("consumer-id", consumer == null ? 0L : consumer.getId())
				.toString();
	}

}
