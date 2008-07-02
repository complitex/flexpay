package org.flexpay.common.service.reporting;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.util.JDBCUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.util.file.FilePrefixFilter;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.util.*;

public class ReportUtil {

	private Logger log = Logger.getLogger(getClass());

	private static final String EXTENSION_TEMPLATE = ".jrxml";
	private static final String EXTENSION_COMPILED_TEMPLATE = ".jasper";
	private static final String EXTENSION_REPORT = ".jprint";

	private static final String EXTENSION_PDF = ".pdf";
	private static final String EXTENSION_TXT = ".txt";
	private static final String EXTENSION_XML = ".xml";
	private static final String EXTENSION_HTML = ".html";
	private static final String EXTENSION_CSV = ".csv";

	private static final String RESOURCE_CONNECTION = ReportUtil.class.getName() + "_CONNECTION";

	private SessionFactory sessionFactory;
	private DataSource dataSource;

	private Set<String> compiledReports = new HashSet<String>();

	/**
	 * Upload report
	 *
	 * @param is   Report source stream
	 * @param name Report name
	 * @throws IOException if failure occurs
	 */
	public void uploadReportTemplate(InputStream is, String name) throws IOException {
		ensureDirsExist();
		OutputStream os = new FileOutputStream(getTemplateFile(name));

		try {
			// copy report
			IOUtils.copyLarge(is, os);

			// invalidate compiled report version
			compiledReports.remove(name);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	private void ensureDirsExist() {
		File templatesDir = getReportTemplatesDir();
		if (!templatesDir.exists()) {
			templatesDir.mkdirs();
		}

		File reportsDir = getReportsDir();
		if (!reportsDir.exists()) {
			reportsDir.mkdirs();
		}
	}

	/**
	 * Compile report template and save it with a .jasper extension
	 *
	 * @param name Report name to compile
	 * @throws JRException if failure occurs
	 */
	private void ensureReportCompiled(String name) throws JRException {
		if (compiledReports.contains(name)) {
			return;
		}

		// compile report
		JasperCompileManager.compileReportToFile(
				getTemplatePath(name), getCompiledTemplatePath(name));

		// mark template compiled, no need to compile it again
		compiledReports.add(name);
	}

	/**
	 * Export filled report to pdf format
	 *
	 * @param name Report name
	 * @return Result PDF file
	 * @throws Exception if failure occurs
	 */
	public File exportToPdf(String name) throws Exception {

		JRPdfExporter exporter = new JRPdfExporter();
		return exportToFile(exporter, name, EXTENSION_PDF);
	}

	/**
	 * Export filled report to xml format
	 *
	 * @param name Report name
	 * @return Result XML file
	 * @throws Exception if failure occurs
	 */
	public File exportToXml(String name) throws Exception {

		JRXmlExporter exporter = new JRXmlExporter();
		return exportToFile(exporter, name, EXTENSION_XML);
	}

	/**
	 * Export filled report to html format
	 *
	 * @param name Report name
	 * @return Result HTML file
	 * @throws Exception if failure occurs
	 */
	public File exportToHtml(String name) throws Exception {

		JRHtmlExporter exporter = new JRHtmlExporter();
		return exportToFile(exporter, name, EXTENSION_HTML);
	}

	/**
	 * Export filled report to csv format
	 *
	 * @param name Report name
	 * @return Result CSV file
	 * @throws Exception if failure occurs
	 */
	public File exportToCsv(String name) throws Exception {

		JRCsvExporter exporter = new JRCsvExporter();
		return exportToFile(exporter, name, EXTENSION_CSV);
	}

	/**
	 * Export filled report to plain text
	 *
	 * @param name Report name
	 * @return Result TXT file
	 * @throws Exception if failure occurs
	 */
	public File exportToTxt(String name) throws Exception {

		JRTextExporter exporter = new JRTextExporter();
		exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 10);
		exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 10);

		return exportToFile(exporter, name, EXTENSION_TXT);
	}

	private File exportToFile(JRAbstractExporter exporter, String name, String ext) throws Exception {
		JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(getReportFile(name));
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, getReportPath(name, ext));
		exporter.exportReport();

