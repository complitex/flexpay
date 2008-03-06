package org.flexpay.eirc.service.importexport;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.importexport.TestImportService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.eirc.dao.importexport.PersonalAccountJdbcDataSource;

public class TestEircImportService extends TestImportService {

	private Logger log = Logger.getLogger(getClass());

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	protected void runTest() throws Throwable {
		super.runTest();
//		testReadRawPersonalAccounts();
//		testImportPersonalAccounts();
	}

	public void testReadRawPersonalAccounts() {
		PersonalAccountJdbcDataSource source = (PersonalAccountJdbcDataSource)
				applicationContext.getBean("personalAccountJdbcDataSource");

		source.initialize();
		int nRecords = 0;
		while (source.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawPersonalAccountData data = source.next(typeHolder);

			if (data != null) {
				assertFalse("Not expected account",
						source.getForbiddenPersons().contains(data.getLastName()));
			}

			if (++nRecords % 1000 == 0) {
				log.info("Accounts read: " + nRecords);
			}
		}
	}

	public void testImportPersonalAccounts() throws FlexPayException {
		EircImportService importService = (EircImportService) applicationContext
				.getBean("importServiceEirc");

		PersonalAccountJdbcDataSource dataSource = (PersonalAccountJdbcDataSource) applicationContext
				.getBean("personalAccountJdbcDataSource");
		importService.importPersonalAccounts(new Town(1L), new DataSourceDescription(1L), dataSource);
	}
}
