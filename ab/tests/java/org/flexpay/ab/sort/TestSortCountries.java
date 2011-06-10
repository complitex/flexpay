package org.flexpay.ab.sort;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.assertNotSame;

public class TestSortCountries extends AbSpringBeanAwareTestCase {

	@Test
	public void testSortCountriesByName() {

		final String hql = "select distinct o from Country o " +
						   "	left outer join o.translations t1 with (t1.lang.id=?)" +
						   "	left outer join o.translations t2 with (t2.lang.id=?)" +
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
						.setFirstResult(1)
						.setMaxResults(30)
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();
		log.debug("Time spent sorting 1-30 countries {}", watch);

		assertNotSame("Streets not found.", 0, size);
	}

	@Test
	public void testSortAllCountriesByName() {

		final String hql = "select distinct o from Country o " +
						   "	left outer join o.translations t1 with (t1.lang.id=?)" +
						   "	left outer join o.translations t2 with (t2.lang.id=?)" +
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
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();

		log.debug("Time spent sorting all countries {}", watch);

		assertNotSame("countries not found.", 0, size);
	}
}
