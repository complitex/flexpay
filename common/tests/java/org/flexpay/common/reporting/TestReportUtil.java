package org.flexpay.common.reporting;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.test.annotation.NotTransactional;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;

public class TestReportUtil extends SpringBeanAwareTestCase {

	@Autowired
	private ReportUtil reportUtil;

	private DataSourceDescription sourceDescription;

	@Test(expected = Exception.class)
	public void testFillNotExistingReport() throws Exception {
		reportUtil.runReport("_NOT_EXISTING_REPORT_", Collections.emptyMap());
	}

	/**
	 * Test JasperReporting with a HQL query. It works rather slow, so ignored by default
	 *
	 * @throws Exception if failure occurs
	 */
	@Test
	@Ignore
	@NotTransactional
	public void testHqlReport() throws Exception {
		InputStream is = getFileStream("org/flexpay/common/reporting/sample_report_hql.jrxml");
		String reportName = "sample_report_hql";

		try {
			reportUtil.uploadReportTemplate(is, reportName);
			String repName = reportUtil.runReport(reportName,
					map(
							ar("dataSourceId", "dataSourceDescr"),
							ar(sourceDescription.getId(), sourceDescription.getDescription())));
			reportUtil.exportToTxt(repName);
		} finally {
			IOUtils.closeQuietly(is);
			reportUtil.deleteAll(reportName);
		}
	}

	@Test
	@NotTransactional
	public void testSqlReport() throws Exception {
		InputStream is = getFileStream("org/flexpay/common/reporting/sample_report_sql.jrxml");
		String reportName = "sample_report_sql";

		try {
			reportUtil.uploadReportTemplate(is, reportName);
			String repName = reportUtil.runReport(reportName,
					map(
							ar("dataSourceId", "dataSourceDescr"),
							ar(sourceDescription.getId(), sourceDescription.getDescription())));

			reportUtil.exportToPdf(repName);
			reportUtil.exportToTxt(repName);
			reportUtil.exportToXml(repName);
			reportUtil.exportToCsv(repName);
			reportUtil.exportToHtml(repName);
		} finally {
			IOUtils.closeQuietly(is);
//			reportUtil.deleteAll(reportName);
		}
	}

	@Before
	public void before() {
		// find data source description for CN
		sourceDescription = (DataSourceDescription) DataAccessUtils.uniqueResult(hibernateTemplate.find(
				"from DataSourceDescription where description='Источник - Тестовые данные ПУ из ЦН'"));
		if (sourceDescription == null) {
			throw new RuntimeException("Cannot find data source description");
		}
	}
}
