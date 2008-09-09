package org.flexpay.eirc.process.quittance.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Container for all necessary Quittance information with calculated summs, service
 * tarifs, subsidies, etc
 */
public class QuittanceInfo {

	private String quittanceNumber;
	private String address;
	private String personFIO;

	private Date periodBeginDate;
	private Date periodEndDate;
	private Date operationDate;

	private BigDecimal summToPay;

	private BigDecimal totalSquare;
	private BigDecimal warmSquare;
	private BigDecimal privileges;
	private int habitantNumber;
	private int privilegersNumber;

	private BigDecimal incomingDebt;
	private BigDecimal outgoingDebt;

	private String jksBankAccount;
	private String bankAccount;

	private List<ServiceTotals> servicesTotals;

}
