package org.flexpay.ab.service.imp;

import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.common.dao.paging.Page;
import org.apache.commons.collections.ArrayStack;

import java.util.List;

public class BuildingServiceImpl implements BuildingService {

	private BuildingsDao buildingsDao;

	/**
	 * Setter for property 'buildingsDao'.
	 *
	 * @param buildingsDao Value to set for property 'buildingsDao'.
	 */
	public void setBuildingsDao(BuildingsDao buildingsDao) {
		this.buildingsDao = buildingsDao;
	}

	public List<Buildings> getBuildings(ArrayStack filters, Page pager) {
		StreetFilter filter = (StreetFilter) filters.peek();
		return buildingsDao.findBuildings(filter.getSelectedId(), pager);
	}
}
