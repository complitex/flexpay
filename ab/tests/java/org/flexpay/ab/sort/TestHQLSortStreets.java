package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.StreetSorterByName;
import org.flexpay.ab.persistence.sorter.StreetSorterByType;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestHQLSortStreets extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetService streetService;

	@Test
	public void testSortStreetsByName() {

		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TestData.TOWN_NSK));
		List<ObjectSorter> sorters = CollectionUtils.list(new StreetSorterByName().activate());
		Page<Street> pager = new Page<Street>();
		List<Street> streets = streetService.find(filters, sorters, pager);

		assertFalse("No streets found", streets.isEmpty());
	}

	@Test
	public void testSortStreetsByType() {

		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TestData.TOWN_NSK));
		List<ObjectSorter> sorters = CollectionUtils.list(new StreetSorterByType().activate());
		Page<Street> pager = new Page<Street>();
		List<Street> streets = streetService.find(filters, sorters, pager);

		assertFalse("No streets found", streets.isEmpty());
	}
}
