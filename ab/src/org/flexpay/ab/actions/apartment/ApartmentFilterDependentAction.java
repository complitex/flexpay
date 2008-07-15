package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.filters.*;
import org.apache.commons.collections.ArrayStack;

public abstract class ApartmentFilterDependentAction extends BuildingsFilterDependentAction {

	protected ApartmentFilter apartmentFilter = new ApartmentFilter();

	/**
	 * Getter for property 'filters'.
	 *
	 * @return Value for property 'filters'.
	 */
	@Override
	public ArrayStack getFilters() {
		ArrayStack filters = super.getFilters();
		filters.push(apartmentFilter);

		return filters;
	}

	/**
	 * Setter for property 'filters'.
	 *
	 * @param filters Value to set for property 'filters'.
	 */
	@Override
	public void setFilters(ArrayStack filters) {

		setFilters(filters, 6);
	}

	@Override
	protected int setFilters(ArrayStack filters, int n) {
		n = super.setFilters(filters, n);
		apartmentFilter = (ApartmentFilter) filters.peek(--n);

		return n;
	}

	public ApartmentFilter getApartmentFilter() {
		return apartmentFilter;
	}

	public void setApartmentFilter(ApartmentFilter apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}
}
