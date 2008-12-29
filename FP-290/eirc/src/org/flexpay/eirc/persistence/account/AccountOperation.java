package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Apartment;

import java.math.BigDecimal;
import java.util.Date;

public class AccountOperation extends DomainObject {

	private Long uniqueBunchNumber;
	private BigDecimal summ;
	private Account debetAccount;
	private Account creditAccount;
	private AccountOperationType operationType;
	private Date operationTime;
	private Addin addin;
	private Person performer;
	private Apartment location;
	private String cashDeskNumber;
}
