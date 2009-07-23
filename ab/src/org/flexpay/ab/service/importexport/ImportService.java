package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;

public interface ImportService {

	public void importDistricts(Town town, DataSourceDescription sourceDescription)
			throws FlexPayException;

	public void importStreets(Town town, DataSourceDescription sourceDescription)
			throws FlexPayException;

	/**
	 * Import street types only builds corrections and does not add any new street types to the system, use User Interface,
	 * or plain SQL to add new types.
	 *
	 * @param sourceDescription Data source description
	 */
	void importStreetTypes(DataSourceDescription sourceDescription);

	void importBuildings(DataSourceDescription sourceDescription) throws Exception;

	void importApartments(DataSourceDescription sourceDescription) throws Exception;

	void importPersons(Stub<DataSourceDescription> sd) throws Exception;
}
