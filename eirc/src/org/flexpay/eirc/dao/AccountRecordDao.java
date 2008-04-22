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
	 * Calculate consumer balance for date
	 *
	 * @param consumerId AbstractConsumer identifier
	 * @param date Date to calculate balance
	 * @return Consumer Balance
	 */
	List<BigDecimal> findBalanceForDate(Long consumerId, Date date);

	/**
	 * Calculate sum of AccountRecord.amount by Person, Apartment and with operationDate before date-parameter 
	 *
	 * @param personId Person key
	 * @param apartmentId Apartment key
	 * @param date Date 
	 * @return List of Object[]. First array element of type Long and contain service_id
	 */
	List<Object[]> findCalculateServiceAmount(Long personId, Long apartmentId, Date date);
}