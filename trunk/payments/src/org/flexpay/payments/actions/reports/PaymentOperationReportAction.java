package org.flexpay.payments.actions.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.report.ReportPrintHistoryRecord;
import org.flexpay.common.persistence.report.ReportType;
import org.flexpay.common.service.ReportPrintHistoryRecordService;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.actions.PaymentOperationAction;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.reports.payments.PaymentPrintForm;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultReportLocale;

public class PaymentOperationReportAction extends PaymentOperationAction {

	private static final String REPORT_BASE_NAME = "DoubleQuittancePayment";
    private static final String REPORT_NAME_TXT_SUFFIX = "Txt";

	private Long operationId;

	private Boolean copy = false;
	private String format;

	private FPFile report;

	private ReportUtil reportUtil;
	private PaymentsReporter paymentsReporter;
	private ReportPrintHistoryRecordService reportPrintHistoryRecordService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (operationId == null || operationId <= 0) {
			log.warn("Incorrect operation id {}", operationId);
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		Operation op = operationService.read(new Stub<Operation>(operationId));
		if (!copy) {
			fillOperation(op);
		}

		PaymentPrintForm form = paymentsReporter.getPaymentPrintFormData(op);
		if (form == null) {
			log.warn("Can't get payment form data from DB for operation {}", op);
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		if (copy) {
			form.setQuittanceNumber(form.getQuittanceNumber() + " " + getText("payments.quittance.copy"));
		}

		addPrintHistoryRecord();

		Map<String, Object> params = map(
				ar("operationDate", "organizationName", "quittanceNumber",
						"cashierFIO", "payerFIO", "total", "totalSpelling",
						"inputSumm", "changeSumm", "paymentPointName",
						"paymentPointAddress", "detailses", "digest"),
				ar(form.getOperationDate(), form.getOrganizationName(), form.getQuittanceNumber(),
						form.getCashierFIO(), form.getPayerFIO(), form.getTotal(), form.getTotalSpelling(),
						form.getInputSumm(), form.getChangeSumm(), form.getPaymentPointName(),
						form.getPaymentPointAddress(), form.getDetailses(), form.getDigestValue()));

		JRDataSource dataSource = new JRBeanCollectionDataSource(form.getDetailses());

        String suf = ReportUtil.FORMAT_TXT.equals(format) ? REPORT_NAME_TXT_SUFFIX : "";
		String paymentPointSuffix = getPaymentPointSuffix(form, suf);
		String reportName = REPORT_BASE_NAME + suf + paymentPointSuffix;

		if (!reportUtil.templateUploaded(reportName)) {
			uploadReportTemplates(paymentPointSuffix, suf);
		}

		if (ReportUtil.FORMAT_PDF.equals(format)) {
            log.debug("Exporting {} report. Format: PDF", reportName);
			report = reportUtil.exportToPdf(reportName, params, dataSource, getDefaultReportLocale());
		} else if (ReportUtil.FORMAT_HTML.equals(format)) {
            log.debug("Exporting {} report. Format: HTML", reportName);
			report = reportUtil.exportToHtml(reportName, params, dataSource, getDefaultReportLocale());
		} else if (ReportUtil.FORMAT_CSV.equals(format)) {
            log.debug("Exporting {} report. Format: CSV", reportName);
			report = reportUtil.exportToCsv(reportName, params, dataSource, getDefaultReportLocale());
		} else if (ReportUtil.FORMAT_TXT.equals(format)) {
            log.debug("Exporting {} report. Format: TXT", reportName);
			report = reportUtil.exportToTxt(reportName, params, dataSource, getDefaultReportLocale());
		} else {
			log.warn("Unknown print format {}", format);
			return REDIRECT_ERROR;
		}

		return FILE;
	}

	private Long addPrintHistoryRecord() {
		ReportPrintHistoryRecord record = new ReportPrintHistoryRecord();
		record.setPrintDate(new Date());
		record.setReportType(ReportType.OPERATION_REPORT);
		record.setUserName(getUserPreferences().getUsername());
		reportPrintHistoryRecordService.addRecord(record);
		return record.getId();
	}

	private String getPaymentPointSuffix(PaymentPrintForm form, String formatSuffix) {
		
		String perPointQuittance = REPORT_BASE_NAME + formatSuffix + "_" + form.getPaymentPointStub().getId();
		String resName = "WEB-INF/payments/reports/" + perPointQuittance + ReportUtil.EXTENSION_TEMPLATE;
		if (ApplicationConfig.isResourceAvailable(resName)) {
			return "_" + form.getPaymentPointStub().getId();
		}

		return "";
	}

	private void uploadReportTemplates(String paymentPointSuffix, String formatSuffix) throws Exception {
		uploadReportTemplate("DoubleQuittancePayment" + formatSuffix + paymentPointSuffix);
		uploadReportTemplate("QuittancePayment" + formatSuffix + paymentPointSuffix);
	}

	private void uploadReportTemplate(String reportName) throws Exception {

		InputStream is = null;
		try {
			String resName = "WEB-INF/payments/reports/" + reportName + ReportUtil.EXTENSION_TEMPLATE;
			is = ApplicationConfig.getResourceAsStream(resName);
			reportUtil.uploadReportTemplate(is, reportName);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	public void setCopy(Boolean copy) {
		this.copy = copy;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public FPFile getReport() {
		return report;
	}

	@Required
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	@Required
	public void setPaymentsReporter(PaymentsReporter paymentsReporter) {
		this.paymentsReporter = paymentsReporter;
	}

	@Required
	public void setReportPrintHistoryRecordService(ReportPrintHistoryRecordService reportPrintHistoryRecordService) {
		this.reportPrintHistoryRecordService = reportPrintHistoryRecordService;
	}
}
