package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.sorter.BuildingsSorter;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.junit.Assert.assertFalse;

public class TestHQLSortBuildings extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingService buildingService;
	@Autowired
	private AddressService addressService;

	@Test
	public void testSortBuildings() throws Exception {

		ArrayStack filters = CollectionUtils.arrayStack(new StreetFilter(TestData.IVANOVA));
		List<ObjectSorter> sorters = CollectionUtils.list(new BuildingsSorter().activate());
		Page<BuildingAddress> pager = new Page<BuildingAddress>();
		List<BuildingAddress> buildings = buildingService.findAddresses(filters, sorters, pager);

		assertFalse("No buildings found", buildings.isEmpty());

		for (BuildingAddress address : buildings) {
			log.debug(addressService.getBuildingsAddress(stub(address), null));
		}
	}
}
