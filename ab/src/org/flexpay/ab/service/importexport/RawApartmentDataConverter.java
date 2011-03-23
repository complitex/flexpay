package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

public class RawApartmentDataConverter implements DataConverter<Apartment, RawApartmentData> {

	private BuildingService buildingService;
	private ObjectsFactory factory;

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
	@Override
	public Apartment fromRawData(RawApartmentData rawData,
								 DataSourceDescription dataSourceDescription,
								 CorrectionsService correctionsService)
			throws FlexPayException {

		Apartment apartment = factory.newApartment();

		Stub<BuildingAddress> buildings = correctionsService.findCorrection(
				rawData.getBuildingId(), BuildingAddress.class, stub(dataSourceDescription));
		if (buildings == null) {
			throw new FlexPayException("Failed finding building for apartment #" + rawData.getExternalSourceId());
		}

		Building persistent = buildingService.findBuilding(buildings);
		if (persistent == null) {
			throw new FlexPayException("Failed getting building by buildings #" + buildings.getId());
		}
		apartment.setBuilding(persistent);

		ApartmentNumber number = new ApartmentNumber();
		number.setApartment(apartment);
		number.setValue(rawData.getNumber());
		number.setBegin(DateUtil.now());
		number.setEnd(ApplicationConfig.getFutureInfinite());

		Set<ApartmentNumber> numbers = set();
		numbers.add(number);
		apartment.setApartmentNumbers(numbers);

		return apartment;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		this.factory = factory;
	}

}
