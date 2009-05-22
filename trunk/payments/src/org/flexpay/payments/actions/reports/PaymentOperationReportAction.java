package org.flexpay.payments.actions.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.reporting.ReportUtil;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.reports.payments.PaymentPrintForm;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.util.Map;

public class PaymentOperationReportAction extends FPActionSupport {

	private ReportUtil reportUtil;
	private PaymentsReporter paymentsReporter;

	private Operation operation = new Operation();
	private FPFile report;

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

		if (operation.isNew()) {
			addActionError(getText("error.no_id"));
			return SUCCESS;
		}

		PaymentPrintForm form = paymentsReporter.getPaymentPrintFormData(Stub.stub(operation));
		Map<?, ?> params = map(
				ar("operationDate", "organizationName", "quittanceNumber",
						"cashierFIO", "total", "totalSpelling",
						"inputSumm", "changeSumm"),
				ar(form.getOperationDate(), form.getOrganizationName(), form.getQuittanceNumber(),
						form.getCashierFIO(), form.getTotal(), form.getTotalSpelling(),
						form.getInputSumm(), form.getChangeSumm()));

		JRDataSource dataSource = new JRBeanCollectionDataSource(form.getDetailses());

		String reportName = getReportName(form);
		if (!reportUtil.templateUploaded(reportName)) {
			uploadReport(reportName);
		}

		report = reportUtil.exportToPdf(reportName, params, dataSource);

		return FILE;
	}

	private String getReportName(PaymentPrintForm form) {
		String base = "QuittancePayment";
		String perPointQuittance = base + "_" + form.getPaymentPointStub().getId();
		String resName = "WEB-INF/payments/reports/" + perPointQuittance + ReportUtil.EXTENSION_TEMPLATE;
		if (ApplicationConfig.isResourceAvailable(resName)) {
			return perPointQuittance;
		}

		return base;
	}

	private void uploadReport(String reportName) throws Exception {

		InputStream is = null;
		try {
			String resName = "WEB-INF/payments/reports/" + reportName + ReportUtil.EXTENSION_TEMPLATE;
			is = ApplicationConfig.getResourceAsStream(resName);
			reportUtil.uploadReportTemplate(is, reportName);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public FPFile getReport() {
		return report;
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

	@Required
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	@Required
	public void setPaymentsReporter(PaymentsReporter paymentsReporter) {
		this.paymentsReporter = paymentsReporter;
	}
}
