package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.sorter.ApartmentSorter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ApartmentsListAction extends FPActionWithPagerSupport<Apartment> {

	private Long buildingFilter;
	private List<Apartment> apartments = list();

	protected ApartmentSorter apartmentSorter = new ApartmentSorter();

	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(apartmentSorter);

		if (buildingFilter == null || buildingFilter <= 0) {
			log.warn("Incorrect building id in filter ({})", buildingFilter);
			return SUCCESS;
		}

		apartments = apartmentService.getApartments(new Stub<BuildingAddress>(buildingFilter), sorters, getPager());
		apartments = apartmentService.readFull(Apartment.collectionIds(apartments));

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

}
