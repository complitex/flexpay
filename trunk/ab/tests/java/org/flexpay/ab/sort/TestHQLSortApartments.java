package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.sorter.ApartmentSorter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class TestHQLSortApartments extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentService apartmentService;

	@Test
	public void testSortApartments() {

		ArrayStack filters = CollectionUtils.arrayStack(new BuildingsFilter(TestData.ADDR_IVANOVA_27));
		List<ObjectSorter> sorters = CollectionUtils.list(new ApartmentSorter().activate());
		Page<Apartment> pager = new Page<Apartment>();
		List<Apartment> apartments = apartmentService.find(filters, sorters, pager);

		assertFalse("No apartments found", apartments.isEmpty());
	}
}
