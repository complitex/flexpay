package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestImportService extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testGetConnection();
		testImportDistricts();
		testImportStreetTypes();
		testImportStreets();
		testImportBuildings();
	}

	public void testGetConnection() {

		JdbcTemplate template = (JdbcTemplate) applicationContext.getBean("dataExportJdbcTemplate");

		assertNotNull("Template is null", template);
		template.execute("SELECT 1 FROM DUAL");
	}

	public void testImportDistricts() throws Throwable {
		ImportService service = (ImportService) applicationContext.getBean("importService");
		assertNotNull("ImportService is null", service);

		Town town = new Town(1L);
		DataSourceDescription dsd = new DataSourceDescription(1L);

		service.importDistricts(town, dsd);
	}

	public void testImportStreetTypes() {
		ImportService service = (ImportService) applicationContext.getBean("importService");
		assertNotNull("ImportService is null", service);

		DataSourceDescription dsd = new DataSourceDescription(1L);

		service.importStreetTypes(dsd);
	}

	public void testImportStreets() throws Throwable {
		ImportService service = (ImportService) applicationContext.getBean("importService");
		assertNotNull("ImportService is null", service);

		Town town = new Town(1L);
		DataSourceDescription dsd = new DataSourceDescription(1L);

		service.importStreets(town, dsd);
	}

	public void testImportBuildings() throws Throwable {
		ImportService service = (ImportService) applicationContext.getBean("importService");
		assertNotNull("ImportService is null", service);

		DataSourceDescription dsd = new DataSourceDescription(1L);

		service.importBuildings(dsd);
	}
}
