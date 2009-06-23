package org.flexpay.rent.process;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.rent.reports.contract.ContractForServicesForm;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenerateContractForServicesPDFJasperJob extends Job {

	public static final String JOB_NAME = "generateContractForServicesPDFJob";

	public final static String RESULT_FILE_ID = "RESULT_FILE_ID";
	public static final String PARAM_DATA_FORM = "dataForm";

	private ReportUtil reportUtil;

	public String execute(Map<Serializable, Serializable> contextVariables) throws FlexPayException {

		ContractForServicesForm dataForm = (ContractForServicesForm) contextVariables.get(PARAM_DATA_FORM);

		Logger plog = ProcessLogger.getLogger(getClass());

		try {
			long time = System.currentTimeMillis();
			plog.info("Starting PDF contract for services generation");

			// upload report and subreports templates
			plog.info("Uploading report template");
			uploadReportTemplates();

			List<Object> ds = new ArrayList<Object>();
			ds.add(dataForm);
			JRDataSource dataSource = new JRBeanCollectionDataSource(ds);

			plog.info("Running report");
			FPFile report = reportUtil.exportToPdf("ContractForServices", dataForm.getParams(), dataSource, ApplicationConfig.getDefaultReportLocale());

			contextVariables.put(RESULT_FILE_ID, report.getId());

			plog.info("Ended PDF contract for services generation, time spent: {} ms.", System.currentTimeMillis() - time);
		} catch (Exception e) {
			contextVariables.put(Job.STATUS_ERROR, "Error : " + e.getMessage());
			plog.error("Error", e);
			return Job.RESULT_ERROR;
		}

		return Job.RESULT_NEXT;
	}

	private void uploadReportTemplates() throws Exception {
		uploadReportTemplate("ContractForServices");
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