		return getReportFile(name, ext);

	}

	/**
	 * Compile and fill report
	 *
	 * @param name	   Report template name
	 * @param parameters Report parameters
	 * @return Filled report name
	 * @throws Exception if failure occurs
	 */
	@SuppressWarnings({"unchecked"})
	public String runReport(String name, Map parameters) throws Exception {

		if (parameters == null || parameters == Collections.emptyMap()) {
			parameters = new HashMap();
		}

		ensureReportCompiled(name);

		// Timestamp should be enough to avoid report names collisions
		String reportName = name + "-" + System.currentTimeMillis();

		// Load compiled report template
		JasperReport report = (JasperReport) JRLoader.loadObject(getCompiledTemplateFile(name));
		Collection<String> resourceNames = fillParameters(report, parameters);
		try {
			if (requiresConnection(report)) {
				Connection c = dataSource.getConnection();
				parameters.put(RESOURCE_CONNECTION, c);
				resourceNames.add(RESOURCE_CONNECTION);
				JasperFillManager.fillReportToFile(report, getReportPath(reportName), parameters, c);
			} else {
				log.debug("Filling without connection");
				JasperFillManager.fillReportToFile(report, getReportPath(reportName), parameters);
			}
			return reportName;
		} finally {
			cleanup(parameters, resourceNames);
		}
	}

	@SuppressWarnings({"unchecked"})
	private Collection<String> fillParameters(JasperReport report, Map parameters) {

		Collection<String> resourceNames = new ArrayList<String>();

		JRQuery query = report.getQuery();
		if (query != null) {
			log.debug("Found QUERY!");
			if ("hql".equalsIgnoreCase(query.getLanguage())) {
				log.debug("Found hql QUERY!");
				Session session = sessionFactory.openSession();
				parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
				resourceNames.add(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION);

				// TODO: check if it is enough
				report.setProperty(JRHibernateQueryExecuterFactory.PROPERTY_HIBERNATE_QUERY_LIST_PAGE_SIZE, "5000");
				report.setProperty(JRHibernateQueryExecuterFactory.PROPERTY_HIBERNATE_QUERY_RUN_TYPE, "list");
				report.setProperty(JRHibernateQueryExecuterFactory.PROPERTY_HIBERNATE_CLEAR_CACHE, "true");
			}
		}

		// Hope there is no subreports, todo check it

		return resourceNames;
	}

	/**
	 * Check if report requires Connection to run
	 *
	 * @param report JasperReport
	 * @return <code>true</code> if Connection required, or <code>false</otherwise>
	 */
	private boolean requiresConnection(JRReport report) {
		return report.getQuery() != null && "sql".equalsIgnoreCase(report.getQuery().getLanguage());
	}

	private void cleanup(Map parameters, Collection<String> resourceNames) {
		for (String resourceName : resourceNames) {
			Object resource = parameters.get(resourceName);
			parameters.remove(resourceName);
			if (resource instanceof Session) {
				((Session) resource).close();
			} else if (resource instanceof Connection) {
				JDBCUtils.closeQuitly((Connection) resource);
			}
		}
	}

	private File getReportsDir() {
		ApplicationConfig config = ApplicationConfig.getInstance();
		return new File(config.getDataRoot(), "reports");
	}

	private File getReportTemplatesDir() {
		return new File(getReportsDir(), "templates");
	}

	/**
	 * Get path to the report template with a given name
	 *
	 * @param name Report name
	 * @return File path to the template
	 */
	public File getTemplateFile(String name) {
		return new File(getReportTemplatesDir(), name + EXTENSION_TEMPLATE);
	}

	private String getTemplatePath(String name) {
		return getTemplateFile(name).getAbsolutePath();
	}

	/**
	 * Get path to the compiled report template with a given name
	 *
	 * @param name Report name
	 * @return File path to the compiled template
	 */
	public File getCompiledTemplateFile(String name) {
		return new File(getReportTemplatesDir(), name + EXTENSION_COMPILED_TEMPLATE);
	}

	private String getCompiledTemplatePath(String name) {
		return getCompiledTemplateFile(name).getAbsolutePath();
	}

	/**
	 * Get path to the compiled report template with a given name
	 *
	 * @param name Report name
	 * @return File path to the compiled template
	 */
	public File getReportFile(String name) {
		return new File(getReportsDir(), name + EXTENSION_REPORT);
	}

	private String getReportPath(String name) {
		return getReportFile(name).getAbsolutePath();
	}

	/**
	 * Get path to the compiled report template with a given name
	 *
	 * @param name	  Report name
	 * @param extension File name extension
	 * @return File path to the compiled template
	 */
	public File getReportFile(String name, String extension) {
		return new File(getReportsDir(), name + extension);
	}

	private String getReportPath(String name, String extension) {
		return getReportFile(name, extension).getAbsolutePath();
	}

	/**
	 * Delete template file
	 *
	 * @param name Report name
	 * @return <code>true</code> if file was deleted, or <code>false</code> otherwise
	 */
	public boolean deleteTemplate(String name) {
		return getTemplateFile(name).delete();
	}

	/**
	 * Delete compiled template file
	 *
	 * @param name Report name
	 * @return <code>true</code> if file was deleted, or <code>false</code> otherwise
	 */
	public boolean deleteCompiledTemplate(String name) {
		compiledReports.remove(name);
		return getCompiledTemplateFile(name).delete();
	}

	/**
	 * Delete filled report
	 *
	 * @param name Report name
	 * @return <code>true</code> if file was deleted, or <code>false</code> otherwise
	 */
	public boolean deleteReport(String name) {
		return getReportFile(name).delete();
	}

	/**
	 * Delete both template and compiled template files
	 *
	 * @param name Report name
	 * @return number of deleted files
	 */
	public int deleteTemplates(String name) {
		int n = 0;
		if (deleteTemplate(name)) {
			++n;
		}
		if (deleteCompiledTemplate(name)) {
			++n;
		}
		return n;
	}

	/**
	 * Delete templates and all filled reports
	 *
	 * @param name Report name
	 * @return number of deleted files
	 * @throws Exception if deletion fails
	 */
	public int deleteAll(String name) throws Exception {
		int n = deleteTemplates(name);

		File reportsDir = getReportsDir();
		File[] files = reportsDir.listFiles(new FilePrefixFilter(name));
		for (File reportFile : files) {
			if (reportFile.isDirectory()) {
				FileUtils.deleteDirectory(reportFile);
			} else {
				reportFile.delete();
			}
			++n;
		}

		return n;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
