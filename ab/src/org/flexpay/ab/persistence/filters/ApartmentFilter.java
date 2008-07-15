package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.List;
import java.util.Collections;

public class ApartmentFilter extends PrimaryKeyFilter<Apartment> {

	private List<Apartment> apartments = Collections.emptyList();

	public List<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments = apartments;
	}
}
