package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.account.QuittanceDetails;

import java.util.Date;
import java.util.List;

public interface QuittanceDetailsDao extends GenericDao<QuittanceDetails, Long> {
	
	List<QuittanceDetails> findByEircAccountAndDateTill(Long id, Date dateFrom, Date dateTill);

    List<QuittanceDetails> findByQuittanceId(Long id);
}