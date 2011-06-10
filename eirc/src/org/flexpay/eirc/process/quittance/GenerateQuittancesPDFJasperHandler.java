package org.flexpay.eirc.process.quittance;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.process.quittance.report.JRQuittanceDataSource;
import org.flexpay.eirc.reports.quittance.QuittancePrintInfoData;
import org.flexpay.eirc.reports.quittance.QuittanceReporter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultReportLocale;
import static org.flexpay.common.util.config.ApplicationConfig.getResourceAsStream;

public class GenerateQuittancesPDFJasperHandler extends TaskHandler {

	public final static String RESULT_FILE_ID = "RESULT_FILE_ID";
	public static final String PARAM_DATE_FROM = "dateFrom";
	public static final String PARAM_DATE_TILL = "dateTill";
	public static final String PARAM_SERVICE_ORGANIZATION_ID = "serviceOrganizationId";

	private ReportUtil reportUtil;
	private JRQuittanceDataSource jrDataSource;
	private QuittanceReporter quittanceReporter;

	@Override
	public String execute(Map<String, Object> contextVariables) throws FlexPayException {

		Date dateFrom = (Date) contextVariables.get(PARAM_DATE_FROM);
		Date dateTill = (Date) contextVariables.get(PARAM_DATE_TILL);
		Long organizationId = (Long) contextVariables.get(PARAM_SERVICE_ORGANIZATION_ID);

		Logger plog = ProcessLogger.getLogger(getClass());

		try {
			long time = System.currentTimeMillis();
			plog.info("Starting PDF quittances generation");

			// upload report and subreports templates
			plog.info("Uploading report template");
			uploadReportTemplates();

			plog.info("Fetching quittances");
			QuittancePrintInfoData printInfoData = quittanceReporter.getPrintData(
					new Stub<EircServiceOrganization>(organizationId), dateFrom, dateTill);

			plog.info("About to prepare JR data source");
			jrDataSource.setPrintData(printInfoData, 4);

			plog.info("Running report");
			FPFile report = reportUtil.exportToPdf("Quittance", null, jrDataSource, getDefaultReportLocale());

			contextVariables.put(RESULT_FILE_ID, report.getId());

			if (plog.isInfoEnabled()) {
				plog.info("Ended PDF quittances generation, time spent: {} ms.", System.currentTimeMillis() - time);
			}
		} catch (Exception e) {
			contextVariables.put(Job.STATUS_ERROR, "Error : " + e.getMessage());
			plog.error("Error", e);
			return Job.RESULT_ERROR;
		}

		return Job.RESULT_NEXT;
	}

	private void uploadReportTemplates() throws Exception {
		uploadReportTemplate("Quittance");
		uploadReportTemplate("services");
		uploadReportTemplate("services_details");
		uploadReportTemplate("services_left");
		uploadReportTemplate("subservices");
	}

	private void uploadReportTemplate(String name) throws Exception {
		InputStream is = null;
		try {
			String resName = "WEB-INF/eirc/reports/quittance/" + name + ReportUtil.EXTENSION_TEMPLATE;
			is = getResourceAsStream(resName);
			reportUtil.uploadReportTemplate(is, name);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static Set<String> getParameterNames() {
		return CollectionUtils.set(PARAM_DATE_FROM, PARAM_DATE_TILL, PARAM_SERVICE_ORGANIZATION_ID);
	}

	@Required
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	@Required
	public void setJrDataSource(JRQuittanceDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}

	@Required
	public void setQuittanceReporter(QuittanceReporter quittanceReporter) {
		this.quittanceReporter = quittanceReporter;
	}
}
