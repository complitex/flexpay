package org.flexpay.ab.util.standalone;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.standalone.StandaloneTask;

public class ImportTask implements StandaloneTask {

	private ImportService importService;
	private Stub<DataSourceDescription> sourceDescription;
	private Town town;

	/**
	 * Execute task
	 */
	public void execute() {
		try {
			importService.importDistricts(town, new DataSourceDescription(sourceDescription));
			importService.importStreetTypes(new DataSourceDescription(sourceDescription));
			importService.importStreets(town, new DataSourceDescription(sourceDescription));
			importService.importBuildings(new DataSourceDescription(sourceDescription));
			importService.importApartments(new DataSourceDescription(sourceDescription));
			importService.importPersons(sourceDescription);
		} catch (Exception e) {
			throw new RuntimeException("Failed importing objects", e);
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
		this.sourceDescription = stub(sourceDescription);
	}

	public void setTown(Town town) {
		this.town = town;
	}
}
