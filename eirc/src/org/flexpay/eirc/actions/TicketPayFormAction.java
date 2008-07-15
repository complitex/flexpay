package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.pdf.PdfTicketWriter.TicketInfo;
import org.flexpay.eirc.service.TicketService;

public class TicketPayFormAction extends FPActionSupport {

	private TicketService ticketService;

	private Long ticketId;
	private TicketInfo ticketInfo;


	public String doExecute() throws FlexPayException {
		if (isSubmit()) {
			ticketInfo = ticketService.getTicketInfo(ticketId);
			return SUCCESS;
		}

		return INPUT;
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
	 * @param ticketService the ticketService to set
	 */
	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * @return the ticketInfo
	 */
	public TicketInfo getTicketInfo() {
		return ticketInfo;
	}

}
