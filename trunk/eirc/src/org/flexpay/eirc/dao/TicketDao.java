package org.flexpay.eirc.dao;

import java.util.Date;
import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Ticket;

public interface TicketDao extends GenericDao<Ticket, Long> {
	List<Ticket> findObjectsByPersonAndTillDate(Page<Ticket> pager,
			Long personId, Date dateTill);

	List<Ticket> findByOrganizationAndInterval(Long serviceOrganizationId,
			Date dateFrom, Date dateTill);

}
