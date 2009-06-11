package org.flexpay.payments.actions.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.actions.PaymentOperationAction;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.reports.payments.PaymentPrintForm;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.service.*;
import org.flexpay.payments.util.ServiceFullIndexUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class PaymentOperationReportAction extends PaymentOperationAction {

	private static final String REPORT_BASE_NAME = "DoubleQuittancePayment";

	private Long operationId;

	private FPFile report;

	private ReportUtil reportUtil;
	private PaymentsReporter paymentsReporter;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		PaymentPrintForm form;

		if (operationId != null) {
			form = paymentsReporter.getPaymentPrintFormData(new Stub<Operation>(operationId));
		} else {
			Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
			if (cashbox == null) {
				log.warn("Can't find cashbox with id {}", cashboxId);
				addActionError(getText("payments.errors.cashbox_id_is_bad", "" + cashboxId));
				return SUCCESS;
			}
			log.debug("Found cashbox {}", cashbox);

			Operation operation = createOperation(cashbox);
			form = paymentsReporter.getPaymentPrintFormData(operation);
		}

		if (form == null) {
			addActionError(getText("error.no_id"));
			return SUCCESS;
		}

		Map<String, Object> params = map(
				ar("operationDate", "organizationName", "quittanceNumber",
						"cashierFIO", "payerFIO", "total", "totalSpelling",
						"inputSumm", "changeSumm", "paymentPointAddress", "detailses"),
				ar(form.getOperationDate(), form.getOrganizationName(), form.getQuittanceNumber(),
						form.getCashierFIO(), form.getPayerFIO(), form.getTotal(), form.getTotalSpelling(),
						form.getInputSumm(), form.getChangeSumm(), form.getPaymentPointAddress(), form.getDetailses()));

		JRDataSource dataSource = new JRBeanCollectionDataSource(form.getDetailses());

		String paymentPointSuffix = getPaymentPointSuffix(form);
		String reportName = REPORT_BASE_NAME + paymentPointSuffix;

		if (!reportUtil.templateUploaded(reportName)) {
			uploadReportTemplates(paymentPointSuffix);
		}

		report = reportUtil.exportToPdf(reportName, params, dataSource);

		return FILE;
	}

	private String getPaymentPointSuffix(PaymentPrintForm form) {
		
		String perPointQuittance = REPORT_BASE_NAME + "_" + form.getPaymentPointStub().getId();
		String resName = "WEB-INF/payments/reports/" + perPointQuittance + ReportUtil.EXTENSION_TEMPLATE;
		if (ApplicationConfig.isResourceAvailable(resName)) {
			return "_" + form.getPaymentPointStub().getId();
		}

		return "";
	}

	private void uploadReportTemplates(String paymentPointSuffix) throws Exception {
		uploadReportTemplate("DoubleQuittancePayment" + paymentPointSuffix);
		uploadReportTemplate("QuittancePayment" + paymentPointSuffix);
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
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
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
