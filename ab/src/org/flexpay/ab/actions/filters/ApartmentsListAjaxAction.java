package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ApartmentsListAjaxAction extends FPActionWithPagerSupport {

	private String[] parents;
	private List<Apartment> apartments = list();

	private ApartmentService apartmentService;

	@NotNull
	public String doExecute() throws Exception {

		Long addressIdLong;

		try {
			addressIdLong = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect building address id in filter ({})", parents[0]);
			return SUCCESS;
		}

		apartments = apartmentService.getApartments(new Stub<BuildingAddress>(addressIdLong), getPager());

		log.debug("Found apartments: {}", apartments);

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
	protected String getErrorResult() {
		return SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {

	}

	public void setParents(String[] parents) {
		this.parents = parents;
	}

	public List<Apartment> getApartments() {
		return apartments;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

}
