package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentEditAction extends FPActionSupport {

	private BuildingsFilter buildingsFilter = new BuildingsFilter();
	private Apartment apartment = Apartment.newInstance();
	private String apartmentNumber;

	private String crumbCreateKey;
	private BuildingService buildingService;
	private ApartmentService apartmentService;

	@NotNull
	public String doExecute() throws Exception {

		if (apartment.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		apartment = apartment.isNew() ? apartment : apartmentService.readFull(stub(apartment));
		if (apartment == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		if (apartment.isNotNew()) {
			buildingsFilter.setSelectedId(apartment.getBuildingStub().getId());
			buildingsFilter.setReadOnly(true);
		}

		if (!isSubmit()) {
			apartmentNumber = apartment.getNumber();
			return INPUT;
		}

		if (!buildingsFilter.needFilter()) {
			addActionError(getText("ab.error.apartment.invalid_buildings_id"));
			return REDIRECT_SUCCESS;
		}

		Building building = buildingService.findBuilding(buildingsFilter.getSelectedStub());
		if (building == null) {
			addActionError(getText("ab.error.apartment.invalid_buildings_id"));
			return REDIRECT_SUCCESS;
		}

		apartment.setNumber(apartmentNumber);

		if (apartment.isNew()) {
			apartment.setBuilding(building);
			apartmentService.create(apartment);
		} else {
			apartmentService.update(apartment);
		}

		addActionError(getText("ab.apartment.saved"));

		return REDIRECT_SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {
		if (apartment.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
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

	public BuildingsFilter getBuildingsFilter() {
		return buildingsFilter;
	}

	public void setBuildingsFilter(BuildingsFilter buildingsFilter) {
		this.buildingsFilter = buildingsFilter;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
