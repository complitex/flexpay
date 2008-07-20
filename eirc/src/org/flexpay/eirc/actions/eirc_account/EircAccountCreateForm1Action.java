package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.actions.apartment.BuildingsFilterDependentAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.dao.paging.Page;

import java.util.Collections;
import java.util.List;

public class EircAccountCreateForm1Action extends BuildingsFilterDependentAction {

	private ApartmentService apartmentService;

	private List<Apartment> apartments = Collections.emptyList();


	public String doExecute() {

		initFilters();
		if (getFiltersError() == null) {
			Page pager = new Page();
			pager.setPageSize(10000);
			apartments = apartmentService.getApartments(getFilters(), pager);
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
	protected String getErrorResult() {
		return SUCCESS;
	}

	/**
	 * @param apartmentService the apartmentService to set
	 */
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	/**
	 * @return the apartments
	 */
	public List<Apartment> getApartments() {
		return apartments;
	}
}
