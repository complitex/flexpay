package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ExchangeHelper {

	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private StreetService streetService;
	private TownService townService;
	private PersonService personService;

	/**
	 * Get address group delimited with {@link Operation#ADDRESS_DELIMITER}
	 *
	 * @param apartment Apartment stub
	 * @return Address group
	 * @throws FlexPayException if failure occurs
	 */
	public String getAddressGroup(Apartment apartment) throws FlexPayException {
		String apartmentNumber = apartmentService.getApartmentNumber(stub(apartment));

		Buildings buildings = buildingService.getFirstBuildings(apartment.getBuildingStub());
		String buildingNumber = buildings.getNumber();
		String bulkNumber = buildings.getBulk();

		Street street = streetService.readFull(buildings.getStreetStub());

		StreetName streetNameObj = street.getCurrentName();
		StreetNameTranslation nameTranslation = TranslationUtil
				.getTranslation(streetNameObj.getTranslations());
		String streetName = nameTranslation.getName();

		StreetType streetTypeObj = street.getCurrentType();
		String streetType = null;
		if (streetTypeObj != null) {
			StreetTypeTranslation typeTranslation = TranslationUtil
					.getTranslation(streetTypeObj.getTranslations());
			streetType = typeTranslation.getName();
		}

		Town townObj = townService.readFull(street.getTownStub());
		TownName townNameObj = townObj.getCurrentName();
		TownNameTranslation townNameTranslation = TranslationUtil
				.getTranslation(townNameObj.getTranslations());
		String town = townNameTranslation.getName();

		String[] arr = {town, streetType, streetName, buildingNumber, bulkNumber, apartmentNumber};
		return StringUtils.join(arr, Operation.ADDRESS_DELIMITER);
	}

	/**
	 * Get first, middle, last person names group delimited with {@link Operation#FIO_DELIMITER}
	 *
	 * @param personStub Person stub
	 * @return FIO group
	 */
	public String getFIOGroup(Stub<Person> personStub) {
		Person persistent = personService.read(personStub);
		PersonIdentity identity = persistent.getDefaultIdentity();
		String firstName = identity.getFirstName();
		String middleName = identity.getMiddleName();
		String lastName = identity.getLastName();

		String[] arr = {lastName, firstName, middleName};
		return StringUtils.join(arr, Operation.FIO_DELIMITER);
	}

	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
