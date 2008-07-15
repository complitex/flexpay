package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.service.TicketService;

public class TicketPayAction extends FPActionSupport {

	private TicketService ticketService;

	private Long ticketId;

	public String doExecute() {
		ticketService.payTicket(ticketId);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return SUCCESS;
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
