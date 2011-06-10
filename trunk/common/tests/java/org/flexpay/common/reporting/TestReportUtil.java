package org.flexpay.common.reporting;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;

import java.io.InputStream;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;

public class TestReportUtil extends SpringBeanAwareTestCase {

	@Autowired
	private ReportUtil reportUtil;
	@Autowired
	private FPFileService fileService;

	private DataSourceDescription sourceDescription;

	@Test (expected = Exception.class)
	public void testFillNotExistingReport() throws Exception {
		String reportName = "_NOT_EXISTING_REPORT_";
		FPFile file = reportUtil.exportToHtml(reportName, null, null, null);
		fileService.delete(file);
	}

	/**
	 * Test JasperReporting with a HQL query. It works rather slow, so ignored by default
	 *
	 * @throws Exception if failure occurs
	 */
	@Test
	public void testHqlReport() throws Exception {
		InputStream is = getFileStream("org/flexpay/common/reporting/sample_report_hql.jrxml");
		String reportName = "sample_report_hql";

		try {
			reportUtil.uploadReportTemplate(is, reportName);
			FPFile file = reportUtil.exportToCsv(reportName,
					map(
							ar("dataSourceId", "dataSourceDescr"),
							ar(sourceDescription.getId(), sourceDescription.getDescription())), null, null);
			fileService.delete(file);
		} finally {
			IOUtils.closeQuietly(is);
			reportUtil.deleteAll(reportName);
		}
	}

	@Test
	public void testSqlReport() throws Exception {
		InputStream is = getFileStream("org/flexpay/common/reporting/sample_report_sql.jrxml");
		String reportName = "sample_report_sql";

		try {
			reportUtil.uploadReportTemplate(is, reportName);
			FPFile file = reportUtil.exportToPdf(reportName,
					map(
							ar("dataSourceId", "dataSourceDescr"),
							ar(sourceDescription.getId(), sourceDescription.getDescription())), null, null);
			fileService.delete(file);
		} finally {
			IOUtils.closeQuietly(is);
			reportUtil.deleteAll(reportName);
		}
	}

	@Before
	public void before() {
		// find data source description for CN
		String dscr = "Источник - Тестовые данные ПУ из ЦН";
		sourceDescription = (DataSourceDescription) DataAccessUtils.uniqueResult(jpaTemplate.find(
				"from DataSourceDescription where description=?", dscr));
		if (sourceDescription == null) {
			throw new RuntimeException("Cannot find data source description: " + dscr);
		}
	}
}
