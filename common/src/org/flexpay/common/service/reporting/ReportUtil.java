package org.flexpay.common.service.reporting;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.JRSwapFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.JDBCUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.hibernate.Session;
import org.jboss.util.file.FilePrefixFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.*;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultReportLocale;

public class ReportUtil {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String EXTENSION_TEMPLATE = ".jrxml";
	private static final String EXTENSION_COMPILED_TEMPLATE = ".jasper";

	private static final String EXTENSION_PDF = ".pdf";
	private static final String EXTENSION_HTML = ".html";
	private static final String EXTENSION_TXT = ".txt";
	private static final String EXTENSION_CSV = ".csv";

	public static final String FORMAT_PDF = "pdf";
	public static final String FORMAT_HTML = "html";
	public static final String FORMAT_TXT = "txt";
	public static final String FORMAT_CSV = "csv";

	/**
	 * Name of fonts that are to
	 */
	private static Set<String> fontNames = set(
            "LiberationMono-Italic",
            "LiberationMono-Regular",
            "LiberationSans-Bold",
            "LiberationSans-BoldItalic",
            "LiberationSans-Italic",
            "LiberationSans-Regular",
            "LiberationSerif-Bold",
            "LiberationSerif-BoldItalic",
            "LiberationSerif-Italic",
            "LiberationSerif-Regular"
    );

	private static final String EXTENSION_FONT_TTF = ".ttf";

	/**
	 * Subreports directory parameter name
	 */
	private static final String PARAM_NAME_SUBREPORTS_DIR = "SUBREPORT_DIR";

	/**
	 * Report locale parameter name
	 */
	private static final String PARAM_NAME_REPORT_LOCALE = "REPORT_LOCALE";

	private static final String RESOURCE_CONNECTION = ReportUtil.class.getName() + "_CONNECTION";

	private EntityManagerFactory entityManagerFactory;
	private DataSource dataSource;

	private FPFileService fileService;

	private String fontsPath = "/WEB-INF/classes/fonts/";

