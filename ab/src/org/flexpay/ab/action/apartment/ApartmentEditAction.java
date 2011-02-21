package org.flexpay.ab.action.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ApartmentEditAction extends FPActionSupport {

    public final String APARTMENTS_SEPARATOR = ",";
    public final String INTERVAL_SEPARATOR = "\\.\\.";

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
			addActionError(getText("ab.error.apartment.incorrect_apartment_id"));
			return REDIRECT_ERROR;
		}

		if (apartment.isNotNew()) {
			Stub<Apartment> stub = stub(apartment);
			apartment = apartmentService.readWithHierarchy(stub);

			if (apartment == null) {
				log.warn("Can't get apartment with id {} from DB", stub.getId());
				addActionError(getText("ab.error.apartment.cant_get_apartment"));
				return REDIRECT_ERROR;
			} else if (apartment.isNotActive()) {
				log.warn("Apartment with id {} is disabled", stub.getId());
				addActionError(getText("ab.error.apartment.cant_get_apartment"));
				return REDIRECT_ERROR;
			}

		}

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
            if (apartment.isNew()) {
                processApartmentNumber();
            } else {
                updateApartment();
            }

            if (hasActionErrors()) {
                return INPUT;
            }
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

    @SuppressWarnings({"ObjectToString"})
    private void processApartmentNumber() throws FlexPayExceptionContainer {

        log.debug("ApartmentNumbers string = {}", apartmentNumber);

        String[] apIntervals = apartmentNumber.contains(APARTMENTS_SEPARATOR) ? apartmentNumber.trim().split(APARTMENTS_SEPARATOR) : new String[] {apartmentNumber.trim()};
        log.debug("Apartment intervals = {}", apIntervals.toString());

        Building building = buildingService.findBuilding(new Stub<BuildingAddress>(buildingFilter));

        for (String interval : apIntervals) {

            String[] apValues = interval.contains("..") ? interval.trim().split(INTERVAL_SEPARATOR) : new String[] {interval.trim()};
            log.debug("Apartment values = {}", apValues.toString());

            if (apValues.length == 1) {
                log.debug("Creating apartment with number = {} for building with id = {}", apValues[0], buildingFilter);
                createApartment(apValues[0], building);
            } else if (apValues.length == 2) {

                int start;
                int finish;

                try {
                    start = Integer.parseInt(apValues[0].trim());
                } catch (NumberFormatException e) {
                    log.debug("Incorrect start value in apartment interval");
                    addActionError(getText("ab.error.apartment.incorrect_start_value_in_interval"));
                    return;
                }
                try {
                    finish = Integer.parseInt(apValues[1].trim());
                } catch (NumberFormatException e) {
                    log.debug("Incorrect finish value in apartment interval");
                    addActionError(getText("ab.error.apartment.incorrect_finish_value_in_interval"));
                    return;
                }

                if (start > finish) {
                    log.debug("Incorrect apartment interval: start value more than finish value");
                    addActionError(getText("ab.error.apartment.incorrect_start_value_more_than_finish_value"));
                    return;
                }

                for (int i = start; i <= finish; i++) {
                    log.debug("Creating apartment with number = {} for building with id = {}", i, buildingFilter);
                    createApartment(i + "", building);
                }
            }
        }
    }

    private void createApartment(String apartmentNumber, Building building) throws FlexPayExceptionContainer {
        Apartment ap = Apartment.newInstance();
        ap.setBuilding(building);
        ap.setNumber(apartmentNumber);
        apartmentService.create(ap);
    }

	private void updateApartment() throws FlexPayExceptionContainer {
		apartment.setNumber(apartmentNumber);
        apartmentService.update(apartment);
	}

	private boolean doValidate() {

		if (buildingFilter == null || buildingFilter <= 0) {
			log.warn("Incorrect building address id in filter ({})", buildingFilter);
			addActionError(getText("ab.error.building_address.incorrect_address_id"));
			buildingFilter = 0L;
		} else if (apartment.isNew()) {
			BuildingAddress address = buildingService.readFullAddress(new Stub<BuildingAddress>(buildingFilter));
			if (address == null) {
				log.warn("Can't get building address with id {} from DB", buildingFilter);
				addActionError(getText("ab.error.building_address.cant_get_address"));
				buildingFilter = 0L;
			} else if (address.isNotActive()) {
				log.warn("Building address with id {} is disabled", buildingFilter);
				addActionError(getText("ab.error.building_address.cant_get_address"));
				buildingFilter = 0L;
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
