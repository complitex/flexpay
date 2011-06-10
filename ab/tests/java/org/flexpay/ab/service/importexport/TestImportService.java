package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DataSourceDescription;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.flexpay.common.persistence.Stub.stub;

public class TestImportService extends AbSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("importServiceAb")
	protected ImportService importService;
	@Autowired
	@Qualifier ("dataExportJdbcTemplate")
	protected JdbcTemplate template;

	private DataSourceDescription sourceDescription;
	private Town town;

	@Test
	public void testImportDistricts() throws Throwable {
		importService.importDistricts(town, sourceDescription);
	}

	@Test
	@Ignore
	public void testImportStreetTypes() {
		importService.importStreetTypes(sourceDescription);
	}

	@Test
	@Ignore
	public void testImportStreets() throws Throwable {
		importService.importStreets(town, sourceDescription);
	}

	@Test
	@Ignore
	public void testImportBuildings() throws Throwable {
		importService.importBuildings(sourceDescription);
	}

	@Test
	@Ignore
	public void testImportApartments() throws Throwable {
		importService.importApartments(sourceDescription);
	}

	@Test
	@Ignore
	public void testImportPersons() throws Throwable {
		importService.importPersons(stub(sourceDescription));
	}

	@Before
	public void beforeClass() throws Exception {
		// find data source description for CN
		// see init_db for 'magic' description
		sourceDescription = (DataSourceDescription) DataAccessUtils.uniqueResult(jpaTemplate.find(
				"from DataSourceDescription where description='Источник - Тестовые данные ПУ из ЦН'"));

		if (sourceDescription == null) {
			throw new RuntimeException("Data source description no found");
		}

		town = ApplicationConfig.getDefaultTown();
	}
}
