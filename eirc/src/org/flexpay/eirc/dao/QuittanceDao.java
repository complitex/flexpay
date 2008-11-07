package org.flexpay.eirc.dao;

import java.util.Date;
import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.account.Quittance;
import org.jetbrains.annotations.NotNull;

public interface QuittanceDao extends GenericDao<Quittance, Long>{
	List<Quittance> findObjectsByEircAccountAndDateTill(Page<Quittance> pager,
			Long eircAccountId, Date dateTill);
	
	List<Quittance> listQuittancesForPrinting(Long serviceOrganisationId, Date dateFrom, Date dateTill);

	/**
	 * Get quittance by account number, till date and order number
	 *
	 * @param accountNumber Eirc account number quittance was generated for
	 * @param month Month the quittance was generated for
	 * @param number Order of quittance in specified month
	 * @return List of quittances, possibly empty, usuals case is one quittance list
	 */
	@NotNull
	List<Quittance> findQuittanceByNumber(String accountNumber, Date month, Integer number);
}
