package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.NotTransactional;

public class TestImportService extends SpringBeanAwareTestCase {

	protected ImportService importService;
	protected JdbcTemplate template;

	private DataSourceDescription sourceDescription;
	private Town town;

	@Autowired
	public void setService(@Qualifier ("importServiceAb")ImportService service) {
		this.importService = service;
	}

	@Autowired
	public void setTemplate(@Qualifier ("dataExportJdbcTemplate")JdbcTemplate template) {
		this.template = template;
	}

	@Test
	@NotTransactional
	public void testImportDistricts() throws Throwable {
		importService.importDistricts(town, sourceDescription);
	}

	@Test
	@Ignore
	@NotTransactional
	public void testImportStreetTypes() {
		importService.importStreetTypes(sourceDescription);
	}

	@Test
	@Ignore
	@NotTransactional
	public void testImportStreets() throws Throwable {
		importService.importStreets(town, sourceDescription);
	}

	@Test
	@Ignore
	@NotTransactional
	public void testImportBuildings() throws Throwable {
		importService.importBuildings(sourceDescription);
	}

	@Test
	@Ignore
	@NotTransactional
	public void testImportApartments() throws Throwable {
		importService.importApartments(sourceDescription);
	}

	@Test
	@Ignore
	@NotTransactional
	public void testImportPersons() throws Throwable {
		importService.importPersons(sourceDescription);
	}

	@Before
	public void beforeClass() throws Exception {
		// find data source description for CN
		// see init_db for 'magic' description
		sourceDescription = (DataSourceDescription) DataAccessUtils.uniqueResult(hibernateTemplate.find(
				"from DataSourceDescription where description='Источник - Тестовые данные ПУ из ЦН'"));

		if (sourceDescription == null) {
			throw new RuntimeException("Data source description no found");
		}

		town = ApplicationConfig.getDefaultTown();
	}
}
