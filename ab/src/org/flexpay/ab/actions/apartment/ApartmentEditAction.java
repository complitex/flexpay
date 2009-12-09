package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentEditAction extends FPActionSupport {

	private Apartment apartment = Apartment.newInstance();
	private Long countryFilter;
	private Long regionFilter;
	private Long townFilter;
	private Long streetFilter;
	private Long buildingFilter;
	private String apartmentNumber;

	private String crumbCreateKey;
	private BuildingService buildingService;
	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (apartment == null || apartment.getId() == null) {
			log.warn("Incorrect apartment id");
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (apartment.isNotNew()) {
			Stub<Apartment> stub = stub(apartment);
			apartment = apartmentService.readWithHierarchy(stub);

			if (apartment == null) {
				log.warn("Can't get apartment with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			} else if (apartment.isNotActive()) {
				log.warn("Apartment with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			}

		}

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
			updateApartment();

			addActionMessage(getText("ab.apartment.saved"));

			return REDIRECT_SUCCESS;
		}

		apartmentNumber = apartment.getNumber();

		if (apartment.isNotNew()) {
			buildingFilter = apartment.getDefaultBuildings().getId();
			streetFilter = apartment.getDefaultStreet().getId();
			townFilter = apartment.getTown().getId();
			regionFilter = apartment.getRegion().getId();
			countryFilter = apartment.getCountry().getId();
		}

		return INPUT;

	}

	private void updateApartment() throws FlexPayExceptionContainer {
		apartment.setNumber(apartmentNumber);

		if (apartment.isNew()) {
			apartment.setBuilding(buildingService.findBuilding(new Stub<BuildingAddress>(buildingFilter)));
			apartmentService.create(apartment);
		} else {
			apartmentService.update(apartment);
		}
	}

	private boolean doValidate() {

		if (buildingFilter == null || buildingFilter <= 0) {
			log.warn("Incorrect building address id in filter ({})", buildingFilter);
			addActionError(getText("ab.error.apartment.no_building"));
		} else if (apartment.isNew()) {
			Stub<BuildingAddress> stub = new Stub<BuildingAddress>(buildingFilter);
			BuildingAddress address = buildingService.readFullAddress(stub);
			if (address == null) {
				log.warn("Can't get building address with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
			} else if (address.isNotActive()) {
				log.warn("Building address with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
			}
		}

		return !hasActionErrors();
	}

	@Override
	protected void setBreadCrumbs() {
		if (apartment != null && apartment.isNew()) {
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
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	public Long getCountryFilter() {
		return countryFilter;
	}

	public Long getRegionFilter() {
		return regionFilter;
	}

	public Long getTownFilter() {
		return townFilter;
	}

	public Long getStreetFilter() {
		return streetFilter;
	}

	public Long getBuildingFilter() {
		return buildingFilter;
	}

	public void setBuildingFilter(Long buildingFilter) {
		this.buildingFilter = buildingFilter;
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
