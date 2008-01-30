package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.dao.paging.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ApartmentServiceImpl implements ApartmentService {

	private ApartmentDao apartmentDao;

	/**
	 * Setter for property 'apartmentDao'.
	 *
	 * @param apartmentDao Value to set for property 'apartmentDao'.
	 */
	public void setApartmentDao(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}

	public List<Apartment> getApartments(ArrayStack filters, Page pager) {
		BuildingsFilter filter = (BuildingsFilter) filters.peek();
		return apartmentDao.findObjects(filter.getSelectedId(), pager);
	}
}
