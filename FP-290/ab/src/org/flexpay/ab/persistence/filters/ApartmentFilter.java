package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ApartmentFilter extends PrimaryKeyFilter<Apartment> {

	private List<Apartment> apartments = Collections.emptyList();

	public List<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments = apartments;
	}

	@Nullable
	public Apartment getSelected() {
		if (needFilter()) {
			for (Apartment apartment : apartments) {
				if (getSelectedId().equals(apartment.getId())) {
					return apartment;
				}
			}
		}

		return null;
	}
}
