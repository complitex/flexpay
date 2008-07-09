package org.flexpay.eirc.actions.eirc_account;

import java.util.Collections;
import java.util.List;

import org.flexpay.ab.actions.apartment.FiltersBaseAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.dao.paging.Page;

public class EircAccountCreateForm1Action  extends FiltersBaseAction {

	private ApartmentService apartmentService;
	
	private List<Apartment> apartments = Collections.EMPTY_LIST;
	
	
	public String execute() {
		
		initFilters();
		if(getFiltersError() == null) {
			Page pager = new Page();
			pager.setPageSize(10000);
			apartments = apartmentService.getApartments(getFilters(), pager);
		}
		
		
		
		return "success";
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
