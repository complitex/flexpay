package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.actions.FPActionSupport;
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
			addActionError(getText("ab.error.apartment.no_building"));
			return SUCCESS;
		}

		if (beginDate == null) {
			log.debug("BeginDate parameter is null");
			beginDate = DateUtil.now();
		}
		if (endDate == null) {
			log.debug("EndDate parameter is null");
			endDate = getFutureInfinite();
		}

		Stub<Apartment> stub = new Stub<Apartment>(apartmentFilter);
		Apartment apartment = apartmentService.readWithHierarchy(stub);

		if (apartment == null) {
			log.debug("Can't get apartment with id {} from DB", stub.getId());
			return SUCCESS;
		} else if (apartment.isNotActive()) {
			log.debug("Apartment with id {} is disabled", stub.getId());
			return SUCCESS;
		}

		buildingFilter = apartment.getBuildingStub().getId();
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
