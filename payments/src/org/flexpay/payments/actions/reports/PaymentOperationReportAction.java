package org.flexpay.payments.actions.reports;

import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.reports.payments.PaymentPrintForm;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.ar;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.service.reporting.ReportUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.io.IOUtils;

import java.util.Map;
import java.io.InputStream;
import java.io.IOException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

		if (!reportUtil.templateUploaded("QuittancePayment") || true) {
			uploadReport();
		}

		report = reportUtil.exportToPdf("QuittancePayment", params, dataSource);

		return FILE;
	}

	private void uploadReport() throws Exception {

		InputStream is = null;
		try {
			String name = "QuittancePayment";
			String resName = "WEB-INF/payments/reports/" + name + ReportUtil.EXTENSION_TEMPLATE;
			is = ApplicationConfig.getResourceAsStream(resName);
			reportUtil.uploadReportTemplate(is, name);
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
