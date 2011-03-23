package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTemporal;
import org.flexpay.ab.persistence.DistrictNameTranslation;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.config.ApplicationConfig;

import static org.flexpay.common.util.CollectionUtils.set;

public class RawDistrictDataConverter implements DataConverter<District, RawDistrictData> {

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawDistrictData	   RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	@Override
	public District fromRawData(RawDistrictData rawDistrictData,
								DataSourceDescription dataSourceDescription,
								CorrectionsService correctionsService)
			throws FlexPayException {

		District district = new District();
		DistrictName districtName = new DistrictName();
		districtName.setObject(district);

		DistrictNameTranslation nameTranslation = new DistrictNameTranslation();
		nameTranslation.setLang(ApplicationConfig.getDefaultLanguage());
		nameTranslation.setTranslatable(districtName);
		nameTranslation.setName(rawDistrictData.getName());

		districtName.setTranslations(set(nameTranslation));

		DistrictNameTemporal nameTemporal = new DistrictNameTemporal();
		nameTemporal.setObject(district);
		nameTemporal.setValue(districtName);
		district.setNamesTimeLine(new TimeLine<DistrictName, DistrictNameTemporal>(nameTemporal));

		return district;
	}

}
