package org.flexpay.tc.process.exporters;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffExportCodeServiceExt;
import org.flexpay.tc.service.TariffExportLogRecordService;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class FileCNExporter implements Exporter {

	private PrintStream exportPrintStream;
	private final static String exportFileNamePrefix = "CNExportFile_";
	private TariffCalculationResultService tariffCalculationResultService;
	private TariffExportCodeServiceExt tariffExportCodeServiceExt;
	private TariffExportLogRecordService tariffExportLogRecordService;

	/**
	 * Begin export procedure
	 *
	 * @throws FlexPayException throws flexpay exception when filed to begin export process
	 */
	@Override
	public void beginExport() throws FlexPayException {
		try {
			File f = File.createTempFile(exportFileNamePrefix, "", ApplicationConfig.getTcDataRoot());
			exportPrintStream = new PrintStream(f);
		} catch (FileNotFoundException e) {
			throw new FlexPayException("Can't find file for output.");
		} catch (IOException e) {
			throw new FlexPayException("Can't create export file");
		}
	}

	/**
	 * Export parameters
	 *
	 * @param params params to export
	 * @throws FlexPayException throws flexpay exception when can't export data
	 */
	@Override
	public void export(@NotNull Object[] params) throws FlexPayException {

		if (exportPrintStream == null) {
			throw new FlexPayException("File Exporter is not ready");
		}

		for (Object o : params) {
			exportPrintStream.print(o);
			exportPrintStream.print(" : ");
		}
		TariffCalculationResult tariffCalculationResult = (TariffCalculationResult) params[0];
		TariffExportLogRecord tariffExportLogRecord = new TariffExportLogRecord();
		tariffExportLogRecord.setBuilding(tariffCalculationResult.getBuilding());
		tariffExportLogRecord.setTariff(tariffCalculationResult.getTariff());
		tariffExportLogRecord.setTariffBeginDate((java.util.Date) params[1]);
		tariffExportLogRecord.setTariffExportCode(tariffExportCodeServiceExt.findByCode(TariffExportCode.EXPORTED));
		tariffExportLogRecord.setExportdate(new java.util.Date());

		tariffExportLogRecordService.create(tariffExportLogRecord);

		if (tariffCalculationResult.getId() != null) {
			tariffCalculationResult.setLastTariffExportLogRecord(tariffExportLogRecord);
			tariffCalculationResultService.update(tariffCalculationResult);
		}
		exportPrintStream.println();
	}

	/**
	 * Commit transaction
	 *
	 * @throws FlexPayException throws flexpay exception when filed to commit transaction
	 */
	@Override
	public void commit() throws FlexPayException {
		if (exportPrintStream == null) {
			throw new FlexPayException("File Exporter is not ready");
		} else {
			exportPrintStream.flush();
		}
	}

	/**
	 * Rollback transaction
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          throws FlexPayException when can't rollback transaction
	 */
	@Override
	public void rollback() throws FlexPayException {
		//do nothing
	}

	/**
	 * End export procedure
	 */
	@Override
	public void endExport() {
		if (exportPrintStream != null) {
			exportPrintStream.flush();
			IOUtils.closeQuietly(exportPrintStream);
		}
	}

	@Required
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	@Required
	public void setTariffExportCodeService(TariffExportCodeServiceExt tariffExportCodeServiceExt) {
		this.tariffExportCodeServiceExt = tariffExportCodeServiceExt;
	}

	@Required
	public void setTariffExportLogRecordService(TariffExportLogRecordService tariffExportLogRecordService) {
		this.tariffExportLogRecordService = tariffExportLogRecordService;
	}

}
