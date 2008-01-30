package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface ApartmentService {

	public List<Apartment> getApartments(ArrayStack filters, Page pager);
}
