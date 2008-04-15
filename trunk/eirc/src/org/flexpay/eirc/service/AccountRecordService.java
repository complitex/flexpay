package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.AccountRecord;
import org.flexpay.eirc.persistence.AbstractConsumer;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

public interface AccountRecordService {

	/**
	 * Create new AccountRecord
	 *
	 * @param recordStub stub of the record
	 * @return persisted AccountRecord
	 */
	AccountRecord create(AccountRecord recordStub);

	/**
	 * Find registered account record for stub,
	 *
	 * @param recordStub AccountRecord
	 * @return list of registered records
	 * @throws FlexPayException if more than one record registered
	 */
	AccountRecord findRegisteredRecord(AccountRecord recordStub) throws FlexPayException;

	/**
	 * Calculate current consumer balance
	 *
	 * @param consumer Consumer instance
	 * @return current consumer balance
	 */
	BigDecimal getCurrentBalance(AbstractConsumer consumer);

	/**
	 * Calculate current consumer balance
	 *
	 * @param consumer Consumer instance
	 * @param date Date to calculate balance
	 * @return current consumer balance
	 */
	BigDecimal getBalanceForDate(AbstractConsumer consumer, Date date);

	/**
	 * Find AccountRecord for Person
	 *
	 * @param personId Person key
	 * @param apartmentId Apartment identifier
	 * @return List of AccountRecord sorted by ServiceType and operationDate
	 */
	List<AccountRecord> findForTicket(Long personId, Long apartmentId);
}
