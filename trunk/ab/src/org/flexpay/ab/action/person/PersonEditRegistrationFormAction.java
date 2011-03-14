package org.flexpay.ab.action.person;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class PersonEditRegistrationFormAction extends FPActionSupport {

	private Date beginDate = DateUtil.now();
	private Date endDate = getFutureInfinite();

	private Long countryFilter;
	private Long regionFilter;
	private Long townFilter;
	private Long streetFilter;
	private Long buildingFilter;
	private Long apartmentFilter;

	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (apartmentFilter == null || apartmentFilter <= 0) {
			log.warn("Incorrect apartment id");
			addActionError(getText("ab.error.apartment.incorrect_apartment_id"));
			return SUCCESS;
		}

		Apartment apartment = apartmentService.readWithHierarchy(new Stub<Apartment>(apartmentFilter));
		if (apartment == null) {
			log.warn("Can't get apartment with id {} from DB", apartmentFilter);
			addActionError(getText("ab.error.apartment.cant_get_apartment"));
			return SUCCESS;
		} else if (apartment.isNotActive()) {
			log.warn("Apartment with id {} is disabled", apartmentFilter);
			addActionError(getText("ab.error.apartment.cant_get_apartment"));
			return SUCCESS;
		}

		buildingFilter = apartment.getDefaultBuildings().getId();
		streetFilter = apartment.getDefaultStreet().getId();
		townFilter = apartment.getTown().getId();
		regionFilter = apartment.getRegion().getId();
		countryFilter = apartment.getCountry().getId();

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

}
