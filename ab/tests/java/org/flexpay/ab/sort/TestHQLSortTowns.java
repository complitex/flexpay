package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.sorter.TownSorterByName;
import org.flexpay.ab.persistence.sorter.TownSorterByType;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestHQLSortTowns extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownService townService;

	@Test
	public void testSortTownsByName() {

		ArrayStack filters = CollectionUtils.arrayStack(new RegionFilter(TestData.REGION_NSK));
		List<ObjectSorter> sorters = CollectionUtils.list(new TownSorterByName().activate());
		Page<Town> pager = new Page<Town>();
		List<Town> towns = townService.find(filters, sorters, pager);

		assertFalse("No towns found", towns.isEmpty());

		for (Town townStub : towns) {
			Town town = townService.readFull(stub(townStub));
			assertNotNull("Town found in sorting, but not readFull", town);
			TownName name = town.getCurrentName();
			assertNotNull("Town name not found", name);
			log.debug(name.getDefaultNameTranslation());
		}
	}

	@Test
	public void testSortTownsByType() {

		ArrayStack filters = CollectionUtils.arrayStack(new RegionFilter(TestData.REGION_NSK));
		List<ObjectSorter> sorters = CollectionUtils.list(new TownSorterByType().activate());
		Page<Town> pager = new Page<Town>();
		List<Town> towns = townService.find(filters, sorters, pager);

		assertFalse("No towns found", towns.isEmpty());
	}
}
