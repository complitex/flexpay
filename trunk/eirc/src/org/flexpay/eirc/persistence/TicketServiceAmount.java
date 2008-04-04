package org.flexpay.eirc.persistence;

import java.math.BigDecimal;

import org.flexpay.common.persistence.DomainObject;

public class TicketServiceAmount extends DomainObject {
	private Ticket ticket;
	private ServiceType serviceType;
	private BigDecimal dateFromAmount;
	private BigDecimal dateTillAmount;

	public TicketServiceAmount() {
	}

	public TicketServiceAmount(Long id) {
		super(id);
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	/**
	 * @return the serviceType
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the dateFromAmaunt
	 */
	public BigDecimal getDateFromAmount() {
		return dateFromAmount;
	}

	/**
	 * @param dateFromAmaunt the dateFromAmaunt to set
	 */
	public void setDateFromAmount(BigDecimal dateFromAmount) {
		this.dateFromAmount = dateFromAmount;
	}

	/**
	 * @return the dateTillAmaunt
	 */
	public BigDecimal getDateTillAmount() {
		return dateTillAmount;
	}

	/**
	 * @param dateTillAmaunt the dateTillAmaunt to set
	 */
	public void setDateTillAmount(BigDecimal dateTillAmount) {
		this.dateTillAmount = dateTillAmount;
	}

}
