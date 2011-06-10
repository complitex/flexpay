package org.flexpay.ab.sort;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.sorter.ApartmentSorter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class TestSortApartments extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentService apartmentService;

	@Test
	public void testSortApartments() {
		String hql = "select distinct a from Apartment a " +
					 "	left join a.apartmentNumbers an " +
					 "where a.status=0 and an.begin <= current_date() and an.end > current_date() and a.building.id=? " +
					 "order by lpad(convert(ifnull(an.value, '0'), UNSIGNED), 10, '0')";

		StopWatch watch = new StopWatch();

		Long buildingId = 1L;
		Object[] params = {buildingId};

		watch.start();
		List<?> result = jpaTemplate.find(hql, params);
		int size = result.size();
		watch.stop();

		assertNotSame("Apartments not found.", 0, size);
	}

	@Test
	public void testApartmentSorter() {

		ArrayStack filters = CollectionUtils.arrayStack(new BuildingsFilter(TestData.ADDR_IVANOVA_27));

		ObjectSorter sorter = new ApartmentSorter().activate();
		List<ObjectSorter> sorters = CollectionUtils.list(sorter);
		Page<Apartment> pager = new Page<Apartment>(30, 5);
		List<Apartment> apartments = apartmentService.find(filters, sorters, pager);

		assertFalse("No apartments found", apartments.isEmpty());

		StopWatch watch = new StopWatch();
		watch.start();
		List<Apartment> fullApartments = apartmentService.readFull(DomainObject.collectionIds(apartments), true);
		assertFalse("No apartments found 2", fullApartments.isEmpty());
		watch.stop();
	}
}
