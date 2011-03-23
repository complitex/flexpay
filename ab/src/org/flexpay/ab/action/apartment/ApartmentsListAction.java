package org.flexpay.ab.action.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.sorter.ApartmentSorter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class ApartmentsListAction extends FPActionWithPagerSupport<Apartment> {

	private Long buildingFilter;
	private List<Apartment> apartments = list();

	protected ApartmentSorter apartmentSorter = new ApartmentSorter();

	private ApartmentService apartmentService;
	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return SUCCESS;
		}

		if (apartmentSorter == null) {
			log.warn("ApartmentSorter is null");
			apartmentSorter = new ApartmentSorter();
		}

		apartments = apartmentService.find(arrayStack(new BuildingsFilter(buildingFilter)), list(apartmentSorter), getPager());
		if (log.isDebugEnabled()) {
			log.debug("Total apartments found: {}", apartments.size());
		}

		apartments = apartmentService.readFull(collectionIds(apartments), true);
		if (log.isDebugEnabled()) {
			log.debug("Total full apartments found: {}", apartments.size());
		}

		return SUCCESS;
	}

	private boolean doValidate() {

		if (buildingFilter == null || buildingFilter <= 0) {
			log.warn("Incorrect building id in filter ({})", buildingFilter);
			addActionError(getText("ab.error.building_address.incorrect_address_id"));
			buildingFilter = 0L;
		} else {
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

	public void setBuildingFilter(Long buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

	public List<Apartment> getApartments() {
		return apartments;
	}

	public ApartmentSorter getApartmentSorter() {
		return apartmentSorter;
	}

	public void setApartmentSorter(ApartmentSorter apartmentSorter) {
		this.apartmentSorter = apartmentSorter;
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
