package org.flexpay.eirc.actions;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.pdf.PdfTicketWriter.TicketInfo;
import org.flexpay.eirc.service.TicketService;

public class TicketPayFormAction extends CommonAction {
	
	private TicketService ticketService;
	
	private Long ticketId;
	private TicketInfo ticketInfo;
	
	
	public String execute() throws FlexPayException {
		if (isSubmitted()) {
			ticketInfo = ticketService.getTicketInfo(ticketId);
			return "pay";
		}
		
		return "form";
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
