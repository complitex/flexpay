package org.flexpay.ab.util.standalone;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.util.standalone.StandaloneTask;

public class ImportTask implements StandaloneTask {

	private ImportService importService;
	private DataSourceDescription sourceDescription;
	private Town town;

	/**
	 * Execute task
	 */
	public void execute() {
		try {
			importService.importDistricts(town, sourceDescription);
			importService.importStreetTypes(sourceDescription);
			importService.importStreets(town, sourceDescription);
			importService.importBuildings(sourceDescription);
			importService.importApartments(sourceDescription);
			importService.importPersons(sourceDescription);
		} catch (Exception e) {
			throw new RuntimeException("Failed importing apartments", e);
		}
	}

	/**
	 * Setter for property 'importService'.
	 *
	 * @param importService Value to set for property 'importService'.
	 */
	public void setImportService(ImportService importService) {
		this.importService = importService;
	}

	/**
	 * Setter for property 'sourceDescription'.
	 *
	 * @param sourceDescription Value to set for property 'sourceDescription'.
	 */
	public void setSourceDescription(DataSourceDescription sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	public void setTown(Town town) {
		this.town = town;
	}
}
