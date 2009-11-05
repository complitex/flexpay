package org.flexpay.tc.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.dao.TariffExportLogRecordDao;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.service.TariffExportLogRecordService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class TariffExportLogRecordServiceImpl implements TariffExportLogRecordService {

	private TariffExportLogRecordDao tariffExportLogRecordDao;

	/**
	 * Read tariff export record by stub object
	 *
	 * @param tariffExportLogRecordStub stub object
	 * @return tariff export log record
	 */
	@Transactional (readOnly = true)
	@Override
	public TariffExportLogRecord read(@NotNull Stub<TariffExportLogRecord> tariffExportLogRecordStub) {
		return tariffExportLogRecordDao.read(tariffExportLogRecordStub.getId());
	}

	/**
	 * Save tariff export log record
	 *
	 * @param tariffExportLogRecord TariffExportLogRecord
	 */
	@Transactional (readOnly = false)
	@Override
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
	@Override
	public void update(@NotNull TariffExportLogRecord tariffExportLogRecord) {
		tariffExportLogRecordDao.update(tariffExportLogRecord);
	}

	/**
	 * Delete Tariff export log record
	 *
	 * @param tariffExportLogRecord tariffExportLogRecord
	 */
	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull TariffExportLogRecord tariffExportLogRecord) {
		tariffExportLogRecordDao.delete(tariffExportLogRecord);
	}

	@Required
	public void setTariffExportLogRecordDao(@NotNull TariffExportLogRecordDao tariffExportLogRecordDao) {
		this.tariffExportLogRecordDao = tariffExportLogRecordDao;
	}

}
