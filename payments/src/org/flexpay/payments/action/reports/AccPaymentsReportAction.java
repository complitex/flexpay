package org.flexpay.payments.action.reports;

import net.sf.jasperreports.engine.JRDataSource;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.reports.payments.AccReportData;
import org.flexpay.payments.reports.payments.AccReportRequest;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultReportLocale;

public abstract class AccPaymentsReportAction extends AccountantAWPActionSupport {

	protected static final String PAYMENTS_SUFFIX = "_payments";
	protected static final String CASHBOXES_SUFFIX = "_cashboxes";

    private static final String BEGIN_DATE = "beginDate";
    private static final String END_DATE = "endDate";
    private static final String CREATION_DATE = "creationDate";
    private static final String PAYMENT_COLLECTOR_ORG_ADDRESS = "paymentCollectorOrgAddress";
    private static final String PAYMENT_COLLECTOR_ORG_NAME = "paymentCollectorOrgName";
    private static final String ACCOUNTANT_FIO = "accountantFio";

	protected BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.now());
	protected EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());
	protected BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	protected EndTimeFilter endTimeFilter = new EndTimeFilter();

	private FPFile report;

	private String format;

	private ReportUtil reportUtil;
	protected PaymentsReporter paymentsReporter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			initFilters();
			return SUCCESS;
		}

		Long paymentCollectorId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId();
		if (paymentCollectorId == null) {
			log.error("No payment collector id found in user preferences");
			addActionError("payments.error.payment_collector_not_found");
			return SUCCESS;
		}

        AccReportRequest request = buildReportRequest();

		AccReportData data = paymentsReporter.getAccPaymentsReportData(request);
		data.setAccountantFio(getUserPreferences().getFullName());

		Map<?, ?> params = map(
				ar(BEGIN_DATE, END_DATE, CREATION_DATE, PAYMENT_COLLECTOR_ORG_ADDRESS, PAYMENT_COLLECTOR_ORG_NAME, ACCOUNTANT_FIO),
				ar(data.getBeginDate(), data.getEndDate(), data.getCreationDate(), data.getPaymentCollectorOrgAddress(), data.getPaymentCollectorOrgName(), data.getAccountantFio()));
		JRDataSource dataSource = data.getDataSource();

		String reportName = ensureReportTemplateUploaded(request);
		if (ReportUtil.FORMAT_PDF.equals(format)) {
			report = reportUtil.exportToPdf(reportName, params, dataSource, getDefaultReportLocale());
		} else if (ReportUtil.FORMAT_HTML.equals(format)) {
			report = reportUtil.exportToHtml(reportName, params, dataSource, getDefaultReportLocale());
		} else if (ReportUtil.FORMAT_CSV.equals(format)) {
			report = reportUtil.exportToCsv(reportName, params, dataSource, getDefaultReportLocale());
		} else if (ReportUtil.FORMAT_TXT.equals(format)) {
			report = reportUtil.exportToTxt(reportName, params, dataSource, getDefaultReportLocale());
		} else {
			return SUCCESS;
		}

		return FILE;
	}

	private String ensureReportTemplateUploaded(AccReportRequest request) throws Exception {

		String reportName = getReportName(request);
		if (!reportUtil.templateUploaded(reportName)) {
			uploadReport(reportName);
			// upload additional report files if neccessary
            uploadAdditionalReportFiles(request);
		}

		return reportName;
	}

    protected abstract void uploadAdditionalReportFiles(AccReportRequest request) throws Exception;

	protected void uploadReport(String reportName) throws Exception {
		reportUtil.uploadReportTemplate("WEB-INF/payments/reports/", reportName);
	}

	protected Cashbox getCashbox() {
		//return cashboxService.read(new Stub<Cashbox>(cashboxId));
		// TODO: FIXME
		return null;
	}

	/**
	 * Returns report template name
	 *
	 * @param request request
	 * @return report template name
	 */
	protected abstract String getReportName(AccReportRequest request);

	protected abstract AccReportRequest buildReportRequest();

	@SuppressWarnings ({"unchecked"})
	protected abstract void initFilters();

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	public BeginTimeFilter getBeginTimeFilter() {
		return beginTimeFilter;
	}

	public void setBeginTimeFilter(BeginTimeFilter beginTimeFilter) {
		this.beginTimeFilter = beginTimeFilter;
	}

	public EndTimeFilter getEndTimeFilter() {
		return endTimeFilter;
	}

	public void setEndTimeFilter(EndTimeFilter endTimeFilter) {
		this.endTimeFilter = endTimeFilter;
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
}
