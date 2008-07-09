package org.flexpay.ab.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class AllObjectsDao {

	private HibernateTemplate hibernateTemplate;

	private long counter = 0;

	public void saveOrUpdate(DomainObject domainObject) {
		// remove object from hibernate session managed by hibernateTemplate
		hibernateTemplate.evict(domainObject);

		hibernateTemplate.saveOrUpdate(domainObject);

		++counter;
		if (counter == 15) {
			hibernateTemplate.flush();
			counter = 0;
		}
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void openSession() {
	}

	public void setRollbackOnly() {
	}

	public void closeSession() {
		hibernateTemplate.flush();
		hibernateTemplate.clear();
	}
}
