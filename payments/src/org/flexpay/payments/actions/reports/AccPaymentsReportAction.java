package org.flexpay.payments.actions.reports;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.reports.payments.AccPaymentsReportRequest;
import org.flexpay.payments.reports.payments.AccPaymentReportData;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public abstract class AccPaymentsReportAction extends CashboxCookieActionSupport {

	// detailing options
	private static final int DETAILS_OPTION_PAYMENT_POINT = 1;
	private static final int DETAILS_OPTION_CASHBOX = 2;
	private static final int DETAILS_OPTION_PAYMENT = 3;

	private static final String PAYMENTS_SUFFIX = "_payments";
	private static final String CASHBOXES_SUFFIX = "_cashboxes";

	// filters
	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.now());
	private EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());
	private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();
	private PaymentPointsFilter paymentPointsFilter = new PaymentPointsFilter();
	private CashboxFilter cashboxFilter = new CashboxFilter();

	// report file
	private FPFile report;

	// form data
	private Integer details;

	// required services
	private PaymentPointService paymentPointService;
	private CashboxService cashboxService;

	private ReportUtil reportUtil;
	protected PaymentsReporter paymentsReporter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			initFilters();
			return SUCCESS;
		}

		AccPaymentsReportRequest request = buildReportRequest();
		AccPaymentReportData data = paymentsReporter.getAccPaymentsReportData(request, getCashbox());

		Map<?, ?> params = map(
				ar("beginDate", "endDate", "creationDate", "paymentPointName", "paymentPointAddress",
						"paymentCollectorOrgName", "accountantFio"),
				ar(data.getBeginDate(), data.getEndDate(), data.getCreationDate(), data.getPaymentPointName(), data.getPaymentPointAddress(),
						data.getPaymentCollectorOrgName(), data.getAccountantFio()));
		JRDataSource dataSource = new JRBeanCollectionDataSource(data.getDetailses());

		String reportName = ensureReportTemplateUploaded(request);
		report = reportUtil.exportToPdf(reportName, params, dataSource, getUserPreferences().getLocale());

		return FILE;
	}

	private String ensureReportTemplateUploaded(AccPaymentsReportRequest request) throws Exception {

		String reportName = getReportName(request);
		//	FIXME set back before commit (disable caching)
		//if (!reportUtil.templateUploaded(reportName)) {

			uploadReport(reportName);
			// upload additional report files if neccessary
			Long paymentPointId = request.getPaymentPointId();
			Long cashboxId = request.getCashboxId();
			switch (request.getDetailsLevel()) {
				case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT_POINT:
					break;
				case AccPaymentsReportRequest.DETAILS_LEVEL_CASHBOX:
					if (paymentPointId == null && cashboxId == null) {						
						uploadReport(reportName + CASHBOXES_SUFFIX);
					} else if (paymentPointId != null && cashboxId == null) {
						uploadReport(reportName + CASHBOXES_SUFFIX);
					}
					break;
				case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT:
					if (paymentPointId == null && cashboxId == null) {
						uploadReport(reportName + CASHBOXES_SUFFIX);
						uploadReport(reportName + PAYMENTS_SUFFIX);
					} else if (paymentPointId != null && cashboxId == null) {
						uploadReport(reportName + PAYMENTS_SUFFIX);
					} else if (paymentPointId != null && cashboxId != null) {
						uploadReport(reportName + PAYMENTS_SUFFIX);
					}
					break;
				default:
					break;
			}
		//}

		return reportName;
	}


	private void uploadReport(String reportName) throws Exception {

		reportUtil.uploadReportTemplate("WEB-INF/payments/reports/", reportName);
	}

	protected Cashbox getCashbox() {
		return cashboxService.read(new Stub<Cashbox>(cashboxId));
	}

	/**
	 * Returns report template name
	 *
	 * @param request request
	 * @return report template name
	 */
	protected abstract String getReportName(AccPaymentsReportRequest request);

	protected abstract int getPaymentStatus();

	private AccPaymentsReportRequest buildReportRequest() {

		AccPaymentsReportRequest request = new AccPaymentsReportRequest();

		request.setDetailsLevel(details);

		if (paymentPointsFilter.needFilter()) {
			request.setPaymentPointId(paymentPointsFilter.getSelectedId());
		}

		if (cashboxFilter.needFilter()) {
			request.setCashboxId(cashboxFilter.getSelectedId());
		}

		request.setBeginDate(beginTimeFilter.setTime(beginDateFilter.getDate()));
		request.setEndDate(endTimeFilter.setTime(endDateFilter.getDate()));
		request.setPaymentStatus(getPaymentStatus());
		request.setLocale(getUserPreferences().getLocale());

		return request;
	}

	private void initFilters() {

		paymentPointsFilter.initFilter(session);
		paymentPointService.initFilter(paymentPointsFilter);

		if (paymentPointsFilter.needFilter()) {
			cashboxFilter.initFilter(session);
			cashboxService.initFilter(CollectionUtils.arrayStack(paymentPointsFilter), cashboxFilter);
		}
	}

	@NotNull
	@Override
	protected String getErrorResult() {

		return SUCCESS;
	}

	// filters
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

	public PaymentPointsFilter getPaymentPointsFilter() {
		return paymentPointsFilter;
	}

	public void setPaymentPointsFilter(PaymentPointsFilter paymentPointsFilter) {
		this.paymentPointsFilter = paymentPointsFilter;
	}

	public CashboxFilter getCashboxFilter() {
		return cashboxFilter;
	}

	public void setCashboxFilter(CashboxFilter cashboxFilter) {
		this.cashboxFilter = cashboxFilter;
	}

	// form data
	public Integer getDetails() {
		return details;
	}

	public void setDetails(Integer details) {
		this.details = details;
	}

	// report file
	public FPFile getReport() {
		return report;
	}

	// required services
	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
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
