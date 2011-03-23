package org.flexpay.common.dao;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Test;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;

import static org.junit.Assert.assertEquals;

public class TestCountOptimize extends SpringBeanAwareTestCase {

	@Test
	public void testCallCount1() {
		Long count = (Long) DataAccessUtils.uniqueResult(hibernateTemplate.find("select count(d) from Dual d"));
		assertEquals("Count(1) failed", Long.valueOf(1L), count);

		count = (Long) DataAccessUtils.uniqueResult(hibernateTemplate.find("select count(*) from Dual"));
		assertEquals("Count(1) failed", Long.valueOf(1L), count);

		count = (Long) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery("select count(d) from Dual d").iterate().next();
			}
		});
		assertEquals("Count(1) failed", Long.valueOf(1L), count);
	}
}
