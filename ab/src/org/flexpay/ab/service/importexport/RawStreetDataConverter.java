package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Set;
import java.util.HashSet;

public class RawStreetDataConverter implements DataConverter<Street, RawStreetData> {

	/**
	 * Convert raw data to domain object
	 *
	 * @param streetRawData		 RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	public Street fromRawData(RawStreetData streetRawData,
							  DataSourceDescription dataSourceDescription,
							  CorrectionsService correctionsService)
			throws FlexPayException {

		Street street = new Street();
		StreetName streetName = new StreetName();
		streetName.setObject(street);

		StreetNameTranslation nameTranslation = new StreetNameTranslation();
		nameTranslation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
		nameTranslation.setTranslatable(streetName);
		nameTranslation.setName(streetRawData.getName());

		Set<StreetNameTranslation> translations = new HashSet<StreetNameTranslation>();
		translations.add(nameTranslation);
		streetName.setTranslations(translations);

		StreetNameTemporal nameTemporal = new StreetNameTemporal();
		nameTemporal.setObject(street);
		nameTemporal.setValue(streetName);
		street.setNamesTimeLine(new TimeLine<StreetName, StreetNameTemporal>(nameTemporal));

		// TODO add street types support

		return street;
	}
}
