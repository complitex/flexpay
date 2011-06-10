package org.flexpay.ab.sort;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.junit.Test;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.assertNotSame;

public class TestSortStreets extends AbSpringBeanAwareTestCase {

	public static final Stub<Town> TOWN = new Stub<Town>(2L);

	@Test
	public void testSortStreetsByName() {

		final String hql = "select distinct s from Street s " +
						   "	left join s.nameTemporals tn " +
						   "	left join tn.value n " +
						   "	left outer join n.translations t1 with (t1.lang.id=?)" +
						   "	left outer join n.translations t2 with (t2.lang.id=?)" +
						   "where s.parent.id=? " +
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
						.setParameter(2, TOWN.getId())
						.setFirstResult(1)
						.setMaxResults(30)
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();

		assertNotSame("Streets not found.", 0, size);
	}

	@Test
	public void testSortAllStreetsbyName() {

		final String hql = "select distinct s from Street s " +
						   "	left join s.nameTemporals tn " +
						   "	left join tn.value n " +
						   "	left outer join n.translations t1 with (t1.lang.id=?)" +
						   "	left outer join n.translations t2 with (t2.lang.id=?)" +
						   "where s.parent.id=? " +
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
						.setParameter(2, TOWN.getId())
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();

		assertNotSame("Streets not found.", 0, size);
	}

	@Test
	public void testSortStreetsByType() {

		final String hql = "select distinct s from Street s " +
						   "	left join s.typeTemporals tt " +
						   "	left join tt.value tv " +
						   "	left outer join tv.translations t1 with (t1.lang.id=?) " +
						   "	left outer join tv.translations t2 with (t2.lang.id=?) " +
						   "where s.status=0 and s.parent.id=? " +
						   "	and tt.invalidDate='2100-12-31' " +
						   "	and tt.begin <= current_date() and tt.end > current_date() " +
						   "order by ifnull(t1.name, t2.name) ";

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
						.setParameter(2, TOWN.getId())
						.setFirstResult(1)
						.setMaxResults(30)
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();

		assertNotSame("Streets not found.", 0, size);
	}

	@Test
	public void testSortAllStreetsByType() {

		final String hql = "select distinct s from Street s " +
						   "	left join s.typeTemporals tt " +
						   "	left join tt.value tv " +
						   "	left outer join tv.translations t1 with (t1.lang.id=?) " +
						   "	left outer join tv.translations t2 with (t2.lang.id=?) " +
						   "where s.status=0 and s.parent.id=? " +
						   "	and tt.invalidDate='2100-12-31' " +
						   "	and tt.begin <= current_date() and tt.end > current_date() " +
						   "order by ifnull(t1.name, t2.name) ";

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
						.setParameter(2, TOWN.getId())
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();

		assertNotSame("Streets not found.", 0, size);
	}

	@Test
	public void testSortStreetTypesByName() {

		final String hql = "select distinct s from StreetType s " +
						   "	left outer join s.translations t1 with (t1.lang.id=?) " +
						   "	left outer join s.translations t2 with (t2.lang.id=?) " +
						   "where s.status=0 " +
						   "order by ifnull(t1.name, t2.name)";

		StopWatch watch = new StopWatch();

		final Long ruId = 1L;
		final Long enId = 2L;

		watch.start();
		List<?> result = jpaTemplate.executeFind(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createQuery(hql)
						.setParameter(0, enId)
						.setParameter(1, ruId)
						.getResultList();
			}
		});
		int size = result.size();
		watch.stop();

		assertNotSame("Street types not found.", 0, size);
	}
}
