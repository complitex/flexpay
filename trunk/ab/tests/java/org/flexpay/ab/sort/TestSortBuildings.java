package org.flexpay.ab.sort;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.assertNotSame;

import java.util.List;

public class TestSortBuildings extends SpringBeanAwareTestCase {

	@Test
	public void testSortBuildings() {
		String hql = "select distinct b from Building b " +
					 "	left join b.buildingses bs " +
					 "	left join bs.buildingAttributes ba1 with (ba1.buildingAttributeType.id=?) " +
					 "	left join bs.buildingAttributes ba2 with (ba2.buildingAttributeType.id=?) " +
					 "where bs.status=0 and bs.primaryStatus=true and bs.street.id=? " +
					 "order by lpad(convert(ifnull(ba1.value, '0'), UNSIGNED), 10, '0') || lpad(convert(ifnull(ba2.value, '0'), UNSIGNED), 10, '0') ";

		StopWatch watch = new StopWatch();

		Long streetId = 2L;
		Long typeNumberId = 1L;
		Long typebulkId = 2L;
		Long[] params = {typeNumberId, typebulkId, streetId};

		watch.start();
		List<?> result = hibernateTemplate.find(hql, params);
		int size = result.size();
		watch.stop();

		assertNotSame("Buildings not found.", 0, size);
	}
}
