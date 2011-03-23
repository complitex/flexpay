package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.DistrictSorter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class TestHQLSortDistricts extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictService districtService;

	@Test
	public void testSortDistrict() {

		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TestData.TOWN_NSK));
		List<ObjectSorter> sorters = CollectionUtils.list(new DistrictSorter().activate());
		Page<District> pager = new Page<District>();
		List<District> districts = districtService.find(filters, sorters, pager);

		assertFalse("No districts found", districts.isEmpty());
	}
}
