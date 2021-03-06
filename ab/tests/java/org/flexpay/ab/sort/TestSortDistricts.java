package org.flexpay.ab.sort;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.assertNotSame;

public class TestSortDistricts extends AbSpringBeanAwareTestCase {

	@Test
	public void testSortDistrictsByName() {

		final String hql = "select distinct o from District o " +
						   "	left join o.nameTemporals tn " +
						   "	left join tn.value n " +
						   "	left outer join n.translations t1 with (t1.lang.id=?)" +
						   "	left outer join n.translations t2 with (t2.lang.id=?)" +
						   "where o.parent.id=? " +
						   "	and tn.invalidDate='2100-12-31' " +
						   "	and tn.begin <= current_date() and tn.end > current_date() " +
						   "order by ifnull(t1.name, t2.name) DESC";

		StopWatch watch = new StopWatch();

		final Long ruId = 1L;
		final Long enId = 2L;

		watch.start();
		List<?> result = jpaTemplate.executeFind(new JpaCallback() {
			@Override
			public List<?> doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createQuery(hql)
						.setParameter(0, enId)
						.setParameter(1, ruId)
						.setParameter(2, TestData.TOWN_NSK.getId())
						.setFirstResult(1)
						.setMaxResults(30)
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();
		log.debug("Time spent sorting 1-30 districts {}", watch);

		assertNotSame("Streets not found.", 0, size);
	}

	@Test
	public void testSortAllDistrictsByName() {

		final String hql = "select distinct o from District o " +
						   "	left join o.nameTemporals tn " +
						   "	left join tn.value n " +
						   "	left outer join n.translations t1 with (t1.lang.id=?)" +
						   "	left outer join n.translations t2 with (t2.lang.id=?)" +
						   "where o.parent.id=? " +
						   "	and tn.invalidDate='2100-12-31' " +
						   "	and tn.begin <= current_date() and tn.end > current_date() " +
						   "order by ifnull(t1.name, t2.name) DESC";

		StopWatch watch = new StopWatch();

		final Long ruId = 1L;
		final Long enId = 2L;

		watch.start();
		List<?> result = jpaTemplate.executeFind(new JpaCallback() {
			@Override
			public List<?> doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createQuery(hql)
						.setParameter(0, enId)
						.setParameter(1, ruId)
						.setParameter(2, TestData.TOWN_NSK.getId())
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();

		log.debug("Time spent sorting all districts {}", watch);

		assertNotSame("Districts not found.", 0, size);
	}
}
