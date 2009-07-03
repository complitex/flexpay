package org.flexpay.common.service.imp;

import org.flexpay.common.service.ReportPrintHistoryRecordService;
import org.flexpay.common.dao.report.ReportPrintHistoryRecordDao;
import org.flexpay.common.persistence.report.ReportPrintHistoryRecord;
import org.springframework.beans.factory.annotation.Required;

public class ReportPrintHistoryRecordServiceImpl implements ReportPrintHistoryRecordService {

	private ReportPrintHistoryRecordDao reportPrintHistoryRecordDao;

	@Override
	public void addRecord(ReportPrintHistoryRecord record) {
		reportPrintHistoryRecordDao.create(record);
	}

	@Required
	public void setReportPrintHistoryRecordDao(ReportPrintHistoryRecordDao reportPrintHistoryRecordDao) {
		this.reportPrintHistoryRecordDao = reportPrintHistoryRecordDao;
	}
}
