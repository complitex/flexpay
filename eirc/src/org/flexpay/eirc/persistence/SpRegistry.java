package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Registry header for service providers exchange file
 */
public class SpRegistry extends DomainObject {

	private SpFile spFile;
	private SpRegistryType registryType;

	private Long registryNumber;
	private Long recordsNumber;
	private Date creationDate;
	private Date fromDate;
	private Date tillDate;
	private Long senderCode;
	private Long recipientCode;
	private BigDecimal amount;

	private String containers;

	private Organisation sender;
	private Organisation recipient;
	private ServiceProvider serviceProvider;
	
	private SpRegistryStatus registryStatus;
	private SpRegistryArchiveStatus archiveStatus;

	/**
	 * @return the registryStatus
	 */
	public SpRegistryStatus getRegistryStatus() {
		return registryStatus;
	}

	/**
	 * @param registryStatus the registryStatus to set
	 */
	public void setRegistryStatus(SpRegistryStatus registryStatus) {
		this.registryStatus = registryStatus;
	}

	/**
	 * @return the spFile
	 */
	public SpFile getSpFile() {
		return spFile;
	}

	/**
	 * @param spFile the spFile to set
	 */
	public void setSpFile(SpFile spFile) {
		this.spFile = spFile;
	}

	/**
	 * @return the registryType
	 */
	public SpRegistryType getRegistryType() {
		return registryType;
	}

	/**
	 * @param registryType the registryType to set
	 */
	public void setRegistryType(SpRegistryType registryType) {
		this.registryType = registryType;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the tillDate
	 */
	public Date getTillDate() {
		return tillDate;
	}

	/**
	 * @param tillDate the tillDate to set
	 */
	public void setTillDate(Date tillDate) {
		this.tillDate = tillDate;
	}

	/**
	 * @return the sender
	 */
	public Long getSenderCode() {
		return senderCode;
	}

	/**
	 * @param senderCode the sender to set
	 */
	public void setSenderCode(Long senderCode) {
		this.senderCode = senderCode;
	}

	/**
	 * @return the recipient
	 */
	public Long getRecipientCode() {
		return recipientCode;
	}

	/**
	 * @param recipientCode the recipient to set
	 */
	public void setRecipientCode(Long recipientCode) {
		this.recipientCode = recipientCode;
	}

	/**
	 * @return the containers
	 */
	public String getContainers() {
		return containers;
	}

	/**
	 * @param containers the containers to set
	 */
	public void setContainers(String containers) {
		this.containers = containers;
	}

	/**
	 * @return the registryNum
	 */
	public Long getRegistryNumber() {
		return registryNumber;
	}

	/**
	 * @param registryNumber the registryNum to set
	 */
	public void setRegistryNumber(Long registryNumber) {
		this.registryNumber = registryNumber;
	}

	/**
	 * @return the recordNum
	 */
	public Long getRecordsNumber() {
		return recordsNumber;
	}

	/**
	 * @param recordsNumber the recordNum to set
	 */
	public void setRecordsNumber(Long recordsNumber) {
		this.recordsNumber = recordsNumber;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Getter for property 'serviceProvider'.
	 *
	 * @return Value for property 'serviceProvider'.
	 */
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	/**
	 * Setter for property 'serviceProvider'.
	 *
	 * @param serviceProvider Value to set for property 'serviceProvider'.
	 */
	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Organisation getSender() {
		return sender;
	}

	public void setSender(Organisation sender) {
		this.sender = sender;
	}

	public Organisation getRecipient() {
		return recipient;
	}

	public void setRecipient(Organisation recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return the archiveStatus
	 */
	public SpRegistryArchiveStatus getArchiveStatus() {
		return archiveStatus;
	}

	/**
	 * @param archiveStatus the archiveStatus to set
	 */
	public void setArchiveStatus(SpRegistryArchiveStatus archiveStatus) {
		this.archiveStatus = archiveStatus;
	}
}
