package org.flexpay.eirc.persistence;

import org.flexpay.ab.persistence.Apartment;

public class Consumer extends AbstractConsumer {

	private Apartment apartment;
	private ConsumerInfo consumerInfo;
	private EircAccount eircAccount;

	/**
	 * Constructs a new DomainObject.
	 */
	public Consumer() {
	}

	public Consumer(Long id) {
		super(id);
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
}
