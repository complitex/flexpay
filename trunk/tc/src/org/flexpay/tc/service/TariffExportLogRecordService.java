package org.flexpay.tc.service;

import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.common.persistence.Stub;
import org.springframework.security.annotation.Secured;
import org.jetbrains.annotations.NotNull;

public interface TariffExportLogRecordService {

	/**
	 * Read tariff export record by stub object
	 * @param tariffExportLogRecordStub stub object
	 * @return tariff export log record
	 */
	@Secured (Roles.TARIFF_CALCULATION_RULES_FILE_READ)
	public TariffExportLogRecord read(@NotNull Stub<TariffExportLogRecord> tariffExportLogRecordStub);

	/**
	 * Save tariff export log record
	 * @param tariffExportLogRecord TariffExportLogRecord
	 */
	@Secured (Roles.TARIFF_EXPORT_LOG_RECORD_ADD)
	public void save(@NotNull TariffExportLogRecord tariffExportLogRecord);

	/**
	 * Delete Tariff export log record
	 * @param tariffExportLogRecord tariffExportLogRecord
	 */
	@Secured(Roles.TARIFF_EXPORT_LOG_RECORD_DELETE)
	public void delete(@NotNull TariffExportLogRecord tariffExportLogRecord);

}
