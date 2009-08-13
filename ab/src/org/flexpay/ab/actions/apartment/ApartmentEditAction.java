package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
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
	private Long buildingFilter;
	private String apartmentNumber;

	private String crumbCreateKey;
	private BuildingService buildingService;
	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		apartment = apartment.isNew() ? apartment : apartmentService.readFull(stub(apartment));

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
			updateApartment();

			return REDIRECT_SUCCESS;
		}

		apartmentNumber = apartment.getNumber();

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

		if (apartment == null) {
			addActionError(getText("common.object_not_selected"));
			return false;
		}

		if (buildingFilter == null || buildingFilter <= 0) {
			log.warn("Incorrect building address id in filter ({})", buildingFilter);
			addActionError(getText("ab.error.apartment.no_building"));
		}

		return !hasActionErrors();
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
	@Override
	protected String getErrorResult() {
		return INPUT;
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
