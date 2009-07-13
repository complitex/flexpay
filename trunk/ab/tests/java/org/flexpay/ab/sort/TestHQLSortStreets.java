package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.StreetSorterByName;
import org.flexpay.ab.persistence.sorter.StreetSorterByType;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestHQLSortStreets extends AbSpringBeanAwareTestCase {

	public static final Stub<Town> TOWN = TestSortStreets.TOWN;

	@Autowired
	private StreetService streetService;

	@Test
	public void testSortStreetsByName() {

		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TOWN.getId()));

		ObjectSorter sorter = new StreetSorterByName();
		sorter.activate();
		List<ObjectSorter> sorters = CollectionUtils.list(sorter);
		Page<Street> pager = new Page<Street>();
		List<Street> streets = streetService.find(filters, sorters, pager);

		assertFalse("No streets found", streets.isEmpty());
		assertEquals("Invalid fetch", pager.getTotalNumberOfElements(), streets.size());
	}

	@Test
	public void testSortStreetsByType() {

		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TOWN.getId()));

		ObjectSorter sorter = new StreetSorterByType();
		sorter.activate();
		List<ObjectSorter> sorters = CollectionUtils.list(sorter);
		Page<Street> pager = new Page<Street>();
		List<Street> streets = streetService.find(filters, sorters, pager);

		assertFalse("No streets found", streets.isEmpty());
		assertEquals("Invalid fetch", pager.getTotalNumberOfElements(), streets.size());
	}
}
