package org.flexpay.payments.actions.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.actions.OperatorAWPActionSupport;
import org.flexpay.payments.reports.payments.PaymentsPrintInfoData;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultReportLocale;
import static org.flexpay.common.util.config.ApplicationConfig.isResourceAvailable;

/**
 * Provides functionality for creating report about payments performed in one day
 * <p/>
 * To specify what kind of payments should be included {@link DayPaymentsReportAction#getPaymentsData(java.util.Date,
 * java.util.Date)} method should be implemented
 * <p/>
 * To specify report JRXML template name {@link DayPaymentsReportAction#getReportName()} method should be implemented
 */
public abstract class DayPaymentsReportAction extends OperatorAWPActionSupport {

	// report parameter names
	private static final String CASHIER_FIO = "cashierFio";
	private static final String CREATION_DATE = "creationDate";
	private static final String BEGIN_DATE = "beginDate";
	private static final String END_DATE = "endDate";
	private static final String PAYMENT_POINT_NAME = "paymentPointName";
	private static final String PAYMENT_POINT_ADDRESS = "paymentPointAddress";
	private static final String PAYMENT_COLLECTOR_ORG_NAME = "paymentCollectorOrgName";

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();
	protected boolean showDetails;
	private String format;
	private FPFile report;

	protected PaymentsReporter paymentsReporter;
	private ReportUtil reportUtil;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			beginDateFilter.setDate(DateUtil.now());
			return SUCCESS;
		}

		Date beginDate = beginTimeFilter.setTime(beginDateFilter.getDate());
		Date endDate = endTimeFilter.setTime(beginDateFilter.getDate());

		PaymentsPrintInfoData data = getPaymentsData(beginDate, endDate);
		data.setCashierFio(getUserPreferences().getFullName());
		Map<?, ?> params = map(
				ar(CASHIER_FIO, CREATION_DATE, BEGIN_DATE, END_DATE, PAYMENT_POINT_NAME, PAYMENT_POINT_ADDRESS, PAYMENT_COLLECTOR_ORG_NAME),
				ar(data.getCashierFio(), data.getCreationDate(), data.getBeginDate(), data.getEndDate(), data.getPaymentPointName(), data.getPaymentPointAddress(), data.getPaymentCollectorOrgName()));
		JRDataSource dataSource = new JRBeanCollectionDataSource(data.getOperationDetailses());

		String reportName = ensureReportTemplateUploaded();
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

	private String ensureReportTemplateUploaded() throws Exception {

		String reportName = getReportName();
		if (!reportUtil.templateUploaded(reportName)) {
			uploadReport(reportName);
		}
		return reportName;
	}

	/**
	 * Returns printable payments report data. To be implemented by concrete classes.
	 *
	 * @param beginDate lower bound for payment date
	 * @param endDate   higher bound
	 * @return printable payments report data
	 */
	protected abstract PaymentsPrintInfoData getPaymentsData(Date beginDate, Date endDate);

	/**
	 * Returns report template name
	 *
	 * @return report template name
	 */
	protected abstract String getReportBaseName();

	protected String getReportName() {

		Long paymentPointId = getPaymentPoint().getId();

		String base = getReportBaseName();
		String perPointQuittance = base + "_" + paymentPointId;
		String resName = "WEB-INF/payments/reports/" + perPointQuittance + ReportUtil.EXTENSION_TEMPLATE;
		if (isResourceAvailable(resName)) {
			return perPointQuittance;
		}

		return base;
	}

	protected Stub<Cashbox> getCashboxStub() {
		return new Stub<Cashbox>(cashboxId);
	}

	protected Cashbox getCashbox() {
		return cashboxService.read(new Stub<Cashbox>(cashboxId));
	}

	protected PaymentPoint getPaymentPoint() {
		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		return paymentPointService.read(cashbox.getPaymentPointStub());
	}

	private void uploadReport(String reportName) throws Exception {

		reportUtil.uploadReportTemplate("WEB-INF/payments/reports/", reportName);
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	// form data
	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public BeginTimeFilter getBeginTimeFilter() {
		return beginTimeFilter;
	}

	public EndTimeFilter getEndTimeFilter() {
		return endTimeFilter;
	}

	public void setBeginTimeFilter(BeginTimeFilter beginTimeFilter) {
		this.beginTimeFilter = beginTimeFilter;
	}

	public void setEndTimeFilter(EndTimeFilter endTimeFilter) {
		this.endTimeFilter = endTimeFilter;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public FPFile getReport() {
		return report;
	}

	public void setFormat(String format) {
		this.format = format;
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
