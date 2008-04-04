package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.TicketServiceAmount;

public interface TicketServiceAmountService {
	/**
	 * Create new TicketServiceAmount
	 *
	 * @param ticketServiceAmount TicketServiceAmount
	 * @return persisted TicketServiceAmount
	 */
	TicketServiceAmount create(TicketServiceAmount ticketServiceAmount);

}
