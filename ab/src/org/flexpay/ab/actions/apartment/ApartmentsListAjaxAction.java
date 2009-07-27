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

public class ApartmentsListAjaxAction extends FPActionWithPagerSupport<Apartment> {

	private String buildingId;
	private List<Apartment> apartments = list();

	protected ApartmentSorter apartmentSorter = new ApartmentSorter();

	private ApartmentService apartmentService;

	@Override
	@NotNull
	public String doExecute() throws Exception {

		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(apartmentSorter);

		Long buildingIdLong;

		try {
			buildingIdLong = Long.parseLong(buildingId);
		} catch (Exception e) {
			log.warn("Incorrect building address id in filter ({})", buildingId);
			return SUCCESS;
		}

		apartments = apartmentService.getApartments(new Stub<BuildingAddress>(buildingIdLong), sorters, getPager());
		log.info("Total apartments found: {}", apartments);
		apartments = apartmentService.readFull(Apartment.collectionIds(apartments));
		log.info("Total apartments readFull: {}", apartments);

		log.info("Found apartments: {}", apartments);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
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
