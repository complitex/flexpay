package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

public class Consumer extends DomainObjectWithStatus {

	private PersonalAccount internalAccount;
	private Service service;
	private String externalAccountNumber;

	/**
	 * Constructs a new DomainObject.
	 */
	public Consumer() {
	}

	public Consumer(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'internalAccount'.
	 *
	 * @return Value for property 'internalAccount'.
	 */
	public PersonalAccount getInternalAccount() {
		return internalAccount;
	}

	/**
	 * Setter for property 'internalAccount'.
	 *
	 * @param internalAccount Value to set for property 'internalAccount'.
	 */
	public void setInternalAccount(PersonalAccount internalAccount) {
		this.internalAccount = internalAccount;
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
}
