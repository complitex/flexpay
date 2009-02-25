package org.flexpay.eirc.util.selftest;

import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.persistence.Consumer;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Test application and database for corrections to not existing consumers
 */
public class TestConsumersCorrections {

	private HibernateTemplate hibernateTemplate;
	private ClassToTypeRegistry classToTypeRegistry;

	public void doSelfTesting() throws Exception {
		String hql = "select count(*) from DataCorrection dc where dc.objectType=? and " +
				"not exists (select c.id from Consumer c where c.id=dc.internalObjectId)";
		int invalidCorrectionsNumber = DataAccessUtils.intResult(
				hibernateTemplate.find(hql, classToTypeRegistry.getType(Consumer.class)));

		if (invalidCorrectionsNumber != 0) {
			throw new Exception("Corrections found referenced not existing or deleted consumers, need cleanup.");
		}
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}
}
