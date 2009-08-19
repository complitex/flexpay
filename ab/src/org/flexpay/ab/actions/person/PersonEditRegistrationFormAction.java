package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class PersonEditRegistrationFormAction extends FPActionSupport {

	private Date beginDate = DateUtil.now();
	private Date endDate = ApplicationConfig.getFutureInfinite();

	private Long countryFilter;
	private Long regionFilter;
	private Long townFilter;
	private Long streetFilter;
	private Long buildingFilter;
	private Long apartmentFilter;

	private RegionService regionService;
	private TownService townService;
	private StreetService streetService;
	private BuildingService buildingService;
	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (apartmentFilter != null && apartmentFilter > 0) {
			Building building = apartmentService.getBuilding(new Stub<Apartment>(apartmentFilter));;
			BuildingAddress address = buildingService.getFirstBuildings(new Stub<Building>(building.getId()));
			Street street = streetService.readFull(address.getStreetStub());
			Town town = townService.readFull(street.getTownStub());
			Region region = regionService.readFull(town.getRegionStub());
			buildingFilter = address.getId();
			streetFilter = address.getStreetStub().getId();
			townFilter = town.getId();
			regionFilter = region.getId();
			countryFilter = region.getCountryStub().getId();
		}

		return SUCCESS;
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
		return SUCCESS;
	}

	public String getBeginDate() {
		return format(beginDate);
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = DateUtil.parseBeginDate(beginDate);
	}

	public String getEndDate() {
		return format(endDate);
	}

	public void setEndDate(String endDate) {
		this.endDate = DateUtil.parseEndDate(endDate);
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

	public Long getApartmentFilter() {
		return apartmentFilter;
	}

	public void setApartmentFilter(Long apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
