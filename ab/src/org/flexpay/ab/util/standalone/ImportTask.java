package org.flexpay.ab.util.standalone;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ImportTask implements StandaloneTask {

	private ImportService importService;
	private Stub<DataSourceDescription> sourceDescription;
	private Town town;

	/**
	 * Execute task
	 */
    @Override
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

    @Required
	public void setImportService(ImportService importService) {
		this.importService = importService;
	}

	public void setSourceDescription(DataSourceDescription sourceDescription) {
		this.sourceDescription = stub(sourceDescription);
	}

	public void setTown(Town town) {
		this.town = town;
	}
}
