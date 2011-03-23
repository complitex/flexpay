package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.sorter.CountrySorter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestHQLSortCountries extends AbSpringBeanAwareTestCase {

	@Autowired
	private CountryService countryService;

	@Test
	public void testSortCountries() {

		ArrayStack filters = CollectionUtils.arrayStack();
		List<ObjectSorter> sorters = CollectionUtils.list(new CountrySorter().activate());
		Page<Country> pager = new Page<Country>();
		List<Country> countries = countryService.find(filters, sorters, pager);

		assertFalse("No countries found", countries.isEmpty());
		assertEquals("Invalid fetch", pager.getTotalNumberOfElements(), countries.size());
	}
}
