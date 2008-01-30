package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.ParentService;
import org.apache.commons.collections.ArrayStack;

import java.util.List;

public interface BuildingService extends ParentService<BuildingsFilter> {

	public List<Buildings> getBuildings(ArrayStack filters, Page pager);
}
