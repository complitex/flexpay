package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;

public class ApartmentEditAction extends BuildingsFilterDependentAction {

	private BuildingService buildingService;
	private ApartmentService apartmentService;

	private Apartment apartment = new Apartment();
	private String apartmentNumber;

	@NotNull
	public String doExecute() throws Exception {

		if (!buildingsFilter.needFilter()) {
			log.error("!!!!!!!!!!!! no buildings filter value");
			addActionError(getText("ab.error.apartment.invalid_buildings_id"));
			return REDIRECT_SUCCESS;
		}

		Building building = buildingService.findBuilding(buildingsFilter.getSelectedStub());
		if (building == null) {
			log.error("!!!!!!!!!!!! no building fetched");
			addActionError(getText("ab.error.apartment.invalid_buildings_id"));
			return REDIRECT_SUCCESS;
		}

		if (apartment.isNotNew()) {
			apartment = apartmentService.readWithPersons(stub(apartment));
		}

		countryFilter.setReadOnly(true);
		regionFilter.setReadOnly(true);
		townFilter.setReadOnly(true);
		streetFilter.setReadOnly(true);
		buildingsFilter.setReadOnly(true);
		initFilters();

		if (isSubmit()) {
			apartment.setBuilding(building);
			apartment.setNumber(apartmentNumber);
			apartmentService.save(apartment);
			return REDIRECT_SUCCESS;
		}

		apartmentNumber = apartment.getNumber();
		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	/**
	 * @return the apartment
	 */
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * @param apartment the apartment to set
	 */
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	/**
	 * @param apartmentNumber the apartmentNumber to set
	 */
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	/**
	 * @param apartmentService the apartmentService to set
	 */
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
