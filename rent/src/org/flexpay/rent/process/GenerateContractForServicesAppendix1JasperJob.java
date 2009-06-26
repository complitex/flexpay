package org.flexpay.rent.process;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.rent.reports.contract.ContractForServicesAppendix1Form;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class GenerateContractForServicesAppendix1JasperJob extends Job {

	public static final String JOB_NAME = "generateContractForServicesAppendix1Job";

	public static final String RESULT_FILE_ID = "RESULT_FILE_ID";
	public static final String PARAM_DATA_FORM = "dataForm";
	public static final String PARAM_GENERATE_FORMAT = "format";

	private ReportUtil reportUtil;

	public String execute(Map<Serializable, Serializable> contextVariables) throws FlexPayException {

		ContractForServicesAppendix1Form dataForm = (ContractForServicesAppendix1Form) contextVariables.get(PARAM_DATA_FORM);
		String format = (String) contextVariables.get(PARAM_GENERATE_FORMAT);

		Logger plog = ProcessLogger.getLogger(getClass());

		try {
			long time = System.currentTimeMillis();
			plog.info("Starting contract for services appendix1 generation, format: {}", format);

			// upload report and subreports templates
			plog.info("Uploading report template");
			uploadReportTemplates();

			JRDataSource dataSource = new JREmptyDataSource(1);

			plog.info("Running report");

			FPFile report = null;
			if (ReportUtil.FORMAT_PDF.equals(format)) {
				report = reportUtil.exportToPdf("ContractForServicesAppendix1", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
			} else if (ReportUtil.FORMAT_HTML.equals(format)) {
				report = reportUtil.exportToHtml("ContractForServicesAppendix1", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
			} else if (ReportUtil.FORMAT_CSV.equals(format)) {
				report = reportUtil.exportToCsv("ContractForServicesAppendix1", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
			} else {
				throw new FlexPayException("Incorrect format value - " +  format);
			}
			if (report != null) {
				contextVariables.put(RESULT_FILE_ID, report.getId());
			}

			plog.info("Ended jasper contract for services appendix1 generation for format {}, time spent: {} ms.", format, System.currentTimeMillis() - time);
		} catch (Exception e) {
			contextVariables.put(Job.STATUS_ERROR, "Error : " + e.getMessage());
			plog.error("Error", e);
			return Job.RESULT_ERROR;
		}

		return Job.RESULT_NEXT;
	}

	private void uploadReportTemplates() throws Exception {
		uploadReportTemplate("ContractForServicesAppendix1");
	}

	private void uploadReportTemplate(String name) throws Exception {
		InputStream is = null;
		try {
			String resName = "WEB-INF/rent/reports/" + name + ReportUtil.EXTENSION_TEMPLATE;
			is = ApplicationConfig.getResourceAsStream(resName);
			reportUtil.uploadReportTemplate(is, name);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static Set<String> getParameterNames() {
		return CollectionUtils.set(PARAM_DATA_FORM);
	}

	@Required
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

}
