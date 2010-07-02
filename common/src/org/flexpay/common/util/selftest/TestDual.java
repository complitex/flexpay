package org.flexpay.common.util.selftest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class TestDual {

	private HibernateTemplate hibernateTemplate;

	public void doSelfTesting() throws Exception {
		int count = DataAccessUtils.intResult(hibernateTemplate.findByNamedQuery("Dual.count"));

		if (count != 1) {
			throw new Exception("Dual table should contain only one record, found: " + count);
		}
	}

    @Required
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
