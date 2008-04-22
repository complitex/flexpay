package org.flexpay.eirc.actions;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.eirc.service.TicketService;

public class TicketPayAction extends CommonAction {
	
	private TicketService ticketService;
	
	private Long ticketId;
	
	public String execute() {
		ticketService.payTicket(ticketId);
		
		return "success";
	}

	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * @param ticketService the ticketService to set
	 */
	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	

}
