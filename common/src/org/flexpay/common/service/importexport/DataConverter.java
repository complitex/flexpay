package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.exception.FlexPayException;

/**
 * Converter between Domain objects and external raw data
 */
public interface DataConverter<Obj extends DomainObject, ObjData extends RawData<Obj>> {

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawData RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	public Obj fromRawData(
			ObjData rawData, DataSourceDescription dataSourceDescription,
			CorrectionsService correctionsService) throws FlexPayException;
}
