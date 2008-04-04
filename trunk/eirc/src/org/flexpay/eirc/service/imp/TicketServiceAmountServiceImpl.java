package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.TicketServiceAmountDao;
import org.flexpay.eirc.persistence.TicketServiceAmount;
import org.flexpay.eirc.service.TicketServiceAmountService;

public class TicketServiceAmountServiceImpl implements TicketServiceAmountService{
	TicketServiceAmountDao ticketServiceAmountDao;
	
	/**
	 * Create new TicketServiceAmount
	 *
	 * @param ticketServiceAmount TicketServiceAmount
	 * @return persisted TicketServiceAmount
	 */
	public TicketServiceAmount create(TicketServiceAmount ticketServiceAmount) {
		Long id = ticketServiceAmountDao.create(ticketServiceAmount);
		ticketServiceAmount.setId(id);
		return ticketServiceAmount;
	}

	/**
	 * @param ticketServiceAmountDao the ticketServiceAmountDao to set
	 */
	public void setTicketServiceAmountDao(
			TicketServiceAmountDao ticketServiceAmountDao) {
		this.ticketServiceAmountDao = ticketServiceAmountDao;
	}

}
