package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class AccountRecord extends DomainObject {

	private AccountRecordType recordType;
	private Date operationDate;
	private BigDecimal amount;
	private RegistryRecord sourceRegistryRecord;
	private AbstractConsumer consumer;

	// Organisation performed operation with the account
	private Organisation organisation;

	/**
	 * Constructs a new DomainObject.
	 */
	public AccountRecord() {
	}

	public AccountRecord(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'recordType'.
	 *
	 * @return Value for property 'recordType'.
	 */
	public AccountRecordType getRecordType() {
		return recordType;
	}

	/**
	 * Setter for property 'recordType'.
	 *
	 * @param recordType Value to set for property 'recordType'.
	 */
	public void setRecordType(AccountRecordType recordType) {
		this.recordType = recordType;
	}

	/**
	 * Getter for property 'amount'.
	 *
	 * @return Value for property 'amount'.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Setter for property 'amount'.
	 *
	 * @param amount Value to set for property 'amount'.
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Getter for property 'operationDate'.
	 *
	 * @return Value for property 'operationDate'.
	 */
	public Date getOperationDate() {
		return operationDate;
	}

	/**
	 * Setter for property 'operationDate'.
	 *
	 * @param operationDate Value to set for property 'operationDate'.
	 */
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	/**
	 * Getter for property 'consumer'.
	 *
	 * @return Value for property 'consumer'.
	 */
	public AbstractConsumer getConsumer() {
		return consumer;
	}

	/**
	 * Setter for property 'consumer'.
	 *
	 * @param consumer Value to set for property 'consumer'.
	 */
	public void setConsumer(AbstractConsumer consumer) {
		this.consumer = consumer;
	}

	/**
	 * Getter for property 'organisation'.
	 *
	 * @return Value for property 'organisation'.
	 */
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 * Setter for property 'organisation'.
	 *
	 * @param organisation Value to set for property 'organisation'.
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public RegistryRecord getSourceRegistryRecord() {
		return sourceRegistryRecord;
	}

	public void setSourceRegistryRecord(RegistryRecord sourceRegistryRecord) {
		this.sourceRegistryRecord = sourceRegistryRecord;
	}

	public String toString() {
		return new ToStringBuilder(this,  ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("organisation", getOrganisation().getId())
				.append("date", getOperationDate())
				.append("amount", getAmount())
				.append("type", getRecordType().getDescription())
				.toString();
	}
}
