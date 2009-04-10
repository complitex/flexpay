package org.flexpay.eirc.persistence;

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

	private Apartment apartment;
	private ConsumerInfo consumerInfo;
	private EircAccount eircAccount;

	private Service service;
	private String externalAccountNumber;
	private Person responsiblePerson;
	private Date beginDate;
	private Date endDate;

	/**
	 * Constructs a new DomainObject.
	 */
	public Consumer() {
	}

	public Consumer(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'service'.
	 *
	 * @return Value for property 'service'.
	 */
	public Service getService() {
		return service;
	}

	/**
	 * Setter for property 'service'.
	 *
	 * @param service Value to set for property 'service'.
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * Getter for property 'externalAccountNumber'.
	 *
	 * @return Value for property 'externalAccountNumber'.
	 */
	public String getExternalAccountNumber() {
		return externalAccountNumber;
	}

	/**
	 * Setter for property 'externalAccountNumber'.
	 *
	 * @param externalAccountNumber Value to set for property 'externalAccountNumber'.
	 */
	public void setExternalAccountNumber(String externalAccountNumber) {
		this.externalAccountNumber = externalAccountNumber;
	}

	/**
	 * Getter for property 'responsiblePerson'.
	 *
	 * @return Value for property 'responsiblePerson'.
	 */
	@Nullable
	public Person getResponsiblePerson() {
		return responsiblePerson;
	}

	/**
	 * Setter for property 'responsiblePerson'.
	 *
	 * @param responsiblePerson Value to set for property 'responsiblePerson'.
	 */
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

	/**
	 * Getter for property 'apartment'.
	 *
	 * @return Value for property 'apartment'.
	 */
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * Setter for property 'apartment'.
	 *
	 * @param apartment Value to set for property 'apartment'.
	 */
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public ConsumerInfo getConsumerInfo() {
		return consumerInfo;
	}

	public void setConsumerInfo(ConsumerInfo consumerInfo) {
		this.consumerInfo = consumerInfo;
	}

	/**
	 * @return the eircAccount
	 */
	public EircAccount getEircAccount() {
		return eircAccount;
	}

	/**
	 * @param eircAccount the eircAccount to set
	 */
	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	public Stub<EircAccount> getEircAccountStub() {
		return stub(eircAccount);
	}

	@NotNull
	public Stub<Service> getServiceStub() {
		return stub(getService());
	}
}
