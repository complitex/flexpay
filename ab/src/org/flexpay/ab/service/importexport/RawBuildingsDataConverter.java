package org.flexpay.ab.service.importexport;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class RawBuildingsDataConverter implements DataConverter<Buildings, RawBuildingsData> {

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
								 CorrectionsService correctionsService)
			throws FlexPayException {

		Buildings buildings = new Buildings();

		Street street = (Street) correctionsService.findCorrection(
				rawData.getStreetId(), Street.class, dataSourceDescription);
		if (street == null) {
			throw new FlexPayException("Cannot find street");
		}
		buildings.setStreet(street);

		District district = (District) correctionsService.findCorrection(
				rawData.getDistrictId(), District.class, dataSourceDescription);
		if (district == null) {
			throw new FlexPayException("Cannot find district");
		}

		Building building = new Building();
		building.setDistrict(district);

		Set<Buildings> buildingses = new HashSet<Buildings>();
		buildingses.add(buildings);
		building.setBuildingses(buildingses);
		buildings.setBuilding(building);

		List<BuildingAttribute> attributes = new ArrayList<BuildingAttribute>();

		BuildingAttribute number = new BuildingAttribute();
		number.setValue(rawData.getNumber());
		number.setBuildingAttributeType(
				buildingService.getAttributeType(BuildingAttributeType.TYPE_NUMBER));
		number.setBuildings(buildings);
		attributes.add(number);

		if (StringUtils.isNotBlank(rawData.getBulkNumber())) {
			BuildingAttribute bulk = new BuildingAttribute();
			bulk.setValue(rawData.getBulkNumber());
			bulk.setBuildingAttributeType(
					buildingService.getAttributeType(BuildingAttributeType.TYPE_BULK));
			bulk.setBuildings(buildings);
			attributes.add(bulk);
		}
		buildings.setBuildingAttributes(attributes);

		return buildings;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
