package org.flexpay.ab.sort;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.assertNotSame;

import java.util.List;

public class TestSortApartments extends SpringBeanAwareTestCase {

	@Test
	public void testSortBuildings() {
		String hql = "select distinct a from Apartment a " +
					 "	left join a.apartmentNumbers an " +
					 "where a.status=0 and an.begin <= current_date() and an.end > current_date() and a.building.id=? " +
					 "order by convert(ifnull(an.value, '0'), UNSIGNED)";

		StopWatch watch = new StopWatch();

		Long buildingId = 1L;
		Object[] params = {buildingId};

		watch.start();
		List<?> result = hibernateTemplate.find(hql, params);
		int size = result.size();
		watch.stop();

		assertNotSame("Apartments not found.", 0, size);
	}
}
