package org.flexpay.eirc.dao;

import java.util.Date;
import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.account.QuittanceDetails;

public interface QuittanceDetailsDao extends GenericDao<QuittanceDetails, Long> {
	
	List<QuittanceDetails> findByEircAccountAndDateTill(Long id, Date dateFrom, Date dateTill);

}