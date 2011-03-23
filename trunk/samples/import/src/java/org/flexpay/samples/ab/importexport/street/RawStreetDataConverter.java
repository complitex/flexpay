package org.flexpay.samples.ab.importexport.street;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.HashSet;
import java.util.Set;

public class RawStreetDataConverter implements DataConverter<Street, RawStreetData> {

	/**
	 * Convert raw street data to domain object Street
	 *
	 * @param rawData RawStreetData
	 * @param sd	  Data source description
	 * @param cs	  CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	public Street fromRawData(RawStreetData rawData, DataSourceDescription sd, CorrectionsService cs)
			throws FlexPayException {

		// create new Street object
		Street street = new Street();

		// create new street name
		StreetName streetName = new StreetName();
		streetName.setObject(street);

		// set street name translation for default language
		StreetNameTranslation nameTranslation = new StreetNameTranslation();
		nameTranslation.setLang(ApplicationConfig.getDefaultLanguage());
		nameTranslation.setTranslatable(streetName);
		nameTranslation.setName(rawData.getName());

		// set name translations in name
		Set<StreetNameTranslation> translations = new HashSet<StreetNameTranslation>();
		translations.add(nameTranslation);
		streetName.setTranslations(translations);

		// set street name as temporal value, from past infinite to future infinite
		StreetNameTemporal nameTemporal = new StreetNameTemporal();
		nameTemporal.setObject(street);
		nameTemporal.setValue(streetName);
		street.setNamesTimeLine(new TimeLine<StreetName, StreetNameTemporal>(nameTemporal));

		// try to find street type by name
		Stub<StreetType> streetType = cs.findCorrection(rawData.getType(), StreetType.class, null);
		if (streetType == null) {
			// street type was not found, give up
			throw new FlexPayException("Cannot find street type: " + rawData.getType());
		}

		// set street type as temporal value, from past infinite to future infinite
		StreetTypeTemporal temporal = new StreetTypeTemporal();
		temporal.setObject(street);
		temporal.setValue(new StreetType(streetType));
		TimeLine<StreetType, StreetTypeTemporal> typeTimeLine =
				new TimeLine<StreetType, StreetTypeTemporal>(temporal);
		street.setTypesTimeLine(typeTimeLine);

		// convertion well done, return a new street object
		return street;
	}
}
