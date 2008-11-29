package org.flexpay.eirc.process.quittance;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.quittance.report.JRQuittanceDataSource;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.util.config.ApplicationConfig;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GenerateQuittancesPDFJasperJob extends Job {

	public static final String JOB_NAME = "generateQuitancesPDFJob";

	private QuittanceService quittanceService;
	private ReportUtil reportUtil;
	private JRQuittanceDataSource jrDataSource;

	public final static String RESULT_FILE_NAME = "RESULT_FILE_NAME";
	public static final String PARAM_DATE_FROM = "dateFrom";
	public static final String PARAM_DATE_TILL = "dateTill";
	public static final String PARAM_SERVICE_ORGANIZATION_ID = "serviceOrganizationId";

	public String execute(Map<Serializable, Serializable> contextVariables) throws FlexPayException {

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
			List<Quittance> quittances = quittanceService.getQuittances(
					new Stub<ServiceOrganization>(organizationId), dateFrom, dateTill);

			plog.info("About to prepare JR data source");
			jrDataSource.setQuittances(quittances, 4);
			quittances.clear();

			plog.info("Running report");
			String filledReportName = reportUtil.runReport("Quittance", jrDataSource);

			plog.info("Exporting to PDF");
			File reportPath = reportUtil.exportToPdf(filledReportName);

			contextVariables.put(RESULT_FILE_NAME, reportPath.getAbsolutePath());

			plog.info("Ended PDF quittances generation, time spent: " + (System.currentTimeMillis() - time) + "ms.");
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
			is = ApplicationConfig.getResourceAsStream(resName);
			reportUtil.uploadReportTemplate(is, name);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static Set<String> getParameterNames() {
		return CollectionUtils.set(PARAM_DATE_FROM, PARAM_DATE_TILL, PARAM_SERVICE_ORGANIZATION_ID);
	}

	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	public void setJrDataSource(JRQuittanceDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}
}