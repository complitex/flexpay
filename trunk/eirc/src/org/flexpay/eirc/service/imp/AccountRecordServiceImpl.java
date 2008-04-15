package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.dao.AccountRecordDao;
import org.flexpay.eirc.persistence.AbstractConsumer;
import org.flexpay.eirc.persistence.AccountRecord;
import org.flexpay.eirc.service.AccountRecordService;
import org.flexpay.eirc.service.IllegalRecordsStateException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class AccountRecordServiceImpl implements AccountRecordService {

	private AccountRecordDao accountRecordDao;

	/**
	 * Create new AccountRecord
	 *
	 * @param recordStub stub of the record
	 * @return persisted AccountRecord
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public AccountRecord create(AccountRecord recordStub) {
		accountRecordDao.create(recordStub);
		return recordStub;
	}

	/**
	 * Find registered account record for stub
	 *
	 * @param stub AccountRecord
	 * @return list of registered records
	 * @throws FlexPayException if more than one record registered
	 */
	@Transactional (readOnly = true, rollbackFor = Exception.class)
	public AccountRecord findRegisteredRecord(AccountRecord stub) throws FlexPayException {

		List<AccountRecord> registeredRecords = accountRecordDao.findRegisteredRecords(
				new Page(1, 1), stub.getOrganisation().getId(), stub.getConsumer().getId(),
				stub.getOperationDate(), stub.getAmount(),
				stub.getRecordType().getId());
		if (registeredRecords.isEmpty()) {
			return null;
		}

		AccountRecord first = registeredRecords.get(0);
		if (registeredRecords.size() > 1) {
			AccountRecord second = registeredRecords.get(1);
			throw new IllegalRecordsStateException("More than one identical account record exists: " +
												   first + ", " + second);
		}

		return first;
	}

	/**
	 * Calculate current consumer balance
	 *
	 * @param consumer Consumer instance
	 * @return current consumer balance
	 */
	public BigDecimal getCurrentBalance(AbstractConsumer consumer) {
		return getBalanceForDate(consumer, new Date());
	}

	/**
	 * Calculate current consumer balance
	 *
	 * @param consumer Consumer instance
	 * @param date	 Date to calculate balance
	 * @return current consumer balance
	 */
	public BigDecimal getBalanceForDate(AbstractConsumer consumer, Date date) {
		List<BigDecimal> balance = accountRecordDao.findBalanceForDate(consumer.getId(), date);
		return balance.get(0) == null ? BigDecimal.ZERO : balance.get(0);
	}

	/**
	 * Find AccountRecord for Person
	 *
	 * @param personId Person key
	 * @return List of AccountRecord sorted by ServiceType and operationDate
	 */
	public List<AccountRecord> findForTicket(Long personId, Long apartmentId) {
		return accountRecordDao.findForTicket(personId, apartmentId);
	}

	public void setAccountRecordDao(AccountRecordDao accountRecordDao) {
		this.accountRecordDao = accountRecordDao;
	}
}
