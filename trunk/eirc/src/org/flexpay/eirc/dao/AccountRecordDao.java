package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.AccountRecord;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public interface AccountRecordDao extends GenericDao<AccountRecord, Long> {

	/**
	 * Find registered records
	 *
	 * @param page Page object
	 * @param organisationId Organisation id
	 * @param consumerId Consumer id
	 * @param operationDate Operation date
	 * @param amount Operation money amount
	 * @param recordTypeId AccountRecordType id
	 * @return list of AccountRecords
	 */
	List<AccountRecord> findRegisteredRecords(
			Page page, Long organisationId, Long consumerId, Date operationDate, BigDecimal amount, Long recordTypeId);
	
	/**
	 * Find AccountRecord for Person
	 *
	 * @param personId Person key
	 * @return List of AccountRecord sorted by ServiceType and operationDate
	 */
	List<AccountRecord> findForTicket(Long personId, Long apartmentId);
}