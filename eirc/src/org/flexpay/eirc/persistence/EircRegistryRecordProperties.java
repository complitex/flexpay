package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.Nullable;

import static org.flexpay.common.persistence.Stub.stub;

public class EircRegistryRecordProperties extends RegistryRecordProperties {

	private Consumer consumer;
	private boolean fullConsumer;
	private Apartment apartment;
	private boolean fullApartment;
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

	public Stub<Service> getServiceStub() {
		return stub(service);
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

	/**
	 * Set Consumer with all data read, useful for further processing
	 *
	 * @param consumer Read full consumer
	 */
	public void setFullConsumer(Consumer consumer) {
		this.consumer = consumer;
		fullConsumer = true;
	}

	public boolean hasFullConsumer() {
		return fullConsumer;
	}

	public void setFullApartment(Apartment apartment) {
		this.apartment = apartment;
		fullApartment = true;
	}

	public boolean hasFullApartment() {
		return fullApartment;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		EircRegistryRecordProperties that = (EircRegistryRecordProperties) o;

		if (fullApartment != that.fullApartment) return false;
		if (fullConsumer != that.fullConsumer) return false;
		if (apartment != null ? !apartment.equals(that.apartment) : that.apartment != null) return false;
		if (consumer != null ? !consumer.equals(that.consumer) : that.consumer != null) return false;
		if (person != null ? !person.equals(that.person) : that.person != null) return false;
		if (service != null ? !service.equals(that.service) : that.service != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (consumer != null ? consumer.hashCode() : 0);
		result = 31 * result + (fullConsumer ? 1 : 0);
		result = 31 * result + (apartment != null ? apartment.hashCode() : 0);
		result = 31 * result + (fullApartment ? 1 : 0);
		result = 31 * result + (person != null ? person.hashCode() : 0);
		result = 31 * result + (service != null ? service.hashCode() : 0);
		return result;
	}
}
