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
import org.flexpay.rent.reports.contract.ContractForServicesForm;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class GenerateContractForServicesJasperJob extends Job {

	public static final String JOB_NAME = "generateContractForServicesJob";

	public static final String RESULT_FILE_ID = "RESULT_FILE_ID";
	public static final String PARAM_DATA_FORM = "dataForm";
	public static final String PARAM_DATA_FORM1 = "dataForm1";
	public static final String PARAM_GENERATE_FORMAT = "format";

	private ReportUtil reportUtil;

	public String execute(Map<Serializable, Serializable> contextVariables) throws FlexPayException {

		ContractForServicesForm dataForm = (ContractForServicesForm) contextVariables.get(PARAM_DATA_FORM);
		ContractForServicesAppendix1Form dataForm1 = (ContractForServicesAppendix1Form) contextVariables.get(PARAM_DATA_FORM1);
		String format = (String) contextVariables.get(PARAM_GENERATE_FORMAT);

		Logger plog = ProcessLogger.getLogger(getClass());

		try {
			long time = System.currentTimeMillis();
			plog.info("Starting contract for services generation, format: {}", format);

			// upload report and subreports templates
			plog.info("Uploading report template");
			uploadReportTemplates();

			JRDataSource dataSource = new JREmptyDataSource();

			plog.info("Running report");

			FPFile report = null;
			if (ReportUtil.FORMAT_PDF.equals(format)) {
				reportUtil.exportToPdf("ContractForServices", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToPdf("ContractForServicesAppendix1", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToPdf("ContractForServicesAppendix2", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToPdf("ContractForServicesAppendix3", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToPdf("ContractForServicesAppendix4", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToPdf("ContractForServicesAppendix5", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToPdf("ContractForServicesAppendix6", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToPdf("ContractForServicesAppendix7", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
			} else if (ReportUtil.FORMAT_HTML.equals(format)) {
				reportUtil.exportToHtml("ContractForServices", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToHtml("ContractForServicesAppendix1", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToHtml("ContractForServicesAppendix2", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToHtml("ContractForServicesAppendix3", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToHtml("ContractForServicesAppendix4", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToHtml("ContractForServicesAppendix5", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToHtml("ContractForServicesAppendix6", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToHtml("ContractForServicesAppendix7", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
			} else if (ReportUtil.FORMAT_CSV.equals(format)) {
				reportUtil.exportToCsv("ContractForServices", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToCsv("ContractForServicesAppendix1", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToCsv("ContractForServicesAppendix2", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToCsv("ContractForServicesAppendix3", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToCsv("ContractForServicesAppendix4", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToCsv("ContractForServicesAppendix5", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToCsv("ContractForServicesAppendix6", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
				reportUtil.exportToCsv("ContractForServicesAppendix7", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());
			} else {
				throw new FlexPayException("Incorrect format value - " +  format);
			}

			contextVariables.put(GenerateContractForServicesJasperJob.RESULT_FILE_ID, "1");

			plog.info("Ended jasper contract for services generation for format {}, time spent: {} ms.", format, System.currentTimeMillis() - time);
		} catch (Exception e) {
			contextVariables.put(Job.STATUS_ERROR, "Error : " + e.getMessage());
			plog.error("Error", e);
			return Job.RESULT_ERROR;
		}

		return Job.RESULT_NEXT;
	}

	private void uploadReportTemplates() throws Exception {
		uploadReportTemplate("ContractForServices");
		uploadReportTemplate("ContractForServicesAppendix1");
		uploadReportTemplate("ContractForServicesAppendix2");
		uploadReportTemplate("ContractForServicesAppendix3");
		uploadReportTemplate("ContractForServicesAppendix3Sub");
		uploadReportTemplate("ContractForServicesAppendix3");
		uploadReportTemplate("ContractForServicesAppendix4");
		uploadReportTemplate("ContractForServicesAppendix5");
		uploadReportTemplate("ContractForServicesAppendix6");
		uploadReportTemplate("ContractForServicesAppendix7");
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
