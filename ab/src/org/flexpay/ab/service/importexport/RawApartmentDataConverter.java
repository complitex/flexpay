package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.HashSet;
import java.util.Set;

public class RawApartmentDataConverter implements DataConverter<Apartment, RawApartmentData> {

	private BuildingService buildingService;

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawData			   RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public Apartment fromRawData(RawApartmentData rawData,
								 DataSourceDescription dataSourceDescription,
								 CorrectionsService correctionsService)
			throws FlexPayException {

		Apartment apartment = new Apartment();

		Buildings buildings = correctionsService.findCorrection(
				rawData.getBuildingId(), Buildings.class, dataSourceDescription);
		if (buildings == null) {
			throw new FlexPayException("Failed finding building for apartment #" + rawData.getExternalSourceId());
		}

		Building persistent = buildingService.findBuilding(stub(buildings));
		if (persistent == null) {
			throw new FlexPayException("Failed getting building by buildings #" + buildings.getId());
		}
		apartment.setBuilding(persistent);

		ApartmentNumber number = new ApartmentNumber();
		number.setApartment(apartment);
		number.setValue(rawData.getNumber());
		number.setBegin(DateIntervalUtil.now());
		number.setEnd(ApplicationConfig.getFutureInfinite());

		Set<ApartmentNumber> numbers = new HashSet<ApartmentNumber>();
		numbers.add(number);
		apartment.setApartmentNumbers(numbers);

		return apartment;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}