	/**
	 * Upload report and compile it
	 *
	 * @param is   Report source stream
	 * @param name Report name
	 * @throws Exception if failure occurs
	 */
	public void uploadReportTemplate(InputStream is, String name) throws Exception {
		ensureDirsExist();

		@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
		OutputStream os = new FileOutputStream(getTemplateFile(name));

		try {
			// copy report
			IOUtils.copyLarge(is, os);

			ensureReportCompiled(name);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	/**
	 * Upload report and compile it
	 *
	 * @param sourcePath resource name
	 * @param reportName Report name
	 * @throws Exception if failure occurs
	 */
	public void uploadReportTemplate(String sourcePath, String reportName) throws Exception {

		ensureDirsExist();

		uploadTemplateFile(sourcePath, reportName);

		ensureReportCompiled(reportName);
	}

	private void uploadTemplateFile(String sourcePath, String reportName) throws Exception {

		String resName = sourcePath + reportName + ReportUtil.EXTENSION_TEMPLATE;

		InputStream is = ApplicationConfig.getResourceAsStream(resName);
		@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
		OutputStream os = new FileOutputStream(getTemplateFile(reportName));

		try {
			// copy report
			IOUtils.copyLarge(is, os);
		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Check if report was already uploaded
	 *
	 * @param name Report name to check
	 * @return <code>true</code> if report was uploaded, or <code>false</code> otherwise
	 */
	public boolean templateUploaded(String name) {
		return getTemplateFile(name).exists();
	}

	@SuppressWarnings ({"ResultOfMethodCallIgnored"})
	private void ensureDirsExist() {

		File templatesDir = getReportTemplatesDir();
		if (!templatesDir.exists()) {
			templatesDir.mkdirs();
		}

		File reportsDir = getReportsDir();
		if (!reportsDir.exists()) {
			reportsDir.mkdirs();
		}

		File cachesDir = getReportCachesDir();
		if (!cachesDir.exists()) {
			cachesDir.mkdirs();
		}

		File fontsDir = getReportFontsDir();
		if (!fontsDir.exists()) {
			fontsDir.mkdirs();
		}
	}

	/**
	 * Compile report template and save it with a .jasper extension
	 *
	 * @param name Report name to compile
	 * @throws Exception if failure occurs
	 */
	private void ensureReportCompiled(String name) throws Exception {

		if (getCompiledTemplateFile(name).exists()) {
			return;
		}

		// compile report
		JasperCompileManager.compileReportToFile(getTemplatePath(name), getCompiledTemplatePath(name));

		JasperReport report = JasperCompileManager.compileReport(getTemplatePath(name));

		// setup Liberation fonts if they are used
		//TODO: delete this converting fonts path later
//		adjustFontsPath(report);

		// save compiled report 
		JRSaver.saveObject(report, getCompiledTemplatePath(name));
	}

	/**
	 * Export filled report to pdf format
	 *
	 * @param name   Report name
	 * @param params Report parameters
	 * @param source optional data source
	 * @param locale report locale if null default one will be used
	 * @return Result PDF file
	 * @throws Exception if failure occurs
	 */
	public FPFile exportToPdf(String name, @Nullable Map<?, ?> params,
							  @Nullable JRDataSource source, @Nullable Locale locale) throws Exception {

		params = params(params);

		JasperPrint print = fillReport(name, params, source, locale);
		JRPdfExporter exporter = new JRPdfExporter();
		return exportToFile(exporter, print, name + EXTENSION_PDF, params);
	}

	/**
	 * Export filled report to txt format
	 *
	 * @param name   Report name
	 * @param params Report parameters
	 * @param source optional data source
	 * @param locale report locale if null default one will be used
	 * @return Result TXT file
	 * @throws Exception if failure occurs
	 */
	public FPFile exportToTxt(String name, @Nullable Map<?, ?> params,
							  @Nullable JRDataSource source, @Nullable Locale locale) throws Exception {

		params = params(params);

		JasperPrint print = fillReport(name, params, source, locale);
		JRTextExporter exporter = new JRTextExporter();
		return exportToFile(exporter, print, name + EXTENSION_TXT, params);
	}

	/**
	 * Export filled report to html format
	 *
	 * @param name   Report name
	 * @param params Report parameters
	 * @param source optional data source
	 * @param locale report locale if null default one will be used
	 * @return Result HTML file
	 * @throws Exception if failure occurs
	 */
	public FPFile exportToHtml(String name, @Nullable Map<?, ?> params,
							   @Nullable JRDataSource source, @Nullable Locale locale) throws Exception {

		params = params(params);

		JasperPrint print = fillReport(name, params, source, locale);
		JRHtmlExporter exporter = new JRHtmlExporter();
		return exportToFile(exporter, print, name + EXTENSION_HTML, params);
	}

	/**
	 * Export filled report to csv format
	 *
	 * @param name   Report name
	 * @param params Report parameters
	 * @param source optional data source
	 * @param locale report locale if null default one will be used
	 * @return Result CSV file
	 * @throws Exception if failure occurs
	 */
	public FPFile exportToCsv(String name, @Nullable Map<?, ?> params,
							  @Nullable JRDataSource source, @Nullable Locale locale) throws Exception {

		params = params(params);

		JasperPrint print = fillReport(name, params, source, locale);
		JRCsvExporter exporter = new JRCsvExporter();
		return exportToFile(exporter, print, name + EXTENSION_CSV, params);
	}


	private FPFile exportToFile(JRAbstractExporter exporter, JasperPrint jasperPrint, String name, Map<?, ?> params)
			throws Exception {

		FPFile file = new FPFile();
		file.setOriginalName(name);
		file.setModule(fileService.getModuleByName("common"));

		OutputStream os = null;
		try {
			FPFileUtil.createEmptyFile(file);
			os = file.getOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
			if (exporter instanceof JRTextExporter) {
				exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(10));
				exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(5));
			}
			exporter.exportReport();
			fileService.create(file);
		} catch (IOException e) {
			fileService.delete(file);
			throw e;
		} finally {
			IOUtils.closeQuietly(os);
			@SuppressWarnings ({"SuspiciousMethodCalls"})
			JRVirtualizer virtualizer = (JRVirtualizer) params.get(JRParameter.REPORT_VIRTUALIZER);
			virtualizer.cleanup();
		}

		return file;
	}

	/**
	 * Compile and fill report
	 *
	 * @param name		 Report template name
	 * @param parameters   Report parameters
	 * @param jrDataSource optional data source
	 * @param locale	   report locale, if null default locale will be used
	 * @return Filled report
	 * @throws Exception if failure occurs
	 */
	@SuppressWarnings ({"unchecked", "RawUseOfParameterizedType"})
	private JasperPrint fillReport(String name, @NotNull Map parameters,
								   @Nullable JRDataSource jrDataSource, @Nullable Locale locale)
			throws Exception {

		// setting locale parameter
		parameters.put(PARAM_NAME_REPORT_LOCALE, locale != null ? locale : getDefaultReportLocale());

		ensureReportCompiled(name);
		ensureDirsExist();

		// set report virtualizer to prevent OOME generating big reports
		JRSwapFile swap = new JRSwapFile(getReportCachesDir().getAbsolutePath(), 1024, 1024);
		JRAbstractLRUVirtualizer virtualizer = new JRSwapFileVirtualizer(50, swap);
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

		// Load compiled report template
		JasperReport report = (JasperReport) JRLoader.loadObject(getCompiledTemplateFile(name));

		Collection<String> resourceNames = fillParameters(report, parameters);
		try {
			log.debug("Starting report filling: {}", name);
			if (jrDataSource != null) {
				return JasperFillManager.fillReport(report, parameters, jrDataSource);
			} else if (requiresConnection(report)) {
				@SuppressWarnings ({"JDBCResourceOpenedButNotSafelyClosed"})
				Connection c = dataSource.getConnection();
				parameters.put(RESOURCE_CONNECTION, c);
				resourceNames.add(RESOURCE_CONNECTION);
				return JasperFillManager.fillReport(report, parameters, c);
			} else {
				log.debug("Filling without connection");
				return JasperFillManager.fillReport(report, parameters);
			}
		} finally {
			virtualizer.setReadOnly(true);
			cleanup(parameters, resourceNames);

			log.debug("Report filled: {}", name);
		}
	}

	@NotNull
	private Map<?, ?> params(@Nullable Map<?, ?> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			parameters = map();
		}
		return parameters;
	}

	@SuppressWarnings ({"unchecked", "RawUseOfParameterizedType"})
	private Collection<String> fillParameters(JasperReport report, Map parameters) {

		Collection<String> resourceNames = list();

		JRQuery query = report.getQuery();
		if (query != null) {
			log.debug("Found QUERY!");
			if ("hql".equalsIgnoreCase(query.getLanguage())) {
				log.debug("Found hql QUERY!");
				@SuppressWarnings ({"HibernateResourceOpenedButNotSafelyClosed"})
				EntityManager entityManager = entityManagerFactory.createEntityManager();
				parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, entityManager);
				resourceNames.add(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER);

				// TODO: check if it is enough
				report.setProperty(JRJpaQueryExecuterFactory.PROPERTY_JPA_QUERY_PAGE_SIZE, "5000");
			}
		}

		// set dubreports dir
		parameters.put(PARAM_NAME_SUBREPORTS_DIR, getReportTemplatesDir().getAbsolutePath() + "/");

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

	@SuppressWarnings ({"RawUseOfParameterizedType"})
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

	private static File getReportsDir() {
		return new File(ApplicationConfig.getDataRoot(), "reports");
	}

	public static File getReportTemplatesDir() {
		return new File(getReportsDir(), "templates");
	}

	private File getReportCachesDir() {
		return new File(getReportsDir(), "caches");
	}

	private File getReportFontsDir() {
		return new File(getReportsDir(), "fonts");
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
	 * Get path to the font with a given name
	 *
	 * @param name Font name
	 * @return File path to the font
	 */
	private File getFontFile(String name) {
		return new File(getReportFontsDir(), name + EXTENSION_FONT_TTF);
	}

	private String getFontPath(String name) {
		return getFontFile(name).getAbsolutePath();
	}

	private void ensureFontsCopied() throws Exception {

		for (String fontName : fontNames) {
			File fontFile = getFontFile(fontName);

			// file not found, copy it
			if (!fontFile.exists()) {
				String fontResource = fontsPath + fontName + EXTENSION_FONT_TTF;
				InputStream is = ApplicationConfig.getResourceAsStream(fontResource);
				if (is == null) {
					throw new RuntimeException("Font " + fontName + " not found");
				}

				// do copying
				OutputStream os = null;
				try {
					//noinspection IOResourceOpenedButNotSafelyClosed
					os = new BufferedOutputStream(new FileOutputStream(fontFile));

					IOUtils.copyLarge(is, os);
				} catch (IOException ex) {
					log.error("Failed copying font " + fontName, ex);
					throw new Exception("Failed copying font " + fontName, ex);
				} finally {
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(os);
				}
			}
		}
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
		return getCompiledTemplateFile(name).delete();
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
				//noinspection ResultOfMethodCallIgnored
				reportFile.delete();
			}
			++n;
		}

		return n;
	}

	@Required
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Required
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}

}
