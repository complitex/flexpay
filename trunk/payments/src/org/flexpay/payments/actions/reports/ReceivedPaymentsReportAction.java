package org.flexpay.payments.actions.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.service.reporting.ReportUtil;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.reports.payments.ReceivedPaymentsPrintInfoData;
import org.flexpay.payments.service.CashboxService;
import org.flexpay.payments.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Map;

public class ReceivedPaymentsReportAction extends CashboxCookieActionSupport {

	// form data
	private BeginDateFilter beginDateFilter = new BeginDateFilter();

	// report file
	private FPFile report;

	// required services
	private ReportUtil reportUtil;
	private PaymentsReporter paymentsReporter;
	private CashboxService cashboxService;
	private PaymentPointService paymentPointService;

	@NotNull
	protected String doExecute() throws Exception {

		if (isNotSubmit()) {
			beginDateFilter.setDate(DateUtil.now());
			return SUCCESS;
		}

		Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
		Date endDate = DateUtil.getEndOfThisDay(beginDateFilter.getDate());

		ReceivedPaymentsPrintInfoData data = paymentsReporter.
				getReceivedPaymentsPrintFormData(beginDate, endDate, getPaymentPoint(), getLocale());
		Map<?, ?> params = map(
				ar("totalPaymentsCount", "totalPaymentsSumm", "cashierFio", "creationDate",
						"beginDate", "endDate", "paymentPointName", "paymentPointAddress"),
				ar(data.getTotalPaymentsCount(), data.getTotalPaymentsSumm(), data.getCashierFio(),
						data.getCreationDate(), data.getBeginDate(), data.getEndDate(),
						data.getPaymentPointName(), data.getPaymentPointAddress()));

		log.debug("[!!!] data {}", data);
		for (ReceivedPaymentsPrintInfoData.OperationPrintInfo info : data.getOperationDetailses()) {
			log.debug("[!!!] operation {}", info);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(data.getOperationDetailses());

		String reportName = getReportName();
		//if (!reportUtil.templateUploaded(reportName)) {
			uploadReport(reportName);
		//}

		report = reportUtil.exportToPdf(reportName, params, dataSource);

		return FILE;
	}

	@NotNull
	protected String getErrorResult() {

		return SUCCESS;
	}

	private PaymentPoint getPaymentPoint() {
		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		return paymentPointService.read(cashbox.getPaymentPointStub());
	}

	private String getReportName() {

		Long paymentPointId = getPaymentPoint().getId();

		String base = "ReceivedPayments";
		String perPointQuittance = base + "_" + paymentPointId;
		String resName = "WEB-INF/payments/reports/" + perPointQuittance + ReportUtil.EXTENSION_TEMPLATE;
		if (ApplicationConfig.isResourceAvailable(resName)) {
			return perPointQuittance;
		}

		return base;
	}

	private void uploadReport(String reportName) throws Exception {

		reportUtil.uploadReportTemplate("WEB-INF/payments/reports/", reportName);
	}

	// form data
	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public FPFile getReport() {
		return report;
	}

	// required services
	@Required
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	@Required
	public void setPaymentsReporter(PaymentsReporter paymentsReporter) {
		this.paymentsReporter = paymentsReporter;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
