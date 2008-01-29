package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.common.dao.paging.Page;
import org.apache.commons.collections.ArrayStack;

import java.util.List;

public interface BuildingService {

	public List<Buildings> getBuildings(ArrayStack filters, Page pager);
}
