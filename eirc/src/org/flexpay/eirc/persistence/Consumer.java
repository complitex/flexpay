package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class Consumer extends DomainObjectWithStatus {

	private String externalAccountNumber;
	private Date beginDate;
	private Date endDate;

	private Apartment apartment;
	private ConsumerInfo consumerInfo;
	private EircAccount eircAccount;
	private Service service;
	private Person responsiblePerson;

	public Consumer() {
	}

	public Consumer(Long id) {
		super(id);
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getExternalAccountNumber() {
		return externalAccountNumber;
	}

	public void setExternalAccountNumber(String externalAccountNumber) {
		this.externalAccountNumber = externalAccountNumber;
	}

	@Nullable
	public Person getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(@Nullable Person responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
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

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public ConsumerInfo getConsumerInfo() {
		return consumerInfo;
	}

	public void setConsumerInfo(ConsumerInfo consumerInfo) {
		this.consumerInfo = consumerInfo;
	}

	public EircAccount getEircAccount() {
		return eircAccount;
	}

	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	public Stub<EircAccount> getEircAccountStub() {
		return stub(eircAccount);
	}

	public Stub<Apartment> getApartmentStub() {
		return stub(apartment);
	}

	@NotNull
	public Stub<Service> getServiceStub() {
		return stub(getService());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("externalAccountNumber", externalAccountNumber).
				append("beginDate", beginDate).
				append("endDate", endDate).
				toString();
	}

}
