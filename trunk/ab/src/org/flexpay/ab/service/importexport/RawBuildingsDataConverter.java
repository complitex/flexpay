package org.flexpay.ab.service.importexport;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class RawBuildingsDataConverter implements DataConverter<BuildingAddress, RawBuildingsData> {

	private ObjectsFactory factory;

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawData			   RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	@Override
	public BuildingAddress fromRawData(RawBuildingsData rawData,
								 DataSourceDescription dataSourceDescription,
								 CorrectionsService correctionsService) throws FlexPayException {

		Stub<Street> street = correctionsService.findCorrection(
				rawData.getStreetId(), Street.class, stub(dataSourceDescription));
		if (street == null) {
			throw new FlexPayException("Cannot find street");
		}

		Stub<District> district = correctionsService.findCorrection(rawData
				.getDistrictId(), District.class, stub(dataSourceDescription));
		if (district == null) {
			throw new FlexPayException("Cannot find district");
		}

		Building building = factory.newBuilding();
		building.setDistrict(new District(district));

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setStreet(new Street(street));
		building.addAddress(buildingAddress);

		buildingAddress.setBuildingAttribute(BuildingAddress.numberAttribute(rawData.getNumber()));

		if (StringUtils.isNotBlank(rawData.getBulkNumber())) {
			buildingAddress.setBuildingAttribute(BuildingAddress.bulkAttribute(rawData.getBulkNumber()));
		}

		return buildingAddress;
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		this.factory = factory;
	}

}
