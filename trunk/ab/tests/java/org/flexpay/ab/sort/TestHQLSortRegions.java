package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.sorter.RegionSorter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestHQLSortRegions extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionService regionService;

	@Test
	public void testSortRegions() {

		ArrayStack filters = CollectionUtils.arrayStack(new CountryFilter(TestData.COUNTRY_RUS));
		List<ObjectSorter> sorters = CollectionUtils.list(new RegionSorter().activate());
		Page<Region> pager = new Page<Region>();
		List<Region> regions = regionService.find(filters, sorters, pager);

		assertFalse("No regions found", regions.isEmpty());
	}
}
