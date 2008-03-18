package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestImportService extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testGetConnection();
//		testImportDistricts();
//		testImportStreetTypes();
//		testImportStreets();
//		testImportBuildings();
//		testImportApartments();
//		testImportPersons();
	}

	public void testGetConnection() {

		JdbcTemplate template = (JdbcTemplate) applicationContext.getBean("dataExportJdbcTemplate");

		assertNotNull("Template is null", template);
		template.execute("SELECT 1 FROM DUAL");
	}

	public void testImportDistricts() throws Throwable {
		getImportService().importDistricts(new Town(1L), new DataSourceDescription(1L));
	}

	public void testImportStreetTypes() {
		getImportService().importStreetTypes(new DataSourceDescription(1L));
	}

	public void testImportStreets() throws Throwable {
		getImportService().importStreets(new Town(1L), new DataSourceDescription(1L));
	}

	public void testImportBuildings() throws Throwable {
		getImportService().importBuildings(new DataSourceDescription(1L));
	}

	public void testImportApartments() throws Throwable {
		getImportService().importApartments(new DataSourceDescription(1L));
	}

	public void testImportPersons() throws Throwable {
		getImportService().importPersons(new DataSourceDescription(1L));
	}

	protected ImportService getImportService() {
		ImportService service = (ImportService) applicationContext.getBean("importServiceAb");
		assertNotNull("ImportService is null", service);
		return service;
	}
}
