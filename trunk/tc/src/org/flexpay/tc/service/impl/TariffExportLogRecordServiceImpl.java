package org.flexpay.tc.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.dao.TariffExportLogRecordDao;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.service.TariffExportLogRecordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class TariffExportLogRecordServiceImpl implements TariffExportLogRecordService {

	private TariffExportLogRecordDao tariffExportLogRecordDao;

	/**
	 * Read tariff export record by stub object
	 *
	 * @param tariffExportLogRecordStub stub object
	 * @return tariff export log record
	 */
	@Transactional (readOnly = true)
	public TariffExportLogRecord read(@NotNull Stub<TariffExportLogRecord> tariffExportLogRecordStub) {
		return tariffExportLogRecordDao.read(tariffExportLogRecordStub.getId());
	}

	/**
	 * Save tariff export log record
	 *
	 * @param tariffExportLogRecord TariffExportLogRecord
	 */
	@Transactional (readOnly = false)
	public void create(@NotNull TariffExportLogRecord tariffExportLogRecord) {
		tariffExportLogRecord.setId(null);
		tariffExportLogRecordDao.create(tariffExportLogRecord);
	}

	/**
	 * Save tariff export log record
	 *
	 * @param tariffExportLogRecord TariffExportLogRecord
	 */
	@Transactional (readOnly = false)
	public void update(@NotNull TariffExportLogRecord tariffExportLogRecord) {
		tariffExportLogRecordDao.update(tariffExportLogRecord);
	}

	/**
	 * Delete Tariff export log record
	 *
	 * @param tariffExportLogRecord tariffExportLogRecord
	 */
	@Transactional (readOnly = false)
	public void delete(@NotNull TariffExportLogRecord tariffExportLogRecord) {
		tariffExportLogRecordDao.delete(tariffExportLogRecord);
	}

	/**
	 * Set tariff export log record dao
	 *
	 * @param tariffExportLogRecordDao Tariff export log record dao
	 */
	@Required
	public void setTariffExportLogRecordDao(@NotNull TariffExportLogRecordDao tariffExportLogRecordDao) {
		this.tariffExportLogRecordDao = tariffExportLogRecordDao;
	}
}
