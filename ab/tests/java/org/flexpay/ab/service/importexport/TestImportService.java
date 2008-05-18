package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertNotNull;

public class TestImportService extends SpringBeanAwareTestCase {

	protected ImportService service;
	protected JdbcTemplate template;

	@Autowired
	public void setService(@Qualifier("importServiceAb") ImportService service) {
		this.service = service;
	}

	@Autowired
	public void setTemplate(@Qualifier("dataExportJdbcTemplate") JdbcTemplate template) {
		this.template = template;
	}

	@Test
	public void testGetConnection() {

		assertNotNull("Template is null", template);
		template.execute("SELECT 1 FROM DUAL");
	}

	@Test
	@Ignore
	public void testImportDistricts() throws Throwable {
		getImportService().importDistricts(new Town(1L), new DataSourceDescription(1L));
	}

	@Test
	@Ignore
	public void testImportStreetTypes() {
		getImportService().importStreetTypes(new DataSourceDescription(1L));
	}

	@Test
	@Ignore
	public void testImportStreets() throws Throwable {
		getImportService().importStreets(new Town(1L), new DataSourceDescription(1L));
	}

	@Test
	@Ignore
	public void testImportBuildings() throws Throwable {
		getImportService().importBuildings(new DataSourceDescription(1L));
	}

	@Test
	@Ignore
	public void testImportApartments() throws Throwable {
		getImportService().importApartments(new DataSourceDescription(1L));
	}

	@Test
	@Ignore
	public void testImportPersons() throws Throwable {
		getImportService().importPersons(new DataSourceDescription(1L));
	}

	protected ImportService getImportService() {
		ImportService service = (ImportService) applicationContext.getBean("importServiceAb");
		assertNotNull("ImportService is null", service);
		return service;
	}
}
