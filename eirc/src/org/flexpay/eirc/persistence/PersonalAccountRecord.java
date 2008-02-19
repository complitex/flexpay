package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.math.BigDecimal;
import java.util.Date;

public class PersonalAccountRecord extends DomainObject {

	private PersonalAccount account;
	private Date billBeginDate;
	private Date billEndDate;
	private Service service;
	private BigDecimal amount;

	/**
	 * Constructs a new DomainObject.
	 */
	public PersonalAccountRecord() {
	}

	public PersonalAccountRecord(Long id) {
		super(id);
	}

	public PersonalAccount getAccount() {
		return account;
	}

	public void setAccount(PersonalAccount account) {
		this.account = account;
	}

	public Date getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(Date billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public Date getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(Date billEndDate) {
		this.billEndDate = billEndDate;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
