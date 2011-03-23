package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

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
	@Override
	public Street fromRawData(RawStreetData streetRawData,
							  DataSourceDescription dataSourceDescription,
							  CorrectionsService correctionsService)
			throws FlexPayException {

		Street street = new Street();
		StreetName streetName = new StreetName();
		streetName.setObject(street);

		StreetNameTranslation nameTranslation = new StreetNameTranslation();
		nameTranslation.setLang(ApplicationConfig.getDefaultLanguage());
		nameTranslation.setTranslatable(streetName);
		nameTranslation.setName(streetRawData.getName());

		Set<StreetNameTranslation> translations = set();
		translations.add(nameTranslation);
		streetName.setTranslations(translations);

		StreetNameTemporal nameTemporal = new StreetNameTemporal();
		nameTemporal.setObject(street);
		nameTemporal.setValue(streetName);
		street.setNamesTimeLine(new TimeLine<StreetName, StreetNameTemporal>(nameTemporal));

		Stub<StreetType> streetType = correctionsService.findCorrection(
				streetRawData.getTypeId(), StreetType.class, stub(dataSourceDescription));
		if (streetType == null) {
			throw new FlexPayException("Cannot find street type correction: " + streetRawData.getTypeId());
		}
		StreetTypeTemporal temporal = new StreetTypeTemporal();
		temporal.setObject(street);
		temporal.setValue(new StreetType(streetType));
		TimeLine<StreetType, StreetTypeTemporal> typeTimeLine =
				new TimeLine<StreetType, StreetTypeTemporal>(temporal);
		street.setTypesTimeLine(typeTimeLine);

		return street;
	}

}
