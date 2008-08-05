package org.flexpay.eirc.service;

import java.util.Date;
import java.util.List;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.pdf.PdfTicketWriter.TicketInfo;
import org.flexpay.eirc.persistence.Ticket;
import org.flexpay.eirc.persistence.ServiceOrganisation;

public interface TicketService {
	/**
	 * Create new Ticket
	 * 
	 * @param ticket
	 *            Ticket
	 * @return persisted Ticket
	 */
	Ticket create(Ticket ticket);

	void generateForServiceOrganisation(Stub<ServiceOrganisation> stub,
			Date dateFrom, Date dateTill);

	List<Object> getTicketsWithDelimiters(Long serviceOrganisationId,
			Date dateFrom, Date dateTill) throws FlexPayException;

	TicketInfo getTicketInfo(Long ticketId) throws FlexPayException;
	
	public void payTicket(Long ticketId);

}
