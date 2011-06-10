package org.flexpay.common.util.selftest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.jpa.JpaTemplate;

public class TestDual {

	private JpaTemplate jpaTemplate;

	public void doSelfTesting() throws Exception {
		int count = DataAccessUtils.intResult(jpaTemplate.findByNamedQuery("Dual.count"));

		if (count != 1) {
			throw new Exception("Dual table should contain only one record, found: " + count);
		}
	}

    @Required
	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}
}
