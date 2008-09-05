package org.flexpay.eirc.process.quittance.report;

import org.flexpay.eirc.persistence.ServiceType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

/**
 * Container for all necessary Quittance information with calculated summs, service tarifs, subsidies, etc
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

	public static class ServiceTotals {

		private ServiceType serviceType;
		private String serviceCode;
		private BigDecimal tarif;
		private BigDecimal expence;
		private String expenceUnit;
		private BigDecimal charges;
		private BigDecimal recalculation;
		private BigDecimal privilege;
		private BigDecimal subsidy;
		private BigDecimal payed;
		private BigDecimal outgoingDebt;

		private List<SubServiceTotals> subServicesTotals;
	}

	public static class SubServiceTotals {

		private ServiceType serviceType;
		private BigDecimal tarif;
		private BigDecimal recalculation;
	}
}
