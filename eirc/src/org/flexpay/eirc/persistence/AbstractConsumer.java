package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.ab.persistence.Person;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public abstract class AbstractConsumer extends DomainObjectWithStatus {

	private Service service;
	private String externalAccountNumber;
	private Person responsiblePerson;
	private Date beginDate;
	private Date endDate;

	/**
	 * Constructs a new DomainObject.
	 */
	public AbstractConsumer() {
	}

	public AbstractConsumer(Long id) {
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
}
