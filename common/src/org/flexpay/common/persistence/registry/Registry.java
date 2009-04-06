package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.FPFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Registry header for flexpay exchange file
 */
public class Registry extends DomainObject {

	private FPFile spFile;
	private RegistryType registryType;

	private Long registryNumber;
	private Long recordsNumber;
	private Date creationDate;
	private Date fromDate;
	private Date tillDate;
	private Long senderCode;
	private Long recipientCode;
	private BigDecimal amount;

	private List<RegistryContainer> containers = Collections.emptyList();

	private RegistryStatus registryStatus;
	private RegistryArchiveStatus archiveStatus;

	private RegistryProperties properties;

	private int errorsNumber;

	/**
	 * Constructs a new DomainObject.
	 */
	public Registry() {
	}

	public Registry(Long id) {
		super(id);
	}

	/**
	 * @return the registryStatus
	 */
	public RegistryStatus getRegistryStatus() {
		return registryStatus;
	}

	/**
	 * @param registryStatus the registryStatus to set
	 */
	public void setRegistryStatus(RegistryStatus registryStatus) {
		this.registryStatus = registryStatus;
	}

	/**
	 * @return the spFile
	 */
	public FPFile getSpFile() {
		return spFile;
	}

	/**
	 * @param spFile the spFile to set
	 */
	public void setSpFile(FPFile spFile) {
		this.spFile = spFile;
	}

	/**
	 * @return the registryType
	 */
	public RegistryType getRegistryType() {
		return registryType;
	}

	/**
	 * @param registryType the registryType to set
	 */
	public void setRegistryType(RegistryType registryType) {
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
	public List<RegistryContainer> getContainers() {
		return containers;
	}

	/**
	 * @param containers the containers to set
	 */
	public void setContainers(List<RegistryContainer> containers) {
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
	 * @return the archiveStatus
	 */
	public RegistryArchiveStatus getArchiveStatus() {
		return archiveStatus;
	}

	/**
	 * @param archiveStatus the archiveStatus to set
	 */
	public void setArchiveStatus(RegistryArchiveStatus archiveStatus) {
		this.archiveStatus = archiveStatus;
	}

	public RegistryProperties getProperties() {
		return properties;
	}

	public void setProperties(RegistryProperties properties) {
		properties.setRegistry(this);
		this.properties = properties;
	}

	public int getErrorsNumber() {
		return errorsNumber;
	}

	public void setErrorsNumber(int errorsNumber) {
		this.errorsNumber = errorsNumber;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("Registry {").
				append("id", getId()).
				append("registryType", registryType).
				append("registryNumber", registryNumber).
				append("recordsNumber", recordsNumber).
				append("creationDate", creationDate).
				append("fromDate", fromDate).
				append("tillDate", tillDate).
				append("senderCode", senderCode).
				append("recipientCode", recipientCode).
				append("amount", amount).
				append("errorsNumber", errorsNumber).
				append("}").toString();
	}

}
