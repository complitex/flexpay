package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.sorter.ApartmentSorter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.persistence.DomainObject.collectionIds;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

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
			log.debug("ApartmentSorter is null");
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
			addActionError(getText("ab.error.building.no_building"));
		} else {
			Stub<Building> stub = new Stub<Building>(buildingFilter);
			Building building = buildingService.readFull(stub);
			if (building == null) {
				log.warn("Can't get building with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
				buildingFilter = null;
			} else if (building.isNotActive()) {
				log.warn("Building with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
				buildingFilter = null;
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
