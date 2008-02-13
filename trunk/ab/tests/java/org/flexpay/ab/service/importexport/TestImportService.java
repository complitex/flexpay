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
		testImportStreetTypes();
	}

	public void testGetConnection() {

		JdbcTemplate template = (JdbcTemplate) applicationContext.getBean("dataExportJdbcTemplate");

		assertNotNull("Template is null", template);
		template.execute("SELECT 1 FROM DUAL");
	}

	public void testImportDistricts() {
		ImportService service = (ImportService) applicationContext.getBean("importService");
		assertNotNull("ImportService is null", service);

		Town town = new Town();
		town.setId(1L);

		DataSourceDescription dsd = new DataSourceDescription();
		dsd.setId(1L);

		service.importDistricts(town, dsd);
	}

	public void testImportStreetTypes() {
		ImportService service = (ImportService) applicationContext.getBean("importService");
		assertNotNull("ImportService is null", service);

		DataSourceDescription dsd = new DataSourceDescription();
		dsd.setId(1L);

		service.importStreetTypes(dsd);
	}
}
