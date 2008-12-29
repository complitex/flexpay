package org.flexpay.eirc.persistence;

import java.math.BigDecimal;

import org.flexpay.common.persistence.DomainObject;

public class TicketServiceAmount extends DomainObject {
	private Ticket ticket;
	private Consumer consumer;
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
	 * @return the consumer
	 */
	public Consumer getConsumer() {
		return consumer;
	}

	/**
	 * @param consumer the consumer to set
	 */
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	/**
	 * @return the dateFromAmaunt
	 */
	public BigDecimal getDateFromAmount() {
		return dateFromAmount;
	}

	/**
	 * @param dateFromAmount the dateFromAmaunt to set
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
	 * @param dateTillAmount the dateTillAmaunt to set
	 */
	public void setDateTillAmount(BigDecimal dateTillAmount) {
		this.dateTillAmount = dateTillAmount;
	}

}
