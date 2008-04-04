package org.flexpay.eirc.service;

import java.util.Date;

import org.flexpay.eirc.persistence.Ticket;

public interface TicketService {
	/**
	 * Create new Ticket
	 *
	 * @param ticket Ticket
	 * @return persisted Ticket
	 */
	Ticket create(Ticket ticket);
	
	void generateForServiceOrganisation(Long serviceOrganisationId, Date dateFrom, Date dateTill);

}
