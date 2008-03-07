package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.math.BigDecimal;
import java.util.Date;

public class PersonalAccountRecord extends DomainObject {

	private PersonalAccountRecordType recordType;
	private Date operationDate;
	private BigDecimal amount;
	private Consumer consumer;

	/**
	 * Constructs a new DomainObject.
	 */
	public PersonalAccountRecord() {
	}

	public PersonalAccountRecord(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'recordType'.
	 *
	 * @return Value for property 'recordType'.
	 */
	public PersonalAccountRecordType getRecordType() {
		return recordType;
	}

	/**
	 * Setter for property 'recordType'.
	 *
	 * @param recordType Value to set for property 'recordType'.
	 */
	public void setRecordType(PersonalAccountRecordType recordType) {
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
	public Consumer getConsumer() {
		return consumer;
	}

	/**
	 * Setter for property 'consumer'.
	 *
	 * @param consumer Value to set for property 'consumer'.
	 */
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}
}
