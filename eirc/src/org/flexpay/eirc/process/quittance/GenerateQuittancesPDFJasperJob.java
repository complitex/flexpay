package org.flexpay.eirc.process.quittance;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.quittance.report.JRQuittanceDataSource;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.util.config.ApplicationConfig;

import java.io.InputStream;
import java.io.Serializable;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class GenerateQuittancesPDFJasperJob extends Job {

	private QuittanceService quittanceService;
	private ReportUtil reportUtil;
	private JRQuittanceDataSource jrDataSource;

	public final static String RESULT_FILE_NAME = "RESULT_FILE_NAME";

	public String execute(Map<Serializable, Serializable> contextVariables) throws FlexPayException {

		Long serviceOrganisationId = (Long) contextVariables.get("serviceOrganisationId");
		Date dateFrom = (Date) contextVariables.get("dateFrom");
		Date dateTill = (Date) contextVariables.get("dateTill");

		try {
			long time = System.currentTimeMillis();
			log.info("Starting PDF quittances generation");

			List<Quittance> quittances = quittanceService.getQuittances(
					new Stub<ServiceOrganisation>(serviceOrganisationId), dateFrom, dateTill);

			// upload report and subreports templates
			uploadReportTemplates();

			jrDataSource.setQuittances(quittances);
			String filledReportName = reportUtil.runReport("Quittance", jrDataSource);
			File reportPath = reportUtil.exportToPdf(filledReportName);

			contextVariables.put(RESULT_FILE_NAME, reportPath.getAbsolutePath());

			log.info("Ended PDF quittances generation, time spent: " + (System.currentTimeMillis() - time) + "ms.");
		} catch (Exception e) {
			contextVariables.put(Job.STATUS_ERROR, "Error : " + e.getMessage());
			log.error("Error", e);
			return Job.RESULT_ERROR;
		}

		return Job.RESULT_NEXT;
	}

	private void uploadReportTemplates() throws Exception {
		uploadReportTemplate("Quittance");
		uploadReportTemplate("services");
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