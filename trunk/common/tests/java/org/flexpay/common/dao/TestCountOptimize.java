package org.flexpay.common.dao;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.junit.Assert.assertEquals;

public class TestCountOptimize extends SpringBeanAwareTestCase {

	@Test
	public void testCallCount1() {
		Long count = (Long) DataAccessUtils.uniqueResult(jpaTemplate.find("select count(d) from Dual d"));
		assertEquals("Count(1) failed", Long.valueOf(1L), count);

		count = (Long) DataAccessUtils.uniqueResult(jpaTemplate.find("select count(*) from Dual"));
		assertEquals("Count(1) failed", Long.valueOf(1L), count);

		count = (Long) jpaTemplate.execute(new JpaCallback<Object>() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createQuery("select count(d) from Dual d").getSingleResult();
			}
		});
		assertEquals("Count(1) failed", Long.valueOf(1L), count);
	}
}
