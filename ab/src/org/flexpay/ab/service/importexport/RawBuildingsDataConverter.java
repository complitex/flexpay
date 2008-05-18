package org.flexpay.ab.service.importexport;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;

public class RawBuildingsDataConverter implements
		DataConverter<Buildings, RawBuildingsData> {

	private BuildingService buildingService;

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawData			   RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	public Buildings fromRawData(RawBuildingsData rawData,
								 DataSourceDescription dataSourceDescription,
								 CorrectionsService correctionsService) throws FlexPayException {

		Street street = correctionsService.findCorrection(
				rawData.getStreetId(), Street.class, dataSourceDescription);
		if (street == null) {
			throw new FlexPayException("Cannot find street");
		}

		District district = correctionsService.findCorrection(rawData
				.getDistrictId(), District.class, dataSourceDescription);
		if (district == null) {
			throw new FlexPayException("Cannot find district");
		}

		Building building = new Building();
		building.setDistrict(district);

		Buildings buildings = new Buildings();
		buildings.setStreet(street);
		building.addBuildings(buildings);

		buildings.setBuildingAttribute(rawData.getNumber(), buildingService
				.getAttributeType(BuildingAttributeType.TYPE_NUMBER));

		if (StringUtils.isNotBlank(rawData.getBulkNumber())) {
			buildings.setBuildingAttribute(rawData.getBulkNumber(), buildingService
					.getAttributeType(BuildingAttributeType.TYPE_BULK));
		}

		return buildings;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
