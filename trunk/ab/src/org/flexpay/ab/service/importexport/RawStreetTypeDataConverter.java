package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Set;
import java.util.HashSet;

public class RawStreetTypeDataConverter implements DataConverter<StreetType, RawStreetTypeData> {

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawStreetTypeData	 RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public StreetType fromRawData(RawStreetTypeData rawStreetTypeData,
								  DataSourceDescription dataSourceDescription,
								  CorrectionsService correctionsService)
			throws FlexPayException {

		StreetType streetType = new StreetType();

		StreetTypeTranslation typeTranslation = new StreetTypeTranslation();
		typeTranslation.setTranslatable(streetType);
		typeTranslation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
		typeTranslation.setName(rawStreetTypeData.getName());

		Set<StreetTypeTranslation> translations = new HashSet<StreetTypeTranslation>();
		translations.add(typeTranslation);
		streetType.setTranslations(translations);

		return streetType;
	}
}