package org.flexpay.eirc.dao;

import java.util.Date;
import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.account.Quittance;

public interface QuittanceDao extends GenericDao<Quittance, Long>{
	List<Quittance> findObjectsByEircAccountAndDateTill(Page<Quittance> pager,
			Long eircAccountId, Date dateTill);
	
	List<Quittance> findByServiceOrganisationAndDate(Long serviceOrganisationId, Date dateFrom, Date dateTill);
}
