package org.flexpay.ab.sort;

import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestHQLSortStreets extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetService streetService;

	@Test
	public void testSortStreetsByName() {

/*
		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TestData.TOWN_NSK));
		List<ObjectSorter> sorters = CollectionUtils.list(new StreetSorterByName().activate());
		Page<Street> pager = new Page<Street>();
		List<Street> streets = streetService.find(filters, sorters, pager);

		assertFalse("No streets found", streets.isEmpty());

		for (Street streetStub : streets) {
			Street street = streetService.readFull(stub(streetStub));
			assertNotNull("Street found in sorting, but not readFull", street);
			StreetName name = street.getCurrentName();
			assertNotNull("Street name not found", name);
			log.debug(name.getDefaultNameTranslation());
		}
*/
	}

	@Test
	public void testSortStreetsByType() {

/*
		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TestData.TOWN_NSK));
		List<ObjectSorter> sorters = CollectionUtils.list(new StreetSorterByType().activate());
		Page<Street> pager = new Page<Street>();
		List<Street> streets = streetService.find(filters, sorters, pager);

		assertFalse("No streets found", streets.isEmpty());
*/
	}
}
