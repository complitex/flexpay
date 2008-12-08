package org.flexpay.ab.sort;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import static org.junit.Assert.assertNotSame;

import java.util.List;

public class TestSortStreets extends SpringBeanAwareTestCase {

	@Test
	public void testSortStreetsbyName() {

		String hql = "select distinct s from Street s " +
					 "	left join s.nameTemporals tn " +
					 "	left join tn.value n " +
					 "	left outer join n.translations t1 with (t1.lang.id=?)" +
					 "	left outer join n.translations t2 with (t2.lang.id=?)" +
					 "	left join s.typeTemporals tt " +
					 "	left join tt.value tv " +
					 "where s.parent.id=? " +
					 "	and tn.invalidDate='2100-12-31' " +
					 "	and tn.begin <= current_date() and tn.end > current_date() " +
					 "	and tt.invalidDate='2100-12-31' " +
					 "	and tt.begin <= current_date() and tt.end > current_date() " +
					 "order by ifnull(t1.name, t2.name) DESC";

		StopWatch watch = new StopWatch();

		Long nskId = 2L;
		Long ruId = 1L;
		Long enId = 2L;
		Object[] params = {enId, ruId, nskId};

		watch.start();
		List<?> result = hibernateTemplate.find(hql, params);
		int size = result.size();
		watch.stop();

		assertNotSame("Streets not found.", 0, size);

		System.out.println("Time spent: " + watch + ", size: " + size);
	}

	@Test
	public void testSortStreetsByType() {

		String hql = "select distinct s from Street s " +
					 "	left join fetch s.typeTemporals tt " +
					 "	left join fetch tt.value tv " +
					 "	left outer join tv.translations t1 with (t1.lang.id=?) " +
					 "	left outer join tv.translations t2 with (t2.lang.id=?) " +
					 "where s.status=0 and s.parent.id=? " +
					 "	and tt.invalidDate='2100-12-31' " +
					 "	and tt.begin <= current_date() and tt.end > current_date() " +
					 "order by ifnull(t1.name, t2.name) ";

		StopWatch watch = new StopWatch();

		Long nskId = 2L;
		Long ruId = 1L;
		Long enId = 2L;
		Long[] params = {enId, ruId, nskId};

		watch.start();
		List<?> result = hibernateTemplate.find(hql, params);
		int size = result.size();
		watch.stop();

		assertNotSame("Streets not found.", 0, size);

		System.out.println("Time spent: " + watch + ", size: " + size);
	}

	@Test
	public void testSortStreetTypesbyName() {

		String hql = "select distinct s from StreetType s " +
					 "	left outer join s.translations t1 with (t1.lang.id=?) " +
					 "	left outer join s.translations t2 with (t2.lang.id=?) " +
					 "where s.status=0 " +
					 "order by ifnull(t1.name, t2.name)";

		StopWatch watch = new StopWatch();

		Long ruId = 1L;
		Long enId = 2L;
		Long[] params = {enId, ruId};

		watch.start();
		List<?> result = hibernateTemplate.find(hql, params);
		int size = result.size();
		watch.stop();

		assertNotSame("Street types not found.", 0, size);

		System.out.println("Time spent: " + watch + ", size: " + size);
	}
}
