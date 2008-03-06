package org.flexpay.eirc.util.standalone;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.service.importexport.EircImportService;
import org.flexpay.eirc.dao.importexport.RawPersonalAccountDataSource;

/**
 * Standalone task for importing personal accounts
 */
public class AccountsImportTask implements StandaloneTask {

	private EircImportService importService;
	private Town town;
	private DataSourceDescription dataSourceDescription;
	private RawPersonalAccountDataSource dataSource;

	/**
	 * Setter for property 'importService'.
	 *
	 * @param importService Value to set for property 'importService'.
	 */
	public void setImportService(EircImportService importService) {
		this.importService = importService;
	}

	/**
	 * Setter for property 'town'.
	 *
	 * @param town Value to set for property 'town'.
	 */
	public void setTown(Town town) {
		this.town = town;
	}

	/**
	 * Setter for property 'dataSourceDescription'.
	 *
	 * @param dataSourceDescription Value to set for property 'dataSourceDescription'.
	 */
	public void setDataSourceDescription(DataSourceDescription dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	/**
	 * Setter for property 'dataSource'.
	 *
	 * @param dataSource Value to set for property 'dataSource'.
	 */
	public void setDataSource(RawPersonalAccountDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Execute task
	 */
	public void execute() {
		try {
			importService.importPersonalAccounts(town, dataSourceDescription, dataSource);
		} catch (FlexPayException t) {
			throw new RuntimeException(t);
		}
	}
}
