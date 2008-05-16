package org.flexpay.ab.service.importexport;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
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
	 * @param rawData
	 *            RawData
	 * @param dataSourceDescription
	 *            Data source description
	 * @param correctionsService
	 *            CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException
	 *             if failure occurs
	 */
	public Buildings fromRawData(RawBuildingsData rawData,
			DataSourceDescription dataSourceDescription,
			CorrectionsService correctionsService) throws FlexPayException {

		Buildings buildings = new Buildings();

		Street street = correctionsService.findCorrection(
				rawData.getStreetId(), Street.class, dataSourceDescription);
		if (street == null) {
			throw new FlexPayException("Cannot find street");
		}
		buildings.setStreet(street);

		District district = correctionsService.findCorrection(rawData
				.getDistrictId(), District.class, dataSourceDescription);
		if (district == null) {
			throw new FlexPayException("Cannot find district");
		}

		Building building = new Building();
		building.setDistrict(district);

		Set<Buildings> buildingses = new HashSet<Buildings>();
		buildingses.add(buildings);
		building.setBuildingses(buildingses);
		buildings.setBuilding(building);

		Set<BuildingAttribute> attributes = new HashSet<BuildingAttribute>();

		BuildingAttribute number = new BuildingAttribute();
		number.setValue(rawData.getNumber());
		number.setBuildingAttributeType(buildingService
				.getAttributeType(BuildingAttributeType.TYPE_NUMBER));
		number.setBuildings(buildings);
		attributes.add(number);

		if (StringUtils.isNotBlank(rawData.getBulkNumber())) {
			BuildingAttribute bulk = new BuildingAttribute();
			bulk.setValue(rawData.getBulkNumber());
			bulk.setBuildingAttributeType(buildingService
					.getAttributeType(BuildingAttributeType.TYPE_BULK));
